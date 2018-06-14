package org.androidtown.voice.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.androidtown.voice.FolderRealm.Folder;
import org.androidtown.voice.FolderRealm.FolderAdapter;
import org.androidtown.voice.FolderRealm.FolderModel;
import org.androidtown.voice.List.SpecifyMemoToFolderActivity;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;

/**
 * Created by Sansung on 2016-08-13.
 */
public class SpecifyFolderDialog extends Dialog {

    RecyclerView fRecyclerView;

    // 폴더 RecyclerView에 사용될 모델, 어댑터 선언
    ArrayList<Folder> folderList;
    FolderAdapter folderAdapter;
    FolderModel folderModel;


    MemoModel model;
    Memo memo;


    int memoId;



    public SpecifyFolderDialog(Context context) {
        super(context);

        setContentView(R.layout.dialog_specify_folder);
    }

    public SpecifyFolderDialog(Context context, int memoId) {
        super(context);
        setContentView(R.layout.dialog_specify_folder);

        this.memoId = memoId;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fRecyclerView = (RecyclerView) findViewById(R.id.folderRecyclerView);
//        fRecyclerView.setHasFixedSize(true);

        initList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fRecyclerView.setLayoutManager(layoutManager);

        fRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), fRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        model = new MemoModel();
                        memo = model.getMemoById(memoId);

                        Log.i("Tag", "memo 폴더id : " + memo.getIdOfFolder());

                        //선택된 폴더의 정보 가져오기
                        Folder folder = folderModel.getFolderById(position);

                        Log.i("Tag", "folder 폴더id : " + folder.getFolderId());

                        //이동하려는 폴더가 원래의 폴더일 결우,
                        if (memo.getIdOfFolder() == folder.getFolderId()) {
                            Toast.makeText(getContext(), "현재 폴더가 아닌 다른 폴더를 선택하세요.",
                                    Toast.LENGTH_LONG).show();
                        } else {//다른 폴더인 경우 다른 폴더로 이동한다.

                            if (memo.getIdOfFolder() >= 0) {
                                //원래 어느 폴더에 속해있는 경우, 전에 있었던 폴더의 element개수를 하나 빼준다.
                                String fName = folderModel.getFolderById(memo.getIdOfFolder()).getFoldername();
                                int eNum = folderModel.getFolderById(memo.getIdOfFolder()).getElementNum() - 1;

                                Log.i("Tag", "이전 폴더리스트 개수" + eNum);
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

                            //SpecifyMemoToFolderActivity.

                            //다이얼로그 종료
                            SpecifyFolderDialog.this.dismiss();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        }
                    }
                )
        );

    }

    private void initList() {
        // 폴더 리스트 보여주기
        initFolderModel();
        setFolderModel();
        initFolderAdapter();
    }

    private void initFolderAdapter() {
        // adapter 초기화, adapter에 모델 설정, recyclerview에 folderadapter 달아주기
        folderAdapter = new FolderAdapter(getContext());
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
