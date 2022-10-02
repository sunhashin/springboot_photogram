package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;

@Component  // RestController, Service 모든것들이 Component 를 상속해서 만들어져 있음(구현체)
@Aspect   // AOP 처리할 수 있는 핸들러가 됨
public class ValidationAdvice {
	
	// 접근한정자(public/private/protected 모두 : * com~.*Controller.* (모든 함수) (..) // 모든 파라미터에 적용한다는 의미
	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))")  // 함수 시작 전후 모두, @Before : 함수 시작 직전, @After : 함수 종료된 직후
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		// System.out.println("  web api 컨트롤러 ======================================");
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
						
					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성 검사 실패함", errorMap);
				} 
			}
		}
		// proceedingJoinPoint => profile 함수(예제) 의 모든 곳에 접근할 수 있는 함수
		// profile 함수보다 먼저 실행
		
		return proceedingJoinPoint.proceed();  // profile 함수가 실제로 실행되는 부분, null 반환시 실제 해당 profile 함수 동작 안함
	}
	
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		 // 함수의 매개변수에 접근해서 그 매개변수가 어떤것이 있는지 뽑아 보는 것
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof BindingResult) {
				// System.out.println("유효성 검사를 하는 함수입니다.");
				
				BindingResult bindingResult = (BindingResult) arg;
				
				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
						
					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성 검사 실패함", errorMap);
				} 
			}
		}
		return proceedingJoinPoint.proceed();
	}
}
