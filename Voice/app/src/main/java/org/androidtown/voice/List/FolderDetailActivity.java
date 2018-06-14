package org.androidtown.voice.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.voice.Dialog.MemoClickDialog;
import org.androidtown.voice.FolderRealm.Folder;
import org.androidtown.voice.FolderRealm.FolderModel;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoAdapter;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;

public class FolderDetailActivity extends AppCompatActivity {

    ImageView back;
    TextView back2;
    //Dialog 선언
    MemoClickDialog memoClickDialog;

    //RecyclerView 선언
    RecyclerView fDetailRecyclerView;

    //RecyclerView에 사용될 모델, 어뎁터 선언
    ArrayList<Memo> memoList;
    MemoAdapter memoAdapter;
    MemoModel memoModel;
    Memo memo;
    Folder folder;

    // 다중선택
    boolean isChecked;

    ViewPager pager;

    // 인텐트로 받아온 폴더 id를 저장할 변수
    int folderId;

    @Override
    protected void onResume() {
        super.onResume();

        memoModel.getMemosInSameFolder(folderId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_detail);

        fDetailRecyclerView = (RecyclerView) findViewById(R.id.fDetailRecyclerView);
        pager = (ViewPager)findViewById(R.id.mViewPager);
        fDetailRecyclerView.setHasFixedSize(true);
        back = (ImageView)findViewById(R.id.back_detail_folder);
        back2 = (TextView)findViewById(R.id.title_detail_folder);

        initDatas();
        initList();

        FolderModel folderModel = new FolderModel();
        folder = folderModel.getFolderById(folderId);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back2.setText(folder.getFoldername());
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fDetailRecyclerView.setLayoutManager(layoutManager);

        //Listener 설정
        fDetailRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), fDetailRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Memo memo = (Memo) memoAdapter.getItem(position);


                        Intent intent = new Intent(getApplicationContext(), MemoDetailActivity.class);
                        intent.putExtra("memoId", memo.getMemoId());

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //롱클릭시 메모다이얼로그 보여주기
                        Memo memo = (Memo) memoAdapter.getItem(position);

                        memoClickDialog = new MemoClickDialog(view.getContext(), memo.getMemoId(), memoAdapter, folderId);
                        memoClickDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        memoClickDialog.show();
                    }
                }

                )
        );
    }

    private void initDatas() {
        memoModel = new MemoModel();

        //만약 "id"라는 키로 넘어온 값이 없으면 -1로 초기화
        Intent intent = getIntent();
        folderId = intent.getIntExtra("folderId", -1);

        //int형의 데이터를 인텐트로 받아올 때 에러처리
        if (folderId < 0) {
            finish();
        } else {
            //인텐트로 넘겨받은 id를 통해 Realm에서 해당 id의 Folder 데이터 받아오기
            memoModel.getMemosInSameFolder(folderId);
            Log.i("Tag", "initDatas에서 폴더 이름: " );
        }
    }

    private void initList() {
        initAdapter();
        initModel();
        setModel();

    }

    private void initAdapter() {
        //adapter 초기화, adapter에 모델 설정, recyclerview에 adapter 달아주기
        memoAdapter = new MemoAdapter(getApplicationContext(), isChecked);
        memoAdapter.setMemo(memoList);
        fDetailRecyclerView.setAdapter(memoAdapter);
    }

    private void initModel() {
        //모델 초기화(Realm 모델도 초기화)
        memoList = new ArrayList<>();
        memoModel = new MemoModel();
    }

    private void setModel() {
        //Realm에서 모델 가져와 어뎁터 설정
        memoList = memoModel.getMemosInSameFolder(folderId);
        memoAdapter.setMemo(memoList);
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


    @Override
    protected void onDestroy() {
        //Realm 인스턴스 소멸 메소드 호출
        memoModel.closeRealm();
        super.onDestroy();
    }
}
