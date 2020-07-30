package com.itheima.springmvc.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常处理器的自定义的实现类
 * @author lx
 *
 */
public class CustomExceptionResolver implements HandlerExceptionResolver{

	public ModelAndView resolveException(HttpServletRequest request, 
			HttpServletResponse response, Object obj,
			Exception e) {
		// TODO Auto-generated method stub  发生异常的地方 Serivce层  方法  包名+类名+方法名（形参） 字符串
		//日志    1.发布 tomcat war  Eclipse  2.发布Tomcat  服务器上  Linux  Log4j
		
		ModelAndView mav = new ModelAndView();
		//判断异常为类型
		if(e instanceof MessageException){
			//预期异常
			MessageException me = (MessageException)e;
			mav.addObject("error", me.getMsg());
		}else{
			mav.addObject("error", "未知异常");
		}
		mav.setViewName("error");
		return mav;
	}

}
