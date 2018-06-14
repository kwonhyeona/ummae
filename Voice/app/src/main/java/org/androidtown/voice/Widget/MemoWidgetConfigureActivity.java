package org.androidtown.voice.Widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.androidtown.voice.List.ListFragment;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoAdapter;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;

/**
 * The configuration screen for the {@link MemoWidget MemoWidget} AppWidget.
 */
public class MemoWidgetConfigureActivity extends Activity {

    RecyclerView recyclerView;

    //ListView에 사용될 모델, 어뎁터 선언
    ArrayList<Memo> memoList;
    MemoAdapter adapter;
    MemoModel model;
    Memo memo;
    //ArrayList<String> memoContents;

    int memoId;

    private static final String PREFS_NAME = "org.androidtown.voice.Widget.MemoWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_title";
    private static final String PREF_PREFIX_KEY_CONTENT = "appwidget_content";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
//    EditText mAppWidgetText;
//    View.OnClickListener mOnClickListener = new View.OnClickListener() {
//        public void onClick(View v) {
//            final Context context = MemoWidgetConfigureActivity.this;
//
//            // When the button is clicked, store the string locally
//            String widgetText = mAppWidgetText.getText().toString();
//            saveTitlePref(context, mAppWidgetId, widgetText);
//
//            // It is the responsibility of the configuration activity to update the app widget
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            MemoWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
//
//            // Make sure we pass back the original appWidgetId
//            Intent resultValue = new Intent();
//            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//            setResult(RESULT_OK, resultValue);
//            finish();
//        }
//    };

    public MemoWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    static void saveContentPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY_CONTENT + appWidgetId, text);
        prefs.apply();
    }
    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static String loadContentPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String contentValue = prefs.getString(PREF_PREFIX_KEY_CONTENT + appWidgetId, null);
        if (contentValue != null) {
            return contentValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_memo_configure);
        recyclerView = (RecyclerView) findViewById(R.id.widget_memo);


        //리스트뷰 갱신하기
        initAdapter();
        initModel();

        //item을 adapter에 추가할 때 size가 달라도 맞춰주는 메소드
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        onClickEvents();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

//        mAppWidgetText.setText(loadTitlePref(MemoWidgetConfigureActivity.this, mAppWidgetId));
    }

    private void onClickEvents() {
        //각 view 클릭리스너 설정
        recyclerView.addOnItemTouchListener(
                new ListFragment.RecyclerItemClickListener(getApplicationContext(), recyclerView, new ListFragment.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //클릭한 리스트뷰의 아이템을 MemoAdapter에서 정의했던 getItem 메소드를 이용하여 가져오기
                        //모델에서 id를 가져와서 인텐트로 MemoDetailActivity로 보내주기
                        final Context context = MemoWidgetConfigureActivity.this;

                        // When the button is clicked, store the string locally
//                        String widgetText = mAppWidgetText.getText().toString();
//                        saveTitlePref(context, mAppWidgetId, widgetText);
                        Memo memo = (Memo) adapter.getItem(position);

                        String memoName = memo.getMemoName();
                        String memoContent = memo.getMemoContents();

                        saveTitlePref(context, mAppWidgetId,memoName);
                        saveContentPref(context,mAppWidgetId,memoContent);

                        // It is the responsibility of the configuration activity to update the app widget
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                        MemoWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                        // Make sure we pass back the original appWidgetId
                        Intent resultValue = new Intent();
                        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                        setResult(RESULT_OK, resultValue);
                        finish();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    @Override
    public void onResume() {
        //onResume 은 Activity가 화면에 나타나기 직전 실행되는 메소드
        super.onResume();
        memo = model.getMemoById(memoId);

        //리스트뷰 갱신하기
        setModel();
    }

    private void initAdapter() {
        //adapter 초기화, adapter에 모델 설정, recyclerview에 adapter 달아주기
        adapter = new MemoAdapter(getApplicationContext());
        adapter.setMemo(memoList);
        recyclerView.setAdapter(adapter);
    }

    private void initModel() {
        //모델 초기화(Realm 모델도 초기화)
        memoList = new ArrayList<>();
        model = new MemoModel();
    }

    private void setModel() {
        //Realm에서 모델 가져와 어뎁터 설정
        memoList = model.getAllMemos();
        adapter.setMemo(model.getAllMemos());
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

