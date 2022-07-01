package com.lux.ex082httprequestdb;


//서버 DB의 board2 테이블의 하나의 레코드(한줄 : row) 의 값들을 가지고 있는 데이터용 클래스
public class Item {
    //서버의 DB 테이블의 컬룸명과 같은 이름의 변수들
    int no;
    String title;
    String msg;
    String date;

    public Item(int no, String title, String msg, String date) {
        this.no = no;
        this.title = title;
        this.msg = msg;
        this.date = date;
    }

    public Item() {
    }
}
