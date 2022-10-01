package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import lombok.Data;

// @NotNull : null 값 체크
// @NotEmpty : 빈값이거나 null 체크
// @NotBlank : 빈값, null, 빈공백(스페이스) 체크

@Data
public class CommentDto {
	@NotBlank  // 빈값이거나 null 과 빈공백 체크
	private String content;
	@NotNull   //null 체크
	private Integer imageId; 
	
	// toEntity 가 필요 없음
}
