package com.itwill.my_real_korea.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * 페이징 처리 필요한 곳에서 공용으로 사용하는 PageMakerDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageMakerDto<E> {
	
	private List<E> itemList;
	private PageMaker pageMaker;
	private int totRecordCount;

}
