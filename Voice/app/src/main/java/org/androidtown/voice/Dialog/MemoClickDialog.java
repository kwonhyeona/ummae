package org.androidtown.voice.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.androidtown.voice.FolderRealm.Folder;
import org.androidtown.voice.FolderRealm.FolderModel;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoAdapter;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

public class MemoClickDialog extends Dialog {
    //View 선언
    TextView changeNameTextView, moveTextView, deleteTextView, shareTextView;
    Memo memo;
    MemoModel model;

    SpecifyFolderDialog specifyFolderDialog;

    int id;
    MemoAdapter adapter;
    ViewPager pager;
    boolean isAdapter = false;

    int folderId;

    //생성자
    public MemoClickDialog(Context context) {
        super(context);
    }

    public MemoClickDialog(Context context, int id, ViewPager pager) {
        super(context);
        this.id = id;
        this.pager = pager;
    }

    public MemoClickDialog(Context context, int id, MemoAdapter adapter) {
        super(context);
        this.id = id;
        this.adapter = adapter;
        isAdapter = true;
    }

    public MemoClickDialog(Context context, int id, MemoAdapter adapter, int folderId) {
        super(context);
        this.id = id;
        this.adapter = adapter;
        this.folderId = folderId;
        isAdapter = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_long_click);


        initViews();
        onClicked();
    }

    //View 초기화
    private void initViews() {
        changeNameTextView = (TextView) findViewById(R.id.changeNameTextView);
        moveTextView = (TextView) findViewById(R.id.moveTextView);
        deleteTextView = (TextView) findViewById(R.id.deleteTextView);
        shareTextView = (TextView) findViewById(R.id.shareTextView);
    }

    //각 View 리스너 설정
    private void onClicked() {
        changeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeNameDialog changeNameDialog = new ChangeNameDialog(getContext(), id, true, pager);
                changeNameDialog.show();

                MemoClickDialog.this.dismiss();
            }
        });

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model = new MemoModel();
                memo = model.getMemoById(id);


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("메모 삭제")
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

                                if (memo.getIdOfFolder() >= 0) {
                                    FolderModel folderModel = new FolderModel();


                                    String fName = folderModel.getFolderById(memo.getIdOfFolder()).getFoldername();
                                    int eNum = folderModel.getFolderById(memo.getIdOfFolder()).getElementNum() - 1;

                                    Folder previousFolder = new Folder(memo.getIdOfFolder(), fName, eNum);

                                    folderModel.editFolder(previousFolder);
                                }

                                model.deleteMemo(id);
                                dialog.dismiss();

                                Log.d("gilsoo_Memo", "isAdapter : " + String.valueOf(isAdapter));
//                                pager.getAdapter().notifyDataSetChanged();
                                if (isAdapter) {
                                    Log.d("gilsoo_Memo", "adapter : " + adapter);
                                    adapter.setMemo(model.getMemosInSameFolder(folderId));
                                    adapter.notifyDataSetChanged();
                                    isAdapter = false;
                                }
                                MemoClickDialog.this.dismiss();
                                pager.getAdapter().notifyDataSetChanged();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        shareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model = new MemoModel();
                memo = model.getMemoById(id);

                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, memo.getMemoContents());
                getContext().startActivity(Intent.createChooser(shareIntent, "공유 하기"));
            }
        });
        moveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                model = new MemoModel();
                memo = model.getMemoById(id);

                Log.i("Tag", "선택된 메모 제목" + memo.getMemoName());
                specifyFolderDialog = new SpecifyFolderDialog(v.getContext(), memo.getMemoId());
                specifyFolderDialog.setTitle("폴더 선택");
                specifyFolderDialog.show();

//                Intent intent = new Intent(getContext(), SpecifyMemoToFolderActivity.class);
//                intent.putExtra("memoId", memo.getMemoId());
//
//                getContext().startActivity(intent);


                MemoClickDialog.this.dismiss();
            }
        });
    }
}
