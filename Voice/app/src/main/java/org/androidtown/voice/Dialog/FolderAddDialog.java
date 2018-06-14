package org.androidtown.voice.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.voice.FolderRealm.Folder;
import org.androidtown.voice.FolderRealm.FolderModel;
import org.androidtown.voice.R;

public class FolderAddDialog extends Dialog {
    private EditText txt_title;
    private Button btnCancel, btnOk;
    private TextView txt_dialogtitle;
    ViewPager pager;

    public FolderAddDialog(Context context) {
        super(context);
    }

    public FolderAddDialog(Context context, ViewPager pager) {
        super(context);
        this.pager = pager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        getWindow();
        setContentView(R.layout.dialog_change_name);

        initViews();
        onClicked();
    }

    //View 초기화
    private void initViews() {
        txt_title = (EditText) findViewById(R.id.txt_title);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOk = (Button) findViewById(R.id.btnOk);
        txt_dialogtitle = (TextView) findViewById(R.id.txt_dialogtitle);
        txt_dialogtitle.setText("폴더추가");
    }

    //각 View 리스너 설정
    private void onClicked() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderAddDialog.this.dismiss();

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txt_title.getText().toString())) {//폴더이름 입력 시,
                    FolderModel model = new FolderModel();

                    //PrimaryKey인 id 겹치지 않게 id 값 정하기
                    int folderId = model.getNewId();
                    String name = txt_title.getText().toString();
                    int elementNum = 0;

                    //Folder 객체 생성 후 MemoModel에 넘겨줘서 삽입
                    Folder folder = new Folder(folderId, name, elementNum);

                    model.addFolder(folder);
                    model.closeRealm();

                    pager.getAdapter().notifyDataSetChanged();
                    Toast.makeText(getContext(), "폴더추가 완료", Toast.LENGTH_SHORT).show();

                    //Realm에 저장 후 다이얼로그 종료
                    FolderAddDialog.this.dismiss();

                }else{
                    Toast.makeText(getContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}