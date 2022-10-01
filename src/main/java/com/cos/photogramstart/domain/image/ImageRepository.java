package com.cos.photogramstart.domain.image;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Integer>{
 

	/*@Query(value="SELECT * FROM image \r\n" + 
			"	WHERE userID IN (SELECT toUserId FROM subscribe WHERE fromUserId = :principalId) ORDER BY id DESC", nativeQuery = true)*/
	@Query(value="SELECT * FROM image \r\n" + 
			"	WHERE userID IN (SELECT toUserId FROM subscribe WHERE fromUserId = :principalId)", nativeQuery = true)
	Page<Image> mStory(int principalId, Pageable pageable);
	
	@Query(value="SELECT i.* FROM image i INNER JOIN (SELECT ImageId, count(ImageId) likeCount FROM likes GROUP BY ImageId) c "
			+ "ON i.id = c.imageId ORDER BY likeCount DESC", nativeQuery = true)
	List<Image> mPopular();
	
	
}
