package com.cos.photogramstart.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service  // Ioc 로 등록. 타입을 맞춰줘야 함
public class OAuth2DetailsService extends DefaultOAuth2UserService{
	
	private final UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		System.out.println("oAuth2 서비스 탐");
		
		OAuth2User oauth2User = super.loadUser(userRequest);  // 응답 정보를 통해서 파싱해서 담아줌
//		System.out.println(oauth2User.getAttributes());
		
		Map<String, Object> userinfo = oauth2User.getAttributes();
		String username = "facebook_" + (String) userinfo.get("id");
		String password =  new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());  // bCrypt 로 암호화 해서 들어가야함
		String name = (String) userinfo.get("name");
		String email = (String) userinfo.get("email");
		
		User userEntity = userRepository.findByUsername(username);
		
		if (userEntity == null) { // 최초 로그인
			
			User user = User.builder()
					.username(username)    // oauth 로그인 하는 경우 유저이름과 패스워드는 모름, 중복되지 않게 임의로 만들어줘야함
					.password(password)
					.email(email)
					.name(name)
					.role("ROLE_USER")
					.build();
					
			 // 회원 가입 처리하고 리턴
			// 별도 oAuth2Details 를 따로 만들면 세션 정보 접근하는 곳마다 2곳씩 분기처리 해야 하므로 하나로 PrincipalDetails 합쳐서 구현 (오버로딩 처리)
			return new PrincipalDetails(userRepository.save(user), oauth2User.getAttributes());  
			
		} else {  // 페이스북으로 이미 회원가입이 되어 있다는 뜻
			return new PrincipalDetails(userEntity);
		} 
		 
	}
}
