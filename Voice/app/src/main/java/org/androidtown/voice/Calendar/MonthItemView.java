package org.androidtown.voice.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

//몇일인지 표시해주는 텍스트뷰
public class MonthItemView extends TextView {

    private MonthItem item;

    public MonthItemView(Context context) {
        super(context);

        initBackgroundColor();
    }

    public MonthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initBackgroundColor();
    }

    private void initBackgroundColor() {
        setBackgroundColor(Color.WHITE);
    }

    //getter, setter
    public MonthItem getItem() {
        return item;
    }

    public void setItem(MonthItem item) {
        this.item = item;

        int day = item.getDay();
        if (day != 0) {
            setText(String.valueOf(day));
        } else {
            setText("");
        }
    }
}