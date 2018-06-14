package org.androidtown.voice.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.androidtown.voice.FolderRealm.Folder;
import org.androidtown.voice.FolderRealm.FolderModel;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;

public class FolderClickDialog extends Dialog {

    TextView changeNameTextView, deleteTextView, shareTextView, moveTextView;
    FolderModel model;
    Folder folder;
    View longclick;

    int id;

    ViewPager pager;

    public FolderClickDialog(Context context) {
        super(context);
    }
    // private View.OnClickListener  okListener;

    public FolderClickDialog(Context context, int id, ViewPager pager) {
        super(context);
        this.id = id;
        this.pager = pager;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_long_click);

        initViews();
        //폴더에서 공유하기메뉴 제외
        shareTextView.setVisibility(View.GONE);
        moveTextView.setVisibility(View.GONE);
        onClicked();
    }

    //View 초기화
    private void initViews() {
        changeNameTextView = (TextView)findViewById(R.id.changeNameTextView);
        deleteTextView =  (TextView)findViewById(R.id.deleteTextView);
        shareTextView = (TextView)findViewById(R.id.shareTextView);
        moveTextView = (TextView)findViewById(R.id.moveTextView);
        longclick = findViewById(R.id.long_click_folder_view);
    }

    //각 View 리스너 설정
    private void onClicked() {
        changeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeNameDialog changeNameDialog = new ChangeNameDialog(getContext(), id, false, pager);
                changeNameDialog.show();


                FolderClickDialog.this.dismiss();
            }
        });

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model = new FolderModel();
                model.getFolderById(id);


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("폴더 삭제")
                        .setMessage("정말로 삭제하시겠습니까?")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //메모 삭제
                                ArrayList<Memo> memoList = new ArrayList<>();
                                MemoModel cMemoModel = new MemoModel();
                                memoList = cMemoModel.getMemosInSameFolder(model.getFolderById(id).getFolderId());

                                for(Memo memo : memoList){
                                    Log.i("hTag", "폴더 아이디 지울 메모 제목 : " + memo.getMemoName());

                                    String mName = memo.getMemoName();
                                    String content = memo.getMemoContents();
                                    String strCurDate = memo.getMemoday();
                                    String time = memo.getMemoTime();

                                    Memo editMemo = new Memo(memo.getMemoId(), mName, content, -1, strCurDate, time);

                                    cMemoModel.editMemo(editMemo);

                                }

                                model.deleteFolder(id);
                                dialog.dismiss();
                                FolderClickDialog.this.dismiss();
                                pager.getAdapter().notifyDataSetChanged();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}