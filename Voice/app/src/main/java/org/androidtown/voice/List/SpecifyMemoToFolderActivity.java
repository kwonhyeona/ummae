package org.androidtown.voice.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.androidtown.voice.FolderRealm.Folder;
import org.androidtown.voice.FolderRealm.FolderAdapter;
import org.androidtown.voice.FolderRealm.FolderModel;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;

public class SpecifyMemoToFolderActivity extends AppCompatActivity {

    RecyclerView fRecyclerView;
    Toolbar moveToolbar;
    Button backButton, moveOk;

    // 폴더 RecyclerView에 사용될 모델, 어댑터 선언
    ArrayList<Folder> folderList;
    FolderAdapter folderAdapter;
    FolderModel folderModel;


    MemoModel model;
    Memo memo;


    int memoId;
    int folderPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specify_memo_to_folder);

        initDatas();
        initViews();

        setSupportActionBar(moveToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        onClickEventToolbar();

        fRecyclerView = (RecyclerView) findViewById(R.id.fRecyclerView);

        initList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fRecyclerView.setLayoutManager(layoutManager);

        fRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), fRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        folderPosition = position;
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }
                )
        );


    }

    private void onClickEventToolbar() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        moveOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model = new MemoModel();
                memo = model.getMemoById(memoId);

                Folder folder = folderModel.getFolderById(folderPosition);

                //이동하려는 폴더가 원래의 폴더일 결우,
                if (memo.getIdOfFolder() == folder.getFolderId()) {
                    Toast.makeText(getApplicationContext(), "현재 폴더가 아닌 다른 폴더를 선택하세요.",
                            Toast.LENGTH_LONG).show();
                } else {//다른 폴더인 경우 다른 폴더로 이동한다.

                    if(memo.getIdOfFolder() > 0) {
                        //원래 어느 폴더에 속해있는 경우, 전에 있었던 폴더의 element개수를 하나 빼준다.
                        String fName = folderModel.getFolderById(memo.getIdOfFolder()).getFoldername();
                        int eNum = (folderModel.getFolderById(memo.getIdOfFolder()).getElementNum()) - 1;
                        Folder previousFolder = new Folder(memo.getIdOfFolder(), fName, eNum);

                        folderModel.editFolder(previousFolder);


                    }

                    //선택된 폴더의 elementNum 을 하나 증가
                    int folderId = folder.getFolderId();
                    String folderName = folder.getFoldername();
                    int elementNum = folder.getElementNum() + 1;

                    Folder modify_folder = new Folder(folderId, folderName, elementNum);
                    folderModel.editFolder(modify_folder);

                    //메모의 폴더id를 선택한 폴더id로 수정
                    int memoId = memo.getMemoId();
                    int idOfFolder = folder.getFolderId();
                    String memoName = memo.getMemoName();
                    String memoday = memo.getMemoday();
                    String memoContents = memo.getMemoContents();
                    boolean isSelected = memo.getIsSelected();

                    Memo changeMemo = new Memo(memoId, idOfFolder, memoName, memoday, memoContents, isSelected);

                    model.editMemo(changeMemo);
                    model.closeRealm();

                    finish();
                }
            }
        });
    }

    private void initViews() {
        moveToolbar = (Toolbar) findViewById(R.id.moveToolbar);
        backButton = (Button) findViewById(R.id.backButton);
        moveOk = (Button) findViewById(R.id.moveOk);
    }

    private void initList() {
        // 폴더 리스트 보여주기
        initFolderModel();
        setFolderModel();
        initFolderAdapter();
    }

    private void initFolderAdapter() {
        // adapter 초기화, adapter에 모델 설정, recyclerview에 folderadapter 달아주기
        folderAdapter = new FolderAdapter(getApplicationContext());
        folderAdapter.setFolder(folderList);
        Log.i("Tag", "폴더리스트 " + folderList.size());
        fRecyclerView.setAdapter(folderAdapter);
    }

    private void initFolderModel() {
        //모델 초기화(Realm 모델도 초기화)
        folderList = new ArrayList<>();
        folderModel = new FolderModel();

    }

    private void setFolderModel() {
        //폴더 어뎁터 설정
        folderList = folderModel.getAllFolders();
//        folderAdapter.setFolder(folderModel.getAllFolders());
    }


    private void initDatas() {
        model = new MemoModel();

        //만약 "id"라는 키로 넘어온 값이 없으면 -1로 초기화
        Intent intent = getIntent();
        memoId = intent.getIntExtra("memoId", -1);

        //int형의 데이터를 인텐트로 받아올 때 에러처리
        if (memoId < 0) {
            finish();
        } else {
            //인텐트로 넘겨받은 id를 통해 Realm에서 해당 id의 Memo 데이터 받아오기
            model.getMemoById(memoId);
        }
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
        folderModel.closeRealm();
        super.onDestroy();
    }

}
