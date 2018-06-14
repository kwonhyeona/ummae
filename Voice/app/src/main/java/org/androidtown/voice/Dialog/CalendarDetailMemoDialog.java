package org.androidtown.voice.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.androidtown.voice.R;

/**
 * Created by Sansung on 2016-08-14.
 */
public class CalendarDetailMemoDialog extends Dialog {
    TextView memo_date, memo_time, contentsTextView;
    String date, time, contents;
    Button btn_close;

    public CalendarDetailMemoDialog(Context context) {
        super(context);
    }

    public CalendarDetailMemoDialog(Context context, String date, String time, String contents) {
        super(context);
        this.date = date;
        this.time = time;
        this.contents = contents;

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_calendar_detail_memo);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        memo_date.setText(date);
        memo_time.setText(time);
        contentsTextView.setText(contents);
        onClicked();
    }

    private void initViews() {
        memo_date = (TextView) findViewById(R.id.memo_date);
        memo_time = (TextView) findViewById(R.id.memo_time);
        contentsTextView = (TextView) findViewById(R.id.contentsTextView);
        btn_close = (Button) findViewById(R.id.btn_close);
    }

    private void onClicked() {
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDetailMemoDialog.this.dismiss();
            }
        });
    }


}
