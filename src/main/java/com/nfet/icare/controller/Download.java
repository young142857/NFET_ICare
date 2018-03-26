package com.nfet.icare.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * excel模板下载
 * 
 * @author zhoupx
 *
 */
@Controller
@RequestMapping("/mgr")
public class Download {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/download")
	@ResponseBody
	public void download(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="param",required=false) String param)
			throws FileNotFoundException, IOException {
		
		// 模板路径
		String path = request.getServletContext().getRealPath("/");
		String fileName = "";
		logger.info("下载的文件是："+ param);
		if(param.equals("customerinfo"))
			fileName = "customer_Info";
		else if(param.equals("customer_company"))
			fileName = "customer_company_Info";
		else {
			fileName = "customer_device_Info";
		}
		
		String str = path + fileName + ".xlsx";
		logger.info("下载的文件是：" + str);
		
		File f = new File(str);
		response.reset();
		//response.setContentType("multipart/form-data");

		try {
			String encodedfileName = null;
			String agent = request.getHeader("USER-AGENT");
			if (null != agent && -1 != agent.indexOf("MSIE")) {// IE
				encodedfileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			} else if (null != agent && -1 != agent.indexOf("Mozilla")) {
				encodedfileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
			} else {
				encodedfileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			}
			
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + ".xlsx" + "\"");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ServletOutputStream out = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			out = response.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(f));
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[bis.available()];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}

	}

}
