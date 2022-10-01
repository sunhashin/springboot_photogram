package com.cos.photogramstart.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder   // Builder 패턴 사용 위해서 추가
@AllArgsConstructor  // 전체 생성자 생성
@NoArgsConstructor  // 빈 생성자 생성
@Data  // getter, setter 만들어줌
@Entity // 디비에 테이블 생성
@Table(
		uniqueConstraints = {
				@UniqueConstraint(
						name="likes_uk",
						columnNames = {"imageId", "userId"}  // 실제 데이터베이스의 컬럼명 (복합 유니크 키로 만듬)
				)
		}
)
public class Likes {  // N, 1
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // 번호 증가 전략이 데이터베이스를 따라감
	private int id;
	
	// 무한 참조 됨 (호출하는 image 클래스 앞단에 @JsonIgnoreProperties 지정해서 해소
	// 쌀이라는 유저가 1번 이미지글에 2번 좋아요 할 수 없어서 중복불가로 묶어서 유니크 제약 조건 걸음
	@JoinColumn(name = "imageId")
	@ManyToOne  // 기본 패치 전략 EAGER 전략, ManyToOne 는 Lazy 전략
	private Image image; // 1, 1 (하나의 이미지는 여러번의 좋아요가 가능, 그 반대는 불가능)
	
	// 오류가 날 것이라 대응할 것 : like 안에 user 안에 image 정보는 필요 없어서 제외, 아니면 무한 참조 발생
	@JsonIgnoreProperties("images")
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;  // 1, l (한명의 유저는 여러번의 좋아요가 가능, 그 반대는 불가능)
	
	// native query 를 사용하여 insert 하면 코드 적용안됨, 쿼리에서 now() 넣어줘야함
	private LocalDateTime createDate;
	
	/*
	@PrePersist // 디비에 insert 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	*/
	
}
