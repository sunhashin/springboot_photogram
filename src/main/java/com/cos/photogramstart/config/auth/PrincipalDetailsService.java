package com.cos.photogramstart.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service  // IoC
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	// 1. 패스워드는 스프링 시큐리티가 알아서 체크해주니까 신경쓰지 않아도 됨
	// 2. 리턴이 잘 되면 자동으로 UserDetails 타입을 세션으로 만든다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		System.out.println("========================호출되었나요?" + username);
		User userEntity = userRepository.findByUsername(username);
		
		if (userEntity == null) {
			return null;
		} else {
			return new PrincipalDetails(userEntity);  // 결국은 PrincipalDetails 가 리턴되면서 세션에 저장됨 (userEntity 넘김)
		}
	}

}
