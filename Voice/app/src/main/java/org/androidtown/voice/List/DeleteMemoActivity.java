package org.androidtown.voice.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.voice.FolderRealm.Folder;
import org.androidtown.voice.FolderRealm.FolderModel;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoAdapter;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;

public class DeleteMemoActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button okbtn;
    ImageView back;
    TextView back2;

    //ListView에 사용될 모델, 어뎁터 선언
    ArrayList<Memo> memoList;
    MemoAdapter adapter;
    MemoModel model;
    Memo memo;
    boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_memo);
        check = true;

        //memo목록 보여주기 위한 recyclerView 초기화
        recyclerView = (RecyclerView) findViewById(R.id.fragment_both_list);
//        canclebtn = (Button) findViewById(R.id.canclebtn);
        okbtn = (Button) findViewById(R.id.okbtn);
        back = (ImageView) findViewById(R.id.dlt_memo_back);
        back2 = (TextView) findViewById(R.id.tv_list_title);

        //리스트뷰 갱신하기
        initModel();
        initAdapter();

        //item을 adapter에 추가할 때 size가 달라도 맞춰주는 메소드
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        onClickEvents();
    }

    private void onClickEvents() {
        //각 view 클릭리스너 설정
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //클릭한 리스트뷰의 아이템을 MemoAdapter에서 정의했던 getItem 메소드를 이용하여 가져오기
                        //모델에서 id를 가져와서 인텐트로 MemoDetailActivity로 보내주기
                        adapter.setChecked(position);
//                        adapter.getChecked();
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DeleteOkDialog deleteOkDialog = new DeleteOkDialog(DeleteMemoActivity.this);
//                deleteOkDialog.show();

                final ArrayList<Memo> deleteMemoList = adapter.getChecked();

                AlertDialog.Builder popDialog = new AlertDialog.Builder(DeleteMemoActivity.this);

                popDialog.setTitle("메모 삭제")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(Memo memo : deleteMemoList){
                                    if(memo.getIdOfFolder() >= 0){
                                        FolderModel folderModel = new FolderModel();


                                        String fName = folderModel.getFolderById(memo.getIdOfFolder()).getFoldername();
                                        int eNum = folderModel.getFolderById(memo.getIdOfFolder()).getElementNum() - 1;

                                        Folder previousFolder = new Folder(memo.getIdOfFolder(), fName, eNum);

                                        folderModel.editFolder(previousFolder);
                                    }
                                    model.deleteMemo(memo.getMemoId());
                                }

                                DeleteMemoActivity.this.finish();
                            }
                        });

                AlertDialog dialog = popDialog.create();
                dialog.show();
            }
        });

    }

    @Override
    public void onResume() {
        //onResume 은 Activity가 화면에 나타나기 직전 실행되는 메소드
        super.onResume();

        //리스트뷰 갱신하기
        setModel();
    }

    private void initAdapter() {
        //adapter 초기화, adapter에 모델 설정, recyclerview에 adapter 달아주기
        adapter = new MemoAdapter(getApplication(), check, memoList);
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

    // 민주
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