package com.itheima.spring.demo1;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 切面类：注解的切面类
 * @author jt
 */
@Aspect
public class MyAspectAnno {

	@Before(value="MyAspectAnno.pointcut2()")
	public void before(){
		System.out.println("前置增强===========");
	}
	
	// 后置通知:
	@AfterReturning(value="MyAspectAnno.pointcut4()",returning="result")
	public void afterReturning(Object result){
		System.out.println("后置增强==========="+result);
	}
	
	// 环绕通知:
	@Around(value="MyAspectAnno.pointcut3()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
		System.out.println("环绕前增强==========");
		Object obj  = joinPoint.proceed();
		System.out.println("环绕后增强==========");
		return obj;
	}
	
	// 异常抛出通知:
	@AfterThrowing(value="MyAspectAnno.pointcut1()",throwing="e")
	public void afterThrowing(Throwable e){
		System.out.println("异常抛出增强========="+e.getMessage());
	}
	
	// 最终通知
	@After(value="MyAspectAnno.pointcut1()")
	public void after(){
		System.out.println("最终增强============");
	}
	
	// 切入点注解：
	@Pointcut(value="execution(* com.itheima.spring.demo1.OrderDao.find(..))")
	private void pointcut1(){}
	@Pointcut(value="execution(* com.itheima.spring.demo1.OrderDao.save(..))")
	private void pointcut2(){}
	@Pointcut(value="execution(* com.itheima.spring.demo1.OrderDao.update(..))")
	private void pointcut3(){}
	@Pointcut(value="execution(* com.itheima.spring.demo1.OrderDao.delete(..))")
	private void pointcut4(){}
}
