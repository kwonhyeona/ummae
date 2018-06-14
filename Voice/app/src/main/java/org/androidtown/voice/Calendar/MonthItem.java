package org.androidtown.voice.Calendar;

//일자 정보를 담기 위한 클래스
public class MonthItem {

    private int dayValue;

    //생성자
    public MonthItem() {
    }

    public MonthItem(int day) {
        dayValue = day;
    }

    //getter, setter
    public int getDay() {
        return dayValue;
    }

    public void setDay(int day) {
        this.dayValue = day;
    }
}