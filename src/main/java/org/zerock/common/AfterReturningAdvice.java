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
		System.out.println("�޼��� ����"+jp.getSignature());
	}

	
	@Pointcut("execution(* *..*ServiceImpl.*(..))")
	public void allPointcut(){}
	 
	@AfterReturning(pointcut="allPointcut()", returning="returnObj")
	public void afterLog(JoinPoint jp, Object returnObj){
		//����Ͻ� �޼ҵ尡 ������ ��� �����͸� �ٸ� �뵵�� ó���� �� ����Ѵ�.
		String methodName = jp.getSignature().getName();
		 
		System.out.println("[�޼ҵ� ����] " + methodName 
						+ "() �޼ҵ� ���ϰ� : " + returnObj.toString());
		
	}
}






