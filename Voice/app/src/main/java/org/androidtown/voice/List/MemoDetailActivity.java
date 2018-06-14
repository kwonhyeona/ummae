package org.androidtown.voice.List;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

public class MemoDetailActivity extends AppCompatActivity {
    TextView contentsTextView;
    TextView title;
    ImageView back;


    MemoModel model;
    Memo memo;

    int memoId;

    @Override
    protected void onResume() {
        super.onResume();
        memo = model.getMemoById(memoId);
        setViewDatas();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);

        initDatas();
        initView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    private void setViewDatas() {
        title.setText(memo.getMemoName());
        contentsTextView.setText(memo.getMemoContents());
    }

    private void initView() {
        title = (TextView)findViewById(R.id.memo_title);
        contentsTextView = (TextView) findViewById(R.id.contentsTextView);
        back = (ImageView)findViewById(R.id.back_detail_memo);
    }

    @Override
    protected void onDestroy() {
        //Realm 인스턴스 소멸 메소드 호출
        model.closeRealm();
        super.onDestroy();
    }
}
