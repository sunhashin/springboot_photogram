package com.cos.photogramstart.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
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
public class Image { // N, 1 (1)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // 번호 증가 전략이 데이터베이스를 따라감
	private int id;
	
	private String caption;   // 사진 이미지 설명
	private String postImageUrl;  // 사진을 전송받아 그 사진을 서버의 특정 폴더에 저장 - DB 에 그 저장된 경로를 insert
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name="userId")  // foreignkey 로 저장되므로 이름 지정해줌
	@ManyToOne(fetch = FetchType.EAGER)  // 이미지를 셀렉트하면 조인해서 User 정보를 같이 들고옴
	private User user;  // 1, 1 하나의 유저(1)는 여러 이미지 (N)등록, 하나의 이미지는 한명의 유저(1:1)에게 등록가능
	
	// 이미지 좋아요 
	// 가져오려면 양방향 매핑 필요, 하나의 이미지는 여러개의좋아요 가능, 레이지 로딩, 나는 연관관계의 주인이 아니므로 포린키 필요 없음
	/*연관관계별 default 속성
	@OneToMany : LAZY
	@ManyToOne : EAGER
	@ManyToMany : LAZY
	@OneToOne : EAGER
	*/
	@JsonIgnoreProperties({"image"})  // 무한참조 막기
	@OneToMany(mappedBy = "image")  // 여기 적힌 image 는 Likes 클래스의  변수 이름 (image) , getter 호출하면 lazy 로 가져옴
	private List<Likes> likes;
	 
	// 댓글 (양방향 매핑) - 같이 들고 와야 해서 (N)
	@OrderBy("id DESC")  // 코멘트 리스트 id 역순으로 가져옴
	@JsonIgnoreProperties({"image"})  // 댓글에서 무한 참조 막기 위해서 무시 처리 (Comment 안에 image 를 무시하는 것임 (실제무시하는 곳을 호출하는 직전단에 jsonignore 명시
	@OneToMany(mappedBy = "image")  // 연관관계의 주인은 Comment 클래스 안에 image로 지정 (포린키), 포린키에대한 자바 변수를 적어줌, 레이지 로딩 디폴트(상황에 따라 게터호출)
	private List<Comment> comments;
	
	// 좋아요 상태 db 에 생성되면 안됨, 변수로 사용, javax.persistence import
	@Transient
	private boolean lIkeState;
	
	@Transient
	private int likeCount;

	private LocalDateTime createDate;
	
	@PrePersist // 디비에 insert 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
	/*// @Data 를 사용하면 toString() 이 자동 생성, user 부분이 문제가 되므로 삭제
	 * object 를 콘솔에 출력할 때 문제가 될 수 있어서 User 부분을 출력되지 않게 함
	@Override
	public String toString() {
		return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl 
				+ ", createDate=" + createDate + "]";
	}*/
	
}
