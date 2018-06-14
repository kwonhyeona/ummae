package org.androidtown.voice.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.androidtown.voice.FolderRealm.Folder;
import org.androidtown.voice.FolderRealm.FolderModel;
import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

public class ChangeNameDialog extends Dialog {
    private EditText txt_title;
    private Button btnCancel,btnOk;
    private TextView  txt_dialogtitle;

    MemoModel model;
    Memo memo;

    FolderModel folderModel;
    Folder folder;

    ViewPager pager;

    //메모인지 폴더인지 확인하는 변수 true일때 메모, false일때 폴더
    boolean isMemo;

    int id;

    public ChangeNameDialog(Context context) {
        super(context);
    }

    public ChangeNameDialog(Context context, int id , boolean isMemo, ViewPager pager) {
        super(context);
        this.id = id;
        this.isMemo = isMemo;
        this.pager = pager;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_change_name);

        if(isMemo == true){
            initMemoDatas();
        }
        else {
            initFolderDatas();

        }
        initViews();
        onClicked();
    }

    private void initMemoDatas() {
        model = new MemoModel();
        memo = model.getMemoById(id);
    }

    private void initFolderDatas() {
        folderModel = new FolderModel();
        folder = folderModel.getFolderById(id);
    }

    //View 초기화
    private void initViews() {
        txt_title = (EditText) findViewById(R.id.txt_title);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOk = (Button) findViewById(R.id.btnOk);
        txt_dialogtitle = (TextView) findViewById(R.id.txt_dialogtitle);
        txt_dialogtitle.setText("이름변경");

    }

    //각 View 리스너 설정
    private void onClicked() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeNameDialog.this.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메모 이름 변경
                if(isMemo == true) {
                    String name = txt_title.getText().toString();
                    String contents = memo.getMemoContents();
                    String memoday = memo.getMemoday();
                    boolean isSelected = memo.getIsSelected();

                    Memo memo = new Memo(id, name, contents, memoday, isSelected);

                    model.editMemo(memo);
                    model.closeRealm();
                    pager.getAdapter().notifyDataSetChanged();
                }
                //폴더 이름 변경
                else{
                    String name = txt_title.getText().toString();
                    int elementNum = folder.getElementNum();
                    boolean isSelected = folder.getIsSelected();

                    Folder folder = new Folder(id, name, elementNum, isSelected);

                    folderModel.editFolder(folder);
                    folderModel.closeRealm();
                    pager.getAdapter().notifyDataSetChanged();
                }
                ChangeNameDialog.this.dismiss();
            }
        });
    }
}