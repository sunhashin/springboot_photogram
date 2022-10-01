package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진() {
		return imageRepository.mPopular();
	}
	
	@Transactional(readOnly = true)   // 영속성 컨텍스트 변경 감지를 해서, 더티체킹, flush(반영) x, 하지만 세션을 컨트롤러 단까지 가져오는 것은 중요한 부분
	public Page<Image> 이미지스토리(int principalId, Pageable pageable) {
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		// 2(cos) 로그인
		//images 에 좋아요 상태 담기
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like)->{
				if (like.getUser().getId() == principalId) {  // 해당 이미지에 좋아요 한 사람들을 찾아서 현재 로그인한 사람이 좋아요 한 것인지 비교
					image.setLIkeState(true);
				}
			});
		});
		return images;
	}
	
	@Value("${file.path}")  // application.yml 파일에 사용자 정의한 경로 프로퍼티 정보 가져옴
	private String uploadFolder;
	
	@Transactional  // 서비스단에서 디비 변경할때에는 @Transactional 걸어줘야 함
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID(); // uuid
		
		String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename();  // 1.jpg (업로드 하려는 원래 파일명) - uuid 더해서 안겹치게 만듬 
		System.out.println("이미지 파일 이름 : " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		
		// 통신이 일어나거나 I/O 일어날 때 예외가 발생할 수 있음 (런타임시에만 체크할 수 있음
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// image DB 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser()); // 563a95c5-96f9-4f7e-862f-117e9197a83c_tropical_paradise_4-wallpaper-1920x1080.jpg
//		Image imageEntity = imageRepository.save(image);
		imageRepository.save(image);
		
		// 여기 찍으면 이미지 등록시 에러남, 일단 주석처리후 확인
		// imageEntity.toString() 과 동일, 롬복 @Data 에서 자동으로 생성하는 toString() 내에서 user 호출하는 부분때문에 무한 참조가 발생
		// 해결위해 toString() overridng 해서 user 빼고 찍으면 정상 동작함
		// imageEntity.toString() 호출시 User.java 의 private List<Image> images;  부분에서 Image.java 의 private User user; 가 서로 상호 반복하면서 호출함
//		System.out.println(imageEntity);  
	}
}
