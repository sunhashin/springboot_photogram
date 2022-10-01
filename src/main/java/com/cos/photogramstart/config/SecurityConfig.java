package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // 해당 파일(지금 이파일)로 시큐리티 활성화
@Configuration // IOC  
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	 
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// TODO Auto-generated method stub
//		// 부모 httpsecurity 설정 상속받아서 권한 자동 설정함, 생략시 권한 체크 안함
//		super.configure(http);
//	}
	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// super 삭제 - 기존 시큐리티가 가지고 있는 기능이 다 비활성화 됨
		
		// postman 등 툴로 날리거나 직접 폼으로 날리거나 체크안함
		http.csrf().disable(); 
		
		http.authorizeRequests()
			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated()  // 해당주소만 인증이 필요하고
			.anyRequest().permitAll() // 그 외에는 모두 허용
			.and()
			.formLogin()  // 로그인 요구 페이지 주소인 경우 폼 로그인을 사용해서 
			.loginPage("/auth/signin")  // 로그인 페이지로 이동하고 GET - 인증필요한 페이지에 인증이 안되어 있는 경우 호출
			.loginProcessingUrl("/auth/signin")  // 로그인 POST 요청  - 스프링 시큐리티가 로그인 프로세스 진행
			.defaultSuccessUrl("/");  // 성공하면 / 로 보냄
	}
}
