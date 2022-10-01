package com.cos.photogramstart.web.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.service.LikesService;
import com.cos.photogramstart.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ImageApiController {
	
	private final ImageService imageService;
	private final LikesService likesSerivce;
	
	@GetMapping("/api/image")
    public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails,  // 내림 차순을 @pageable 에 주는 경우
			@PageableDefault(size=3,  sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
	/*public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails,  // 네이티브 쿼리에서 직접 주는 경우
			@PageableDefault(size=3) Pageable pageable) {*/
		
		Page<Image> images = imageService.이미지스토리(principalDetails.getUser().getId(), pageable);
		
		 // 무한참조 오류 발생 :
		// images 를 리턴하면 메시지 컨버터가 모든 getter 호출해서 json 바꿔서 던져줌, likes 안에서 image 호출 -> 다시 likes 호출 무한반복
		// likes -> image -> user -> image -> likes 무한참조
		return new ResponseEntity<>(new CMRespDto<>(1, "성공", images), HttpStatus.OK); 
	}
	
	@PostMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> likes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		likesSerivce.좋아요(imageId, principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공", null), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> unLikes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		likesSerivce.좋아요취소(imageId, principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 취소 성공", null), HttpStatus.OK);
	}
}
