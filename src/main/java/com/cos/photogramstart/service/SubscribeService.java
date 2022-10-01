package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;  // qlrm 은 스프링부트에서 제공해 주는 라이브러리가 아님, pom.xml 에 따로 등록해 둬야 함
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
	
	private final SubscribeRepository SubscribeRepository;
	
	// 모든 Jpa Repository 들은 EntityManager를 구현해서 만들어져 있는 구현체
	// 복잡한 쿼리를 직접 수행해서 쿼리해야하는 경우 em 을 상속받아 service 에서 구현하면 됨
	private final EntityManager em;
	
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalId, int pageUserId) {
		
		StringBuffer sb = new StringBuffer();
		
		// 쿼리는 맨뒤에 한칸 띄워줘야 함
		// 쿼리 준비
		sb.append("SELECT u.id, u.username, u.profileImageUrl,  ");
		sb.append(" if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) subscribeState, ");
		sb.append(" if((? = u.id), 1, 0) equalUserState ");
		sb.append("  FROM user u INNER JOIN subscribe s ");
		sb.append(" ON u.id = s.toUserId ");
		sb.append(" WHERE s.fromUserId = ?");  // 세미콜론 첨부하면 안됨
		
		// 1. 물음표 principalId
		// 2. 물음표 principalId
		// 3. 마지막 물음표는 pageUserId 이어야 함
		// 쿼리 완성
		 
		Query query = em.createNativeQuery(sb.toString())
					.setParameter(1, principalId)
					.setParameter(2, principalId)
					.setParameter(3, pageUserId);
		
		// 쿼리 실행 (qlrm 라이브러리 필요 = DTO 에 DB 결과를 매핑하기 위해서)
		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
		
		return subscribeDtos;
	}
	
	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		
		// save 하는 경우는 객체가 필요한데 int 값이 아닌 경우는 object 못만들어서 native query 사용하는 것이 좋음
		// SubscribeRepository.save(null); // findById 로 가져와서 비교하고 해야하므로 복잡, 네이티브쿼리 사용하기 위해 SubscribeRepository에 작성함
		// int result = SubscribeRepository.mSubscribe(fromUserId, toUserId);
		// return result;
		try {
			SubscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독했습니다.");
		}
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		SubscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
