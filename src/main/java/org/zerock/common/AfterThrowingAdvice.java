package org.zerock.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AfterThrowingAdvice {
	@Pointcut("execution(* org.zerock.service..*ServiceImpl.*(..))")
	public void allPointcut(){}
	
	@AfterThrowing(pointcut="allPointcut()", throwing="exceptObj")
	public void exceptionLog(JoinPoint jp, Exception exceptObj){
		//����Ͻ� �޼ҵ� ���� ���߿� ���ܰ� �߻����� ��,
		//�������� ����ó�� ������ ������ �������� ����ϴ� �����̽��̴�.
		
		String methodName = jp.getSignature().getName();
		System.out.println(methodName + "() �޼ҵ� ������ ���� �߻�!");
		
		if(exceptObj instanceof IllegalArgumentException){
			System.out.println("�������� ���� �ԷµǾ����ϴ�.");
		}else if(exceptObj instanceof NumberFormatException){
			System.out.println("���� ������ ���� �ƴմϴ�.");
		}else if(exceptObj instanceof Exception){
			System.out.println("���ܰ� �߻��߽��ϴ�.");
			System.out.println("���ܸ� : " + exceptObj.getMessage());
		}else{
			System.out.println("���ܰ� �߻��߽��ϴ�.");
		}
		
	}
}
