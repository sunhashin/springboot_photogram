package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

 
@RequiredArgsConstructor  // final 이 걸려 있는 모든 아규먼트에 생성자를 만들어줌 (롬복 어노테이션), final field를 DI 할때
@Controller // 1. IoC, 2. 파일을 리턴하는 컨트롤러 
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	private final AuthService authService;  // 자바에서는 전역변수에 final 이 걸려 있으면 무조건 생성자 or 객체가 만들어질 때 초기화 해줘야 함
	/*
//	private AuthService authService;
//	public AuthController(AuthService authService) {
//		
//	}
	*/
	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}
	
	// 회원가입 버튼 -> /auth/singup -> /auth/signin (의도)
	// 회원가입 버튼 -> x (실제) csrf token 이 활성화 되어 있기 때문
	@PostMapping("/auth/signup")
	//public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) {  // key=value (x-www-form-urlencoded)
	// public @ResponseBody String signup(@Valid SignupDto signupDto, BindingResult bindingResult) {  //@Controller 이라도 @ResponseBody 붙어있으면 데이터 리턴
	public  String signup(@Valid SignupDto signupDto, BindingResult bindingResult) {
 
		//전처리 -> 필터로 전환 가능
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
				
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			//throw new RuntimeException("유효성 검사 실패");
			throw new CustomValidationException("유효성 검사 실패함", errorMap);
		} else {
			// User <- SignupDto 에 넣음 (회원가입 처리)
			User user = signupDto.toEntity();
			authService.회원가입(user);
			
			// 로그를 남기는 후처리
			return "auth/signin";
		}

//		System.out.println("signup 실행됨?");
	}
}
