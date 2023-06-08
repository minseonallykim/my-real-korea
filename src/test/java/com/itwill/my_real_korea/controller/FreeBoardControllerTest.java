package com.itwill.my_real_korea.controller;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.freeboard.FreeBoard;
import com.itwill.my_real_korea.dto.freeboard.FreeBoardComment;
import com.itwill.my_real_korea.service.freeboard.FreeBoardCommentService;
import com.itwill.my_real_korea.service.freeboard.FreeBoardService;
import com.itwill.my_real_korea.util.PageMaker;
import com.itwill.my_real_korea.util.PageMakerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class FreeBoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FreeBoardService freeBoardService;
    
    @MockBean
    FreeBoardCommentService freeBoardCommentService;

    //@Test
    void freeBoard_list() throws Exception{
        PageMakerDto<FreeBoard> freeBoardList = new PageMakerDto<>();

        List<FreeBoard> freeBoards = new ArrayList<>();
        freeBoards.add(new FreeBoard(1, "title", "content", null, 0, new City(1,"서울",1,1), "user2",0));
        freeBoards.add(new FreeBoard(2, "title", "content", null, 0, new City(1,"서울",1,1), "user1",0));
        freeBoards.add(new FreeBoard(3, "title", "content", null, 0, new City(1,"서울",1,1), "user3",0));
        freeBoardList.setItemList(freeBoards);
        freeBoardList.setPageMaker(new PageMaker(1, 10));
        freeBoardList.setTotRecordCount(3);
        given(freeBoardService.selectAll(1)).willReturn(freeBoardList);

        mockMvc.perform(get("/freeBoard-list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("freeBoardList"))
                .andExpect(model().attributeExists("pageNo"))
                .andExpect(view().name("freeboard-list"))
                .andDo(print());

        verify(freeBoardService).selectAll(1);
    }

    @Test
    void freeBoard_detail() throws Exception{
        FreeBoard freeBoard = new FreeBoard(1,"title","content",null,12,new City(1,"서울",1,1),"user2",0);
        List<FreeBoardComment> freeBoardCommentList = new ArrayList<>();
        freeBoardCommentList.add(new FreeBoardComment(1, null, null, 1, null));
        freeBoardCommentList.add(new FreeBoardComment(2, null, null, 1, null));
        freeBoardCommentList.add(new FreeBoardComment(3, null, null, 1, null));
        
        given(freeBoardService.selectByNo(1)).willReturn(freeBoard);
        given(freeBoardCommentService.selectByfBoNo(1)).willReturn(freeBoardCommentList);

        mockMvc.perform(get("/freeboard-detail").param("fBoNo", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("freeBoard"))
                .andExpect(model().attributeExists("freeBoardCommentList"))
                .andExpect(model().attributeExists("fBoNo"))
                .andExpect(view().name("freeboard-detail"))
                .andDo(print());

        verify(freeBoardService).selectByNo(1);
        verify(freeBoardCommentService).selectByfBoNo(1);
    }

    //@Test
    void freeBoardWriteForm() throws Exception {
        mockMvc.perform(get("/freeboard-write-form"))
                .andExpect(status().isOk())
                .andExpect(view().name("freeboard-write-form"))
                .andDo(print());
    }

    //@Test
    void freeBoardModifyForm() throws Exception {
        FreeBoard freeBoard = new FreeBoard(1,"title","content",null,12,new City(1,"서울",1,1),"user2",0);
        given(freeBoardService.selectByNo(1)).willReturn(freeBoard);

        mockMvc.perform(get("/freeboard-modify-form").param("fBoNo","1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("freeBoard"))
                .andExpect(view().name("freeboard-modify-form"))
                .andDo(print());
    }
}
