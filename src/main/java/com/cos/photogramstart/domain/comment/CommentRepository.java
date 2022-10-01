package com.cos.photogramstart.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

	/*@Modifying
	@Query(value="INSERT INTO comment(content, imageId, userId, createDate) VALUES(:content, :imageId, :userId, now())", nativeQuery = true)
	// Comment mSave(String content, int imageId, int userId);  // 내가 사용자 정의로 만든 쿼리는 객체 타입 리턴 불가, int 형만 가능, primary key 알 수 없음
	 * 네이티브 쿼리 사용 의미 없어서 jpa repository 가 들고 있는 save 사용해야 함
	int mSave(String content, int imageId, int userId);*/
}