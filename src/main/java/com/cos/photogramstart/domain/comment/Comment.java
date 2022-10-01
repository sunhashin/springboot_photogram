package com.cos.photogramstart.domain.comment;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

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
public class Comment { // 1, N (N, 1)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // 번호 증가 전략이 데이터베이스를 따라감
	private int id;
	
	@Column(length = 100, nullable = false)
	private String content;
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")  // 조인 컬럼(포린키), 데이터베이스에 컬럼 명으로 생성됨
	@ManyToOne(fetch = FetchType.EAGER)  // 셀렉트 할때 딸려오는게 하나면 EAGER 로 가져옴
	private User user;  // 1, 1
	
	@JoinColumn(name = "imageId") // 조인 컬럼(포린키), 데이터베이스에 컬럼 명으로 생성됨
	@ManyToOne(fetch = FetchType.EAGER)
	private Image image; // (1, 1)
	
	private LocalDateTime createDate;
	
	@PrePersist // 디비에 insert 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
