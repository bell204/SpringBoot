package org.zerock.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
 
@Aspect
@Component
public class AfterReturningAdvice {
	@Before("execution(* *..*ServiceImpl.*(..))")
	public void startLog(JoinPoint jp) {
		System.out.println("메서드 시작"+jp.getSignature());
	}

	
	@Pointcut("execution(* *..*ServiceImpl.*(..))")
	public void allPointcut(){}
	 
	@AfterReturning(pointcut="allPointcut()", returning="returnObj")
	public void afterLog(JoinPoint jp, Object returnObj){
		//비즈니스 메소드가 리턴한 결과 데이터를 다른 용도로 처리할 때 사용한다.
		String methodName = jp.getSignature().getName();
		 
		System.out.println("[메소드 리턴] " + methodName 
						+ "() 메소드 리턴값 : " + returnObj.toString());
		
	}
}






