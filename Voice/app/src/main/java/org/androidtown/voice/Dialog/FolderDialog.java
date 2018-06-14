package org.androidtown.voice.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import org.androidtown.voice.R;

public class FolderDialog extends Dialog {

    public FolderDialog(Context context) {
        super(context);
    }
    // private View.OnClickListener  okListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(800,800);
        setContentView(R.layout.dialog_add_folder);

    }
}