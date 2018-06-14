package org.androidtown.voice.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.androidtown.voice.Dialog.FolderAddDialog;
import org.androidtown.voice.Dialog.FolderClickDialog;
import org.androidtown.voice.Dialog.MemoClickDialog;
import org.androidtown.voice.Dialog.SpecifyFolderDialog;
import org.androidtown.voice.FolderRealm.Folder;
import org.androidtown.voice.FolderRealm.FolderAdapter;
import org.androidtown.voice.FolderRealm.FolderModel;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoAdapter;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;


public class ListFragment extends Fragment {
    //View 선언
    RadioGroup radioGroup;
    RadioButton btn_folder, btn_memo;
    FloatingActionMenu floating_menu;
    FloatingActionButton addFolderFab, deleteFolderFab, deleteMemoFab;
    RecyclerView memoRecyclerView, folderRecyclerView;
    ViewPager pager;

    //Dialog 선언
    MemoClickDialog memoClickDialog;
    FolderAddDialog folderAddDialog;
    SpecifyFolderDialog specifyFolderDialog;
    FolderClickDialog folderClickDialog;

    //메모 RecyclerView에 사용될 모델, 어뎁터 선언
    ArrayList<Memo> memoList;
    MemoAdapter memoAdapter;
    MemoModel memoModel;

    // 폴더 RecyclerView에 사용될 모델, 어댑터 선언
    ArrayList<Folder> folderList;
    FolderAdapter folderAdapter;
    FolderModel folderModel;

    // 다중선택
    boolean isChecked;

    // 메모랑 폴더 리싸이클러 뷰 구분하려고
    boolean isRecyclermemo = false;

    // 상세메모 갔다가 돌아올 때 쓰이는 변수
    boolean isReturn = false;


    @Override
    public void onResume() {
        //onResume 은 Activity가 화면에 나타나기 직전 실행되는 메소드
        super.onResume();

        if(isRecyclermemo==true) {
            initMemoList();
        }else
            initFolderList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, container, false);

        //View 초기화
        memoRecyclerView = (RecyclerView) view.findViewById(R.id.allRecyclerView);
        folderRecyclerView = (RecyclerView) view.findViewById(R.id.allRecyclerView);
        radioGroup = (RadioGroup) view.findViewById(R.id.list_radiogroup);
        btn_folder = (RadioButton) view.findViewById(R.id.btn_folder);
        btn_memo = (RadioButton) view.findViewById(R.id.btn_memo);
        floating_menu = (FloatingActionMenu) view.findViewById(R.id.floating_menu);
        addFolderFab = (FloatingActionButton) view.findViewById(R.id.add_folder);
        deleteMemoFab = (FloatingActionButton) view.findViewById(R.id.delete_memo);
        deleteFolderFab = (FloatingActionButton) view.findViewById(R.id.delete_folder);
//        moveMemoFab = (FloatingActionButton) view.findViewById(R.id.move_memo);
        pager = (ViewPager)getActivity().findViewById(R.id.mViewPager);

//        pager.getAdapter().notifyDataSetChanged();

        isChecked = false;

        //item을 adapter에 추가할 때 size가 달라도 맞춰주는 메소드
        memoRecyclerView.setHasFixedSize(true);
        folderRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        memoRecyclerView.setLayoutManager(layoutManager);
        folderRecyclerView.setLayoutManager(layoutManager);

        onTouchEvents();

        return view;
    }

    private void onTouchEvents() {

        //라디오그룹 터치이벤트트
       radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_folder:
                        initFolderList();
                        break;
                    case R.id.btn_memo:
                        initMemoList();
                        break;
                    default:
                        break;
                }
            }
        });


        //리싸이클러뷰 Listener 구현
        memoRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), memoRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (isRecyclermemo == true) {
                            //클릭한 리스트뷰의 아이템을 MemoAdapter에서 정의했던 getItem 메소드를 이용하여 가져오기
                            //모델에서 id를 가져와서 인텐트로 MemoDetailActivity로 보내주기
                            Memo memo = (Memo) memoAdapter.getItem(position);

                            isReturn = true;

                            Intent intent = new Intent(getContext(), MemoDetailActivity.class);
                            intent.putExtra("memoId", memo.getMemoId());

                            startActivity(intent);

                        } else {
                            Folder folder = (Folder) folderAdapter.getItem(position);

                            if (folder.getElementNum() > 0) {
                                Intent intent = new Intent(getContext(), FolderDetailActivity.class);
                                intent.putExtra("folderId", folder.getFolderId());

                                startActivity(intent);

                            } else {
                                Toast.makeText(getContext(), "메모가 없습니다. ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        if (isRecyclermemo == true) {
                            //롱클릭시 메모다이얼로그 보여주기
                            Memo memo = (Memo) memoAdapter.getItem(position);

                            Log.i("Tag", "선택된 메모 position" + position + "메모 제목" + memo.getMemoName());

                            memoClickDialog = new MemoClickDialog(view.getContext(), memo.getMemoId(), pager);
                            memoClickDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                            memoClickDialog.setContentView(R.layout.dialog_long_click);
//                            memoClickDialog.getWindow().setLayout(800, 800);
                            memoClickDialog.show();
                        }else{
                            //롱클릭 구현
                            Folder folder = (Folder) folderAdapter.getItem(position);
                            folderClickDialog = new FolderClickDialog(view.getContext(), folder.getFolderId(), pager);
                            folderClickDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            folderClickDialog.setContentView(R.layout.dialog_long_click);
//                            folderClickDialog.getWindow().setLayout(800, 800);
                            folderClickDialog.show();

                            folderAdapter.notifyDataSetChanged();
                        }
                    }
                })
        );


        //플로팅버튼 Listener 구현
        addFolderFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                folderAddDialog = new FolderAddDialog(v.getContext(), pager);
                folderAddDialog.show();
            }
        });
        deleteFolderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DeleteFolderActivity.class);
                startActivity(intent);
                initFolderList();

            }
        });
        deleteMemoFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteMemoActivity.class);
                startActivity(intent);
                initMemoAdapter();
            }
        });
//        moveMemoFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                specifyFolderDialog = new SpecifyFolderDialog(view.getContext());
////                specifyFolderDialog.setTitle("폴더 선택");
////                specifyFolderDialog.show();
//                Intent intent = new Intent(getActivity(), MoveMemoActivity.class);
//                startActivity(intent);
//                initMemoAdapter();
//
//            }
//        });
    }

    private void initMemoAdapter() {
        //adapter 초기화, adapter에 모델 설정, recyclerview에 adapter 달아주기
//        adapter = new MemoAdapter(getContext());
        memoAdapter = new MemoAdapter(getContext(), isChecked);
        memoAdapter.setMemo(memoList);
        memoRecyclerView.setAdapter(memoAdapter);
    }

    private void initFolderAdapter() {
        // adapter 초기화, adapter에 모델 설정, recyclerview에 folderadapter 달아주기
//        folderAdapter = new FolderAdapter(getContext());
        folderAdapter = new FolderAdapter(getContext(), isChecked);
        folderAdapter.setFolder(folderList);
        folderRecyclerView.setAdapter(folderAdapter);
    }

    private void initMemoModel() {
        //모델 초기화(Realm 모델도 초기화)
        memoList = new ArrayList<>();
        memoModel = new MemoModel();
    }

    private void initFolderModel() {
        //모델 초기화(Realm 모델도 초기화)
        folderList = new ArrayList<>();
        folderModel = new FolderModel();
    }

    private void setMemoModel() {
        //Realm에서 모델 가져와 어뎁터 설정
        memoList = memoModel.getAllMemos();
        memoAdapter.setMemo(memoModel.getAllMemos());
    }

    private void setFolderModel() {
        //폴더 어뎁터 설정
        folderList = folderModel.getAllFolders();
//        folderAdapter.setFolder(folderModel.getAllFolders());
    }

    private void initFolderList() {
        // 폴더 리스트 보여주기
        isRecyclermemo = false;
        initFolderModel();
        setFolderModel();
        initFolderAdapter();
    }

    private void initMemoList() {
        isRecyclermemo = true;
        initMemoAdapter();
        initMemoModel();
        setMemoModel();

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