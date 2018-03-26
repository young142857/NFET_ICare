package com.nfet.icare.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.nfet.icare.pojo.DeviceInfo;
import com.nfet.icare.service.ParseExcelServiceImpl;

/**
 * 解析Excel
 *  
 * 
 * 
 */


@Controller
public class ParseExcelController {
	
	@Autowired
	private ParseExcelServiceImpl parseExcelServiceImpl;
	
	@RequestMapping("/insertDeviceInfo")
	public void insertDeviceInfo() throws IOException{
		InputStream is = new FileInputStream("C:\\Users\\起、\\Desktop\\customer_device_Info.xlsx");
		List<DeviceInfo> deviceInfos = parsing(is);
		int count = parseExcelServiceImpl.insertDeviceInfo(deviceInfos);
		System.out.println(count);
	}
	
	public List<DeviceInfo> parsing(InputStream is) throws IOException{
		List<DeviceInfo> deviceInfos = new ArrayList<>();		
		//读取Excel
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		//获取行数
		for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet sheet = workbook.getSheetAt(numSheet);
			if (sheet == null) {
				continue;
			}	
			//从第二行开始
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				System.out.println(sheet.getLastRowNum());
				XSSFRow row = sheet.getRow(rowNum);
				if (row != null) {
					DeviceInfo deviceInfo = new DeviceInfo();
					XSSFCell  deviceNo = row.getCell(1); 
					XSSFCell  name = row.getCell(2); 
					XSSFCell  type = row.getCell(4); 
					XSSFCell  mfgDate = row.getCell(14);
					
					System.out.println(deviceNo);
					deviceInfo.setDeviceNo(deviceNo.toString());
					deviceInfo.setName(name.toString());
					deviceInfo.setType(type.toString());
					
					//将Excel中的日期转成字符串
					String result = new String();
					if (mfgDate != null ) {
						if (HSSFDateUtil.isCellDateFormatted(mfgDate)) {  
		                    SimpleDateFormat sdf = null;  
		                    if (mfgDate.getCellStyle().getDataFormat() == HSSFDataFormat  
		                            .getBuiltinFormat("h:mm")) {  
		                        sdf = new SimpleDateFormat("HH:mm");  
		                    } else {// 日期  
		                        sdf = new SimpleDateFormat("yyyy/MM/dd");  
		                    }  
		                    Date date = mfgDate.getDateCellValue();  
		                    result = sdf.format(date);  
		                } else if (mfgDate.getCellStyle().getDataFormat() == 58) {  
		                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
		                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		                    double value = mfgDate.getNumericCellValue();  
		                    Date date = org.apache.poi.ss.usermodel.DateUtil  
		                            .getJavaDate(value);  
		                    result = sdf.format(date);  
		                } else {  
		                    double value = mfgDate.getNumericCellValue();  
		                    CellStyle style = mfgDate.getCellStyle();  
		                    DecimalFormat format = new DecimalFormat();  
		                    String temp = style.getDataFormatString();  
		                    // 单元格设置成常规  
		                    if (temp.equals("General")) {  
		                        format.applyPattern("#");  
		                    }  
		                    result = format.format(value);  
		                } 
						deviceInfo.setMfgDate(result);
					} 
					
					deviceInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
					deviceInfo.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			        deviceInfos.add(deviceInfo);						            			        
				}
			}					
		}
		
		return deviceInfos;
	}
}


