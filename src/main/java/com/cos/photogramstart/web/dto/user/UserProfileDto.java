package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
	private boolean pageOwnerState;  // jstl 에서 is 붙으면 파싱이 잘안됨
	private int imageCount;
	private boolean subscribeState;
	private int subscribeCount;
	private User user;
}
