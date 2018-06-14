package org.androidtown.voice.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarMonthAdapter extends BaseAdapter {


	ArrayList<Memo> memoList;
	MemoModel memoModel;

	public static final String TAG = "CalendarMonthAdapter";
	public static int oddColor = Color.rgb(225, 225, 225);
	public static int headColor = Color.rgb(12, 32, 158);

	private int selectedPosition = -1;
	private MonthItem[] items;
	private int countColumn = 7;
	//private int countColumn1= 6;

	int mStartDay;
	int startDay;
	int curYear;
	int curMonth;
	int firstDay;
	int lastDay;
	boolean recreateItems = false;

	Context mContext;
	Calendar mCalendar;
	MonthItemView itemView;

	CalendarMonthAdapter monthViewAdapter;

	//생성자
	public CalendarMonthAdapter(Context context) {
		super();
		mContext = context;

		init();
	}


	private void initModel2() {
		memoList = new ArrayList<>();
		memoModel = new MemoModel();
	}

	//getter, setter 함수
	public int getCurYear() {
		return curYear;
	}
	public void setCurYear(int curYear) {
		this.curYear = curYear;
	}

	public int getCurMonth() {
		return curMonth;
	}
	public void setCurMonth(int curMonth) {
		this.curMonth = curMonth;
	}


	public int getSelectedPosition() {
		return selectedPosition;
	}
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}



	private void init() {

		items = new MonthItem[7*6];

		mCalendar = Calendar.getInstance();
		recalculate();
		resetDayNumbers();
	}

	public void recalculate() {

		// 1일 표시
		mCalendar.set(Calendar.DAY_OF_MONTH, 1);

		// get week day
		int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		mStartDay = mCalendar.getFirstDayOfWeek();
		firstDay = getFirstDay(dayOfWeek);
		startDay = getFirstDayOfWeek();

		Log.d(TAG, "firstDay : " + firstDay + ", startDay : " + startDay);

		curYear = mCalendar.get(Calendar.YEAR);
		curMonth = mCalendar.get(Calendar.MONTH);
		lastDay = getMonthLastDay(curYear, curMonth);

		Log.d(TAG, "curYear : " + curYear + ", curMonth : " + curMonth + ", lastDay : " + lastDay);
	}


	public void setPreviousMonth() {
		mCalendar.add(Calendar.MONTH, -1);
		recalculate();

		resetDayNumbers();
		selectedPosition = -1;
	}

	public void setNextMonth() {
		mCalendar.add(Calendar.MONTH, 1);
		recalculate();

		resetDayNumbers();
		selectedPosition = -1;
	}
	public void resetDayNumbers() {
		int dayNumber;
		for (int i = 0; i < 42; i++) {
			// calculate day number
			dayNumber = (i + 1) - firstDay;
			if (dayNumber < 1 || dayNumber > lastDay) {
				dayNumber = 0;
			}
			// save as a data item
			items[i] = new MonthItem(dayNumber);
		}
		for(int i = 0; i < 35; i++){
			// calculate day number
			dayNumber = (i + 1) - firstDay;
			if (dayNumber < 1 || dayNumber > lastDay) {
				dayNumber = 0;
			}
			// save as a data item
			if(items[i] != null)
				items[i] = new MonthItem(dayNumber);
			else
				getCellHeight();


		}
	}



	private int getFirstDay(int dayOfWeek) {
		int result = 0;

		if (dayOfWeek == Calendar.SUNDAY) {
			result = 0;
		} else if (dayOfWeek == Calendar.MONDAY) {
			result = 1;
		} else if (dayOfWeek == Calendar.TUESDAY) {
			result = 2;
		} else if (dayOfWeek == Calendar.WEDNESDAY) {
			result = 3;
		} else if (dayOfWeek == Calendar.THURSDAY) {
			result = 4;
		} else if (dayOfWeek == Calendar.FRIDAY) {
			result = 5;
		} else if (dayOfWeek == Calendar.SATURDAY) {
			result = 6;
		}

		return result;
	}


	public int getCount() {
		return items.length;
	}

	public Object getItem(int position) {
		return items[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView(" + position + ") called.");

		if (convertView == null) {
			itemView = new MonthItemView(mContext);
		} else {
			itemView = (MonthItemView) convertView;
		}

		// create a params
		GridView.LayoutParams params = new GridView.LayoutParams(
				GridView.LayoutParams.MATCH_PARENT, getCellHeight());


		// calculate row and column

		int rowIndex = position / countColumn;
		int columnIndex = position % countColumn;

		Log.d(TAG, "Index : " + rowIndex + ", " + columnIndex);

		// set item data and properties
		itemView.setItem(items[position]);
		itemView.setLayoutParams(params);
		itemView.setPadding(2, 2, 2, 2);
		itemView.setGravity(Gravity.LEFT);

		if (columnIndex == 0) {
			itemView.setTextColor(Color.rgb(241,95,95));
		}else if(columnIndex == 6){
			itemView.setTextColor(Color.rgb(0,84,255));
		}
		else {
			itemView.setTextColor(Color.BLACK);
		}
		MonthItem curItem = items[position];
		int day = curItem.getDay();

		//선택된 날짜 색 바꾸기
		if (position == getSelectedPosition()) {
			itemView.setBackgroundColor(Color.rgb(218,217,255));
		} else {
			itemView.setBackgroundColor(Color.WHITE);
		}


		//해당하는 메모리스트 있으면 캘린더에 아이콘표시

		initModel2();

		String curCalendarDate = (String.valueOf(curYear) +"년"+ String.valueOf(curMonth + 1)+"월"+ day+ "일");
		memoList = memoModel.getDateOfMemos(curCalendarDate);
		if(!memoList.isEmpty()){
			itemView.setBackgroundResource(R.drawable.ummae_calendar);
		}
		return itemView;
	}

	private int getCellHeight() {
		int height = (mContext.getResources().getDisplayMetrics().heightPixels)/3;
		int cellHeight = height/5;
		if(getCount() > 35)
			cellHeight = height/6;

		return cellHeight;
	}

	//한 주의 첫번째 날짜 구하는 메소드
	public static int getFirstDayOfWeek() {
		int startDay = Calendar.getInstance().getFirstDayOfWeek();
		if (startDay == Calendar.SATURDAY) {
			return Time.SATURDAY;
		} else if (startDay == Calendar.MONDAY) {
			return Time.MONDAY;
		} else {
			return Time.SUNDAY;
		}
	}

	//각 달의 일수 구하는 메소드
	private int getMonthLastDay(int year, int month) {
		switch (month) {
			case 0:
			case 2:
			case 4:
			case 6:
			case 7:
			case 9:
			case 11:
				return (31);

			case 3:
			case 5:
			case 8:
			case 10:
				return (30);

			default:
				if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
					return (29);   // 2월 윤년계산
				} else {
					return (28);
				}
		}
	}


}