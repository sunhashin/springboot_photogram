package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final SubscribeRepository subscribeRepository;

	@Value("${file.path}")  // application.yml 파일에 사용자 정의한 경로 프로퍼티 정보 가져옴
	private String uploadFolder;
	
	@Transactional
	public User 회원프로필사진변경(int principalId, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID(); // uuid

		String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename(); // 1.jpg (업로드 하려는 원래 파일명) : uuid 더해서 안겹치게 만듬
		System.out.println("이미지 파일 이름 : " + imageFileName);

		Path imageFilePath = Paths.get(uploadFolder + imageFileName);

		// 통신이 일어나거나 I/O 일어날 때 예외가 발생할 수 있음 (런타임시에만 체크할 수 있음
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());  // 이미 파일이라서 getFile() 이 필요 없음
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(()->{
			throw new CustomApiException("유저를 찾을 수 없습니다.");
		});
		
		userEntity.setProfileImageUrl(imageFileName);  // 세션에 업데이트 해줘야 해서
		
		return userEntity;
		
	}  // 더티체킹으로 업데이트 됨

	@Transactional(readOnly = true) // 셀렉트하는 곳도 트랜잭션 걸어주면 좋음
	// public User 회원프로필(int pageUserId, int principalId) { // 해당 페이지의 아이디, 로그인한 아이디
	public UserProfileDto 회원프로필(int pageUserId, int principalId) { // 해당 페이지의 아이디, 로그인한 아이디
		UserProfileDto dto = new UserProfileDto();

		// SELECT * FROM image WHERE userId = :pageUserId;
		User userEntity = userRepository.findById(pageUserId).orElseThrow(() -> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});

		/*
		 * // 전략이 FetchType = LAZY 인 경우 getImages의 실제 데이터를 가져오는 순간 추가 쿼리 실행되어 이미지정보 가져옴
		 * userEntity.getImages().get(0);
		 */

		dto.setUser(userEntity);
		// dto.setIsPageOwner(pageUserId == principalId); // 1 은 페이지 주인, -1은 주인이 아님 ->
		// boolean 으로 변경
		dto.setPageOwnerState(pageUserId == principalId);
		dto.setImageCount(userEntity.getImages().size());

		int subscribeState = subscribeRepository.subscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);

		System.out.println("principalId : " + principalId + ",  pageUserId : " + pageUserId);
		System.out.println("subscribeState : " + subscribeState + ", subscribeCount : " + subscribeCount);

		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);

		// 컨트롤러에서 처리하거나, view 단에서 ${images.likes.size()} 해도 됨
		// 좋아요 카운트 추가하기, 각각의 이미지마다 좋아요 값이 다르므로 dto에 추가할 순 없음
		userEntity.getImages().forEach((image) -> {
			image.setLikeCount(image.getLikes().size());
		});

		// return userEntity;
		return dto;
	}

	@Transactional
	public User 회원수정(int id, User user) {
		// 1. 영속화
		// User userEntity = userRepository.findById(id).get(); // 1. 무조건 찾았음 get(), 2.
		// 못찾아서 익셉션 발동 orElseThrow(), 3. orElse()
		/*
		 * User userEntity = userRepository.findById(id).orElseThrow(new
		 * Supplier<IllegalArgumentException>() {
		 * 
		 * @Override public IllegalArgumentException get() { return new
		 * IllegalArgumentException("찾을 수 없는 id입니다."); } });
		 */
		User userEntity = userRepository.findById(id).orElseThrow(() -> {
			return new CustomValidationApiException("찾을 수 없는 id입니다.");
		});

		// 2. 영속화된 오브젝트 수정 - 더티 체킹 (업데이트 완료됨)
		userEntity.setName(user.getName());

		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);

		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());

		return userEntity; // 3. 더티 체킹이 일어나서 업데이트가 완료됨
	}
}
