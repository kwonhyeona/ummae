package org.androidtown.voice.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.androidtown.voice.Dialog.CalendarDetailMemoDialog;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoAdapter;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;


public class CalendarFragment extends Fragment {

    RecyclerView memoCalendarRecyclerView;

    ArrayList<Memo> memoList;
    MemoAdapter memoAdapter;
    MemoModel memoModel;

    boolean isreturn = false;

    //월별 캘린더 뷰 객체, 어댑터
    CalendarMonthView monthView;
    CalendarMonthAdapter monthViewAdapter;

    CalendarDetailMemoDialog calendarDetailMemoDialog;


    //월을 표시하는 텍스트뷰, 메모 보여주는 텍스트뷰
    TextView monthText;
    TextView dayView;
    Button monthPrevious, monthNext;
    public static String name = null;

    //현재 연도, 월
    int curYear, curMonth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calendar, container, false);


        // findViewById
        monthView = (CalendarMonthView) view.findViewById(R.id.monthView);
        dayView = (TextView) view.findViewById(R.id.dayView);
        monthText = (TextView) view.findViewById(R.id.monthText);
        monthPrevious = (Button) view.findViewById(R.id.monthPrevious);
        monthNext = (Button) view.findViewById(R.id.monthNext);

        memoCalendarRecyclerView = (RecyclerView) view.findViewById(R.id.calenderRecyclerView);
        memoCalendarRecyclerView.setHasFixedSize(true);


        //adapter설정
        monthViewAdapter = new CalendarMonthAdapter(getActivity());
        monthView.setAdapter( monthViewAdapter);

        //월 표시
        setMonthText();


        //월 이동 이벤트 처리
        moveMonthEvent();


        //setIcon();


        // 리스너 설정
        monthView.setOnDataSelectionListener(new OnDataSelectionListener() {


            public void onDataSelected(AdapterView parent, View v, int position, long id) {
                // 현재 선택한 일자 정보 표시
                //날짜를 누르면 해당하는 년이랑 월 표시
                MonthItem curItem = (MonthItem) monthViewAdapter.getItem(position);
                long day = curItem.getDay();

                initModel();
                String curCalendarDate = (String.valueOf(curYear) +"년"+ String.valueOf(curMonth + 1)+"월"+ day+ "일");

                dayView.setText(String.valueOf(curYear) + " ." + String.valueOf(curMonth + 1) + " ." + day );

                if(day==0){
                    dayView.setText("날짜");
                }

                memoList = memoModel.getDateOfMemos(curCalendarDate);
                memoAdapter.setMemo(memoList);

                memoCalendarRecyclerView.setAdapter(memoAdapter);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                memoCalendarRecyclerView.setLayoutManager(layoutManager);

                Log.d("MainActivity", "Selected : " + day);


            }
        });
        memoCalendarRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), memoCalendarRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //클릭한 리스트뷰의 아이템을 MemoAdapter에서 정의했던 getItem 메소드를 이용하여 가져오기
                        //모델에서 id를 가져와서 인텐트로 MemoDetailActivity로 보내주기
                        Memo memo = (Memo) memoAdapter.getItem(position);

                        calendarDetailMemoDialog = new CalendarDetailMemoDialog(getContext(),  memo.getMemoday(), memo.getMemoTime(), memo.getMemoContents());
                        calendarDetailMemoDialog.setContentView(R.layout.dialog_calendar_detail_memo);
                        //calendarDetailMemoDialog.getWindow().setLayout(850, 850);
                        calendarDetailMemoDialog.getWindow();

                        calendarDetailMemoDialog.show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


        return view;
    }


    private void initModel() {
        memoList = new ArrayList<>();
        memoAdapter = new MemoAdapter(getContext());
        memoModel = new MemoModel();
    }


    private void setMonthText() {
        curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();

        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }



    private void moveMonthEvent() {
        // 이전 월로 넘어가는 이벤트 처리
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });
        // 다음 월로 넘어가는 이벤트 처리
        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });


    }



    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });

        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}
