package com.cos.photogramstart.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	
	@GetMapping("/user/{pageUserId}")  // 해당 페이지의 주인 아이디 받음
	public String profile(@PathVariable int pageUserId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		// User userEntity = userService.회원프로필(pageUserId, principalDetails.getUser().getId());
		// model.addAttribute("user", userEntity);
		UserProfileDto dto  = userService.회원프로필(pageUserId, principalDetails.getUser().getId());

		model.addAttribute("dto", dto);
		
		return "/user/profile";
	}
	
	@GetMapping("/user/{id}/update")
/*	public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {*/
	public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
	/*	
	 * // 1 추천
		System.out.println("세션 정보 : " + principalDetails.getUser());
		
		// 2 비추
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails mPrincipleDetails = (PrincipalDetails) auth.getPrincipal();*/
		
		// Getter 호출되서 에러, 내부적으로 상호 참조 되서 위험한 코드
		// noSession 이 난 경우 @Transactional 을 안붙여서 ?(open-in-view : false 가 아니라?) controller 단에서 디비 연결이 끊어져서  lazyloading 이 안되었거나
		// sysout 을 찍는 코드가 있어 순환 참조가 발생하는지 확인 필요
		// System.out.println("직접 찾은 세션 정보 : " + mPrincipleDetails.getUser());
		
		// principal : 인증 주체 (접근주체) 라는 뜻
		// 뷰에서 표시하기 위해 매핑하기
		// model.addAttribute("principal", principalDetails.getUser());
		return "/user/update";
	}
}
