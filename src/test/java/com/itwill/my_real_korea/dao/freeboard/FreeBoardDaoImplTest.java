package com.itwill.my_real_korea.dao.freeboard;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.freeboard.FreeBoard;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class FreeBoardDaoImplTest {
    @Autowired
    private FreeBoardDao freeBoardDao;
    @Test
    void insertBoard() throws Exception {
        FreeBoard freeBoard = new FreeBoard(2,"date test","content",null,12,"user2");
        freeBoard.setCity(new City(1,"서울",1,1));
        System.out.println(freeBoard);
        int insertBoard = freeBoardDao.insertBoard(freeBoard);
        assertThat(insertBoard).isEqualTo(1);
    }

    @Test
    void selectByNo() throws Exception {
        FreeBoard freeBoard = freeBoardDao.selectByNo(3);
        assertThat(freeBoard).isNotNull();
        System.out.println(freeBoard);
    }

    @Test
    void selectByUserId() throws Exception {
        List<FreeBoard> freeBoards = freeBoardDao.selectByUserId("myrealkorea2");
        assertThat(freeBoards).isNotNull();
        System.out.println(freeBoards);
    }

    @Test
    void selectAllOrderByFBoNoDesc() throws Exception {
        List<FreeBoard> freeBoardList = freeBoardDao.selectAllOrderByFBoNoDesc(1, 8);
        System.out.println(freeBoardList);
    }

    @Test
    void selectAllOrderByFBoNoAsc() throws Exception {
        List<FreeBoard> freeBoardList = freeBoardDao.selectAllOrderByFBoNoAsc(1, 5);
        System.out.println(freeBoardList);
    }

    @Test
    void selectAllOrderByReadCountDesc() {
        List<FreeBoard> freeBoardList = freeBoardDao.selectAllOrderByReadCountDesc(1,5);
        System.out.println(freeBoardList);
    }

    @Test
    void updateBoard() throws Exception {
        FreeBoard updateFreeBoard = FreeBoard.builder()
                .fBoTitle("title")
                .fBoContent("content")
                .fBoNo(4)
                .build();
        City city = new City(1, "서울", 1, 1);
        updateFreeBoard.setCity(city);
        int updateBoard = freeBoardDao.updateBoard(updateFreeBoard);
        assertThat(updateBoard).isEqualTo(1);
    }
    @Disabled
    @Test
    void deleteBoard() throws Exception {
        freeBoardDao.deleteBoard(26);
    }

    @Test
    void increaseReadCount() throws Exception {
        int i = freeBoardDao.increaseReadCount(1);
        assertThat(i).isEqualTo(1);
    }

    @Test
    void selectFreeBoardCount() throws Exception {
        int selectFreeBoardCount = freeBoardDao.selectFreeBoardCount();
        System.out.println(selectFreeBoardCount);
    }

    @Test
    void selectSearchCount() throws Exception {
        int selectSearchCount = freeBoardDao.selectSearchCount("맛집");
        System.out.println(selectSearchCount);
    }

    @Test
    void selectSearchFreeBoardList() throws Exception {
        List<FreeBoard> freeBoardList = freeBoardDao.selectSearchFreeBoardList(1,4,"맛집");
        System.out.println(freeBoardList);
    }
}