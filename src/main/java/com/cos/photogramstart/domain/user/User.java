package com.cos.photogramstart.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA - Java Persistence API (자바로 데이터를 영구적으로 저장(DB)할 수 있는 API를 제공)

@Builder   // Builder 패턴 사용 위해서 추가
@AllArgsConstructor  // 전체 생성자 생성
@NoArgsConstructor  // 빈 생성자 생성
@Data  // getter, setter 만들어줌
@Entity // 디비에 테이블 생성
public class User {  // 1
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // 번호 증가 전략이 데이터베이스를 따라감
	private int id;
	
	@Column(length = 20, unique=true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	
	private String website;  // 웹사이트
	private String bio;  // 성별
	
	@Column(nullable = false)
	private String email;
	
	private String phone;
	private String gender;
	
	private String profileImageUrl;  // 사진
	private String role;  // 권한
	
	 // mappedBy : 나는 연관관계의 주인이 아니다. 그러므로 테이블에 컬럼을 만들지 말아 (주인은 image 테이블의 user 이다)
	// User 를 셀렉트할때 해당 User 테이블의 id 로 등록된 이미지들을 다 가져와조
	// FetchType 이 Lazy 인 경우 해당 user id 로 등록된 이미지들을 가져오지마, 대신 getImages() 함수의 이미지들이 호출될 때 가져와
	// FetchType 이 EAGER 인 경우 해당 user id 로 등록된 이미지들을 전부 Join 해서 가져와
	// @OneToMany(mappedBy="user", fetch = FetchType.EAGER)   // 1명의 유저는 여러개의 이미지를 만들 수 있다. (1 : N)
	// MessageConverter 가 getter 호출시 json 으로 파싱될 때 내부의 user 무시해서, getUser() 호출되는것 막기 무한 순환 참조 되는 것을 막을때
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)   // 1명의 유저는 여러개의 이미지를 만들 수 있다. (1 : N)
	@JsonIgnoreProperties({"user"})
	private List<Image> images;  // 양방향 매핑 : 다대다 관계를 만들기 위해 리스트 타입 추가, 실제에 유저테이블에 있는 값이 아님, 컬렉션은 디비에 저장하고 있지 말라고 알려줘야함
	
	private LocalDateTime createDate;
	
	@PrePersist // 디비에 insert 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
	//  around 에서 객체 파라미터 보기 위해 출력할때 무한참조 걸려서 회피 위해서 user 객체 찍는 부분 빼줌
	// .domain.user.User.images, could not initialize proxy - no Session
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate="
				+ createDate + "]";
	}
	
}
