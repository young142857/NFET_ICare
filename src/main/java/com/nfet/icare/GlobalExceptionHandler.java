package com.nfet.icare;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局Exception捕获，返回404页面无需在每个Controller中逐个定义
 * @author mark
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String DEFAULT_ERROR_VIEW = "404";
	public static final String MGR_DEFAULT_ERROR_VIEW = "backstage404";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        logger.info("Exception:"+e);
        mav.addObject("url", req.getRequestURL());
        logger.info("url:"+req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        
        //PC端报错跳新的404界面
        if (req.getRequestURL().indexOf("mgr") != -1) {
        	logger.info("error in MGR");
        	mav.setViewName(MGR_DEFAULT_ERROR_VIEW);
		}
        
        return mav;
    }
}
