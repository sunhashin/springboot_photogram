package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>{
	// Many to Many 관계 있는 테이블 처리시 object 를 반환하기 어려운 int 타입등의 경우 네이티브 쿼리로 직접 작성, nativeQuery = true 필수임
	
	@Modifying  // INSERT, DELETE, UPDATE 를 네이티브 쿼리로 작성하려면 해당 어노테이션이 필요
	@Query(value = "INSERT INTO subscribe (fromUserId, toUserId, createDate) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
	void mSubscribe(int fromUserId, int toUserId);  // 성공 1, 실패 -1, (변경된 행의 개수만큼 수 리턴됨) 10개 열을 입력하면 10 리턴, 0 은 변경된 행이 없다는 뜻
	// int mSubscribe(int fromUserId, int toUserId);  // 성공 1, 실패 -1, (변경된 행의 개수만큼 수 리턴됨) 10개 열을 입력하면 10 리턴, 0 은 변경된 행이 없다는 뜻
	
	@Modifying  // INSERT, DELETE, UPDATE 를 네이티브 쿼리로 작성하려면 해당 어노테이션이 필요
	@Query(value = "DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserId =  :toUserId", nativeQuery = true)
	void mUnSubscribe(int fromUserId, int toUserId);  // 성공 1, 실패 -1 (단건인 경우도 마찬가지)
	
	 // select 만 하는것은 @Modifying 필요 없음
	// 구독된 상태 : 1
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true) 
	int subscribeState(int principalId, int pageUserId);
	
	// 해당페이지의 주인이 구독한 수
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :pageUserId", nativeQuery = true)
	int mSubscribeCount(int pageUserId);
	
	
}
