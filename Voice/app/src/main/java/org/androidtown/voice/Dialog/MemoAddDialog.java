package org.androidtown.voice.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoAddDialog extends Dialog {
    private EditText txt_title;
    private Button btnCancel, btnOk;
    private String contentSave;
    TextView txt_dialogtitle;
    ViewPager pager;

    // 현재시간저장하기
    Date date = new Date(System.currentTimeMillis());

    //시간포맷지정하기
    SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년M월d일");
    SimpleDateFormat CurTimeFormat = new SimpleDateFormat("k:mm");

    //메모 저장하는 날짜 구하기
    String strCurDate = CurDateFormat.format(date);//2016년8월4형식
    String strCurTime = CurTimeFormat.format(date);//14:00형식

    //생성자
    public MemoAddDialog(Context context) {
        super(context);
    }

    public MemoAddDialog(Context context, String contentSave) {
        super(context);
        this.contentSave = contentSave;
    }

    public MemoAddDialog(Context context, String contentSave, ViewPager pager) {
        super(context);
        this.contentSave = contentSave;
        this.pager = pager;
    }

    public String getContentSave() {
        return contentSave;
    }

    public void setContentSave(String contentSave) {
        this.contentSave = contentSave;
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
        txt_dialogtitle.setText("메모추가");
    }

    //각 View 리스너 설정
    private void onClicked() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemoAddDialog.this.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isVaild()) {
                    MemoModel model = new MemoModel();

                    //PrimaryKey인 id 겹치지 않게 id 값 정하기
                    int id = model.getNewId();


                    String name = txt_title.getText().toString();
                    String content = contentSave;

                    //Memo 객체 생성 후 MemoModel에 넘겨줘서 삽입
                    Memo memo = new Memo(id, name, content, -1, strCurDate, strCurTime);

                    model.addMemo(memo);
                    model.closeRealm();


                    Log.i("Tag", "memo 폴더id" + memo.getIdOfFolder());

                    Toast.makeText(getContext(), "저장 완료", Toast.LENGTH_SHORT).show();

                    pager.getAdapter().notifyDataSetChanged();
                    MemoAddDialog.this.dismiss();

                }
            }


        });
    }

    //예외처리
    private boolean isVaild() {
        if (txt_title.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "메모 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}