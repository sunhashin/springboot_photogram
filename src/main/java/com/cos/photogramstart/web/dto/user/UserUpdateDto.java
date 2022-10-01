package com.cos.photogramstart.web.dto.user;

import javax.validation.constraints.NotBlank;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class UserUpdateDto {
	@NotBlank
	private String name;  // 필수
	@NotBlank
	private String password;  // 필수
	
	private String website;
	private String bio;
	private String phone;
	private String gender;
	
	// 필수와 아닌 데이터가 섞여 있는 경우 사용하기 위험한 코드, 수정이 필요함
	public User toEntity() {
		return User.builder()
				.name(name)  // 이름을 기재 안했으면 문제 Validation check
				.password(password)  // 패스워드를 기재안했으면 공백으로 들어오면 디비에 공백 패스워드가 들어가는 것이 문제 validation check 필요
				.website(website)
				.bio(bio)
				.phone(phone)
				.gender(gender)
				.build();
	}
}
