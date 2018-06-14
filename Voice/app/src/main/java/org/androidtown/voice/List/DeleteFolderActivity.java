package org.androidtown.voice.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.voice.FolderRealm.Folder;
import org.androidtown.voice.FolderRealm.FolderAdapter;
import org.androidtown.voice.FolderRealm.FolderModel;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;

public class DeleteFolderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView back;
    Button btn_delete;
    boolean check;
    TextView dtlfolder_tv_list_title;

    //ListView에 사용될 모델, 어뎁터 선언
    ArrayList<Folder> folderArrayList;
    FolderAdapter adapter;
    FolderModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_folder);
        check = true;

        //memo목록 보여주기 위한 recyclerView 초기화
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_folder_delete);

        back = (ImageView) findViewById(R.id.dltfolder_back);
        btn_delete = (Button) findViewById(R.id.dtlfolder_deletebtn);
        dtlfolder_tv_list_title = (TextView)findViewById(R.id.dtlfolder_tv_list_title);

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
                        adapter.getChecked();
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
                // todo 뒤로 가기 구현 이 액티비티에서 벗어나서 폴더리스트가 있는곳으로 가기?(ListFragment.java?)
                finish();

            }
        });

        dtlfolder_tv_list_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Folder> deleteFolderList = adapter.getChecked();

                AlertDialog.Builder popDialog = new AlertDialog.Builder(DeleteFolderActivity.this);

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
                                for (Folder folder : deleteFolderList) {

                                    ArrayList<Memo> memoList = new ArrayList<>();
                                    MemoModel cMemoModel = new MemoModel();
                                    memoList = cMemoModel.getMemosInSameFolder(folder.getFolderId());

                                    for(Memo memo : memoList){
                                        Log.i("hTag", "폴더 아이디 지울 메모 제목 : " + memo.getMemoName());

                                        String mName = memo.getMemoName();
                                        String content = memo.getMemoContents();
                                        String strCurDate = memo.getMemoday();
                                        String time = memo.getMemoTime();

                                        Memo editMemo = new Memo(memo.getMemoId(), mName, content, -1, strCurDate, time);

                                        cMemoModel.editMemo(editMemo);



                                    }
                                    model.deleteFolder(folder.getFolderId());

                                }

                                DeleteFolderActivity.this.finish();
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
        adapter = new FolderAdapter(getApplication(), check);
        adapter.setFolder(folderArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void initModel() {
        //모델 초기화(Realm 모델도 초기화)
        folderArrayList = new ArrayList<>();
        model = new FolderModel();
    }

    private void setModel() {
        //Realm에서 모델 가져와 어뎁터 설정
        folderArrayList = model.getAllFolders();
        adapter.setFolder(model.getAllFolders());
    }

    // 리싸이클러뷰에서 아이템에 리스너 준 부분
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