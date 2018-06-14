package org.androidtown.voice.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.androidtown.voice.MemoRealm.Memo;
import org.androidtown.voice.MemoRealm.MemoModel;
import org.androidtown.voice.R;

import java.util.ArrayList;

/**
 * Created by Sansung on 2016-07-28.
 */
public class DeleteOkDialog extends Dialog {
    TextView deleteOk, deleteCancel;
    ArrayList<Memo> deleteMemoList;
    MemoModel model;

    int id;

    public DeleteOkDialog(Context context) {
        super(context);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_ok);
    }

    public DeleteOkDialog(Context context, int id) {
        super(context);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_ok);

        this.id = id;
    }

    public DeleteOkDialog(Context context, ArrayList<Memo> deleteMemoList) {
        super(context);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_ok);

        model = new MemoModel();
        model.getAllMemos();
        this.deleteMemoList = new ArrayList<Memo>();
        this.deleteMemoList = deleteMemoList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        onClicked();
    }

    private void initViews() {
        deleteOk = (TextView) findViewById(R.id.deleteOk);
        deleteCancel = (TextView) findViewById(R.id.deleteCancel);
    }

    private void onClicked() {
        deleteOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //램에서 삭제하는 코드
                for(Memo memo : deleteMemoList){
                    model.deleteMemo(memo.getMemoId());
                }
            }
        });

        deleteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOkDialog.this.dismiss();
            }
        });
    }
}
