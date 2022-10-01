package com.cos.photogramstart.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController
@ControllerAdvice  // 모든 예외를 낚아챔
public class ControllerExcecptionHandler {
	
	//@ExceptionHandler(CustomValidationException.class)  // 런타임 익셉션이 발생하는 모든 익셉션을 가로챔
	//public Map<String, String> validationException(CustomValidationException e) {
	// public CMRespDto validationException(CustomValidationException e) {
	// public CMRespDto<Map<String, String>> validationException(CustomValidationException e) {
//	public CMRespDto<?> validationException(CustomValidationException e) {
//		
//		// return e.getErrorMap();  // return type 이 String 이고 @RestController 라서 데이터를 리턴함
//		// return new CMRespDto(e.getMessage(), e.getErrorMap());  // 사용자 정의 에러 타입 정의한 dto 형태로 리턴 에러메시지 맵을 같이 넘기이 위해서 만듬 
//		return new CMRespDto<Map<String, String>>(-1, e.getMessage(), e.getErrorMap());  // 상태코드 추가하고 제네릭으로 만듬	
	// return new CMRespDto(-1, e.getMessage(),  "문자열 리턴하고 싶어");  // 전역적으로 사용하기 위해서 상태코드 추가하고 제네릭으로 만듬
//	}
	
	@ExceptionHandler(CustomValidationException.class)  // 런타임 익셉션이 발생하는 모든 익셉션을 가로챔, 자바 스크립트 리턴
	public String validationException(CustomValidationException e) {
		// CMRespDto, Script 비교
		// 1. 클라이언트에게 응답할때는 Script 좋음 (브라우저가 받음)
		// 2. Ajax 통신 - CMRespDto
		// 3. Android 통신 - CMRespDto
		System.out.println("===============불려졌나요?");
		if (e.getErrorMap() == null) {
			return Script.back(e.getMessage());  // util package 안에 StringBuffer 로 자바스크립트 alert 띄위는 식으로 에러 메시지 표시하는 샘플
		} else {
			return Script.back(e.getErrorMap().toString());  // util package 안에 StringBuffer 로 자바스크립트 alert 띄위는 식으로 에러 메시지 표시하는 샘플
		}
		
	}
	
	@ExceptionHandler(CustomValidationApiException.class)  // ajax 통신시 ResponseEntity 를 사용하면 Http 상태코드를 반환하여 dom 에서 자동으로 분기하게 하려고
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
		System.out.println("====================== 나 실행됨");
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class)  // ajax 통신시 ResponseEntity 를 사용하면 Http 상태코드를 반환하여 dom 에서 자동으로 분기하게 하려고
	public ResponseEntity<?> apiException(CustomApiException e) {
		System.out.println("====================== 나 api 예외 실행됨");
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	

	@ExceptionHandler(CustomException.class)  // 런타임 익셉션이 발생하는 모든 익셉션을 가로챔, 자바 스크립트 리턴
	public String exception(CustomException e) {
		// CMRespDto, Script 비교
		// 1. 클라이언트에게 응답할때는 Script 좋음 (브라우저가 받음)
		// 2. Ajax 통신 - CMRespDto
		// 3. Android 통신 - CMRespDto
		System.out.println("===============불려졌나요?");
		
		return Script.back(e.getMessage()); 
		
	}
	
}
