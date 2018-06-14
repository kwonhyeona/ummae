package org.androidtown.voice.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

//그리드뷰를 상속하여 새로운 뷰를 정의한 클래스
//선택한 일자를 표시하는 기능등을 효율적으로 처리하기 위해 정의한 클래스
public class CalendarMonthView extends GridView {

    //일자 선택을 위해 직접 정의한 리스너 객체, 어댑터 객체
    private OnDataSelectionListener selectionListener;
    CalendarMonthAdapter adapter;

    //생성자
    public CalendarMonthView(Context context) {
        super(context);

        init();
    }

    public CalendarMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    //속성 초기화
    private void init() {
        setBackgroundColor(Color.WHITE);
        setVerticalSpacing(1);
        setHorizontalSpacing(1);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        // 칼럼의 갯수 설정
        setNumColumns(7);

        // 그리드뷰의 원래 이벤트 처리 리스너 설정
        setOnItemClickListener(new OnItemClickAdapter());
    }

    //어댑터 설정
    public void setAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);

        this.adapter = (CalendarMonthAdapter) adapter;
    }

    //어댑터 객체 반환
    public BaseAdapter getAdapter() {
        return (BaseAdapter)super.getAdapter();
    }

    //getter, setter 리스너
    public OnDataSelectionListener getOnDataSelectionListener() {
        return selectionListener;
    }
    public void setOnDataSelectionListener(OnDataSelectionListener listener) {
        this.selectionListener = listener;
    }

    public void findViewById() {
    }

    class OnItemClickAdapter implements OnItemClickListener {

        public OnItemClickAdapter() {

        }

        public void onItemClick(AdapterView parent, View v, int position, long id) {

            if (adapter != null) {
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetInvalidated();
            }

            if (selectionListener != null) {
                selectionListener.onDataSelected(parent, v, position, id);
            }


        }

    }

}