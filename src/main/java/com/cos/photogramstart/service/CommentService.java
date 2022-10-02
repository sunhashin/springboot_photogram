package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Comment 댓글쓰기(String content, int imageId, int userId) { 
		
		// Tip : 객체를 만들 때 id 값만 담아서 insert 할 수 있다, 아니면 findById 해서 찾아서 해야함
		// 대신 return 시에 image 객체는 id 값만 가지고 있는 빈 객체를 리턴받는다.
		// user 객체는 작성자 정보가 있어야 해서 검색해서 가져와서 담아줘야 함
		Image image =  new Image();
		image.setId(imageId);  // image 객체에 imageId 값만 넣고 그 값만 반환해줌
		
		User userEntity = userRepository.findById(userId).orElseThrow(()-> { // Api 요청이므로
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);
		
		return commentRepository.save(comment);
	}
	
	@Transactional
	public void 댓글삭제(int id) {
		try {
			commentRepository.deleteById(id);
		}catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
		
	}
}
