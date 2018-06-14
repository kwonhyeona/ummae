package org.androidtown.voice.Record;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.naver.speech.clientapi.SpeechConfig;

import org.androidtown.voice.Dialog.MemoAddDialog;
import org.androidtown.voice.NaverRecognizer;
import org.androidtown.voice.R;
import org.androidtown.voice.utils.AudioWriterPCM;

import java.lang.ref.WeakReference;

public class RecordFragment extends Fragment {

    Button btn_save, btn_stop;
    ToggleButton btn_pause;
    ViewPager pager;

    private static final String CLIENT_ID = "NQN5xCmWRVxQnEhEZHjS"; // "내 애플리케이션"에서 Client ID
    private static final SpeechConfig SPEECH_CONFIG = SpeechConfig.OPENAPI_KR; // or SpeechConfig.OPENAPI_EN

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult;
    private Button btnStart;
    private String mResult;
    private AudioWriterPCM writer;
    private boolean isRunning;

    LinearLayout layout;
    String tempText = "";

//    public static void setLibraryPath(String path) throws Exception {
//        System.setProperty("java.library.path", path);
//        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
//        sysPathsField.setAccessible(true);
//        sysPathsField.set(null, null);
//    }
//
//    static {
//        try {
//            setLibraryPath({System.loadLibrary()}); // 여기서 system path 설정
//            } catch (Exception e) {
//            e.printStackTrace();
//            }
//        try {
//            System.loadLibrary ({YOUR LIBRARY});
//            } catch (UnsatisfiedLinkError e) {
//            System.err.println (
//                    "The dynamic link library for Java could not be"
//                    + "loaded .\nConsider using \njava -Djava.library.path =\n" + e.getMessage());
//            throw e;
//            } catch (Exception e) {
//            e.printStackTrace();
//            }
//    }

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txtResult.setText("Connected");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                txtResult.setText(mResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                String[] results = (String[]) msg.obj;
                mResult = results[0];
                txtResult.setText(mResult);
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                txtResult.setText(mResult);
                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                isRunning = false;
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                isRunning = false;
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_record, null);

        btn_pause = (ToggleButton) view.findViewById(R.id.pause);
        btn_stop = (Button) view.findViewById(R.id.stop);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        txtResult = (TextView) view.findViewById(R.id.txt_result);
        btnStart = (Button) view.findViewById(R.id.btn_start);
        pager = (ViewPager) getActivity().findViewById(R.id.mViewPager);

        layout = (LinearLayout) view.findViewById(R.id.layout_ready_record);

        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(getActivity(), handler, CLIENT_ID, SPEECH_CONFIG);

        onClickEvents();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // initialize() must be called on resume time.
        naverRecognizer.getSpeechRecognizer().initialize();

        mResult = "";
        txtResult.setText("");
        btnStart.setText(R.string.str_start);
        btnStart.setEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        // release() must be called on pause time.
        naverRecognizer.getSpeechRecognizer().stopImmediately();
        naverRecognizer.getSpeechRecognizer().release();
        isRunning = false;
    }

    private void onClickEvents() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_start:
                        layout.setVisibility(View.INVISIBLE);
                }
                if (!isRunning) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    txtResult.setText("Connecting...");
                    btnStart.setText(R.string.str_listening);
                    isRunning = true;

                    naverRecognizer.recognize();
                } else {
                    // This flow is occurred by pushing start button again
                    // when SpeechRecognizer is running.
                    // Because it means that a user wants to cancel speech
                    // recognition commonly, so call stop().
                    btnStart.setEnabled(false);

                    naverRecognizer.getSpeechRecognizer().stop();
                }
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            // 일시정지, 누르면 재생모양으로 바뀌고 녹음 일시정지 -> 토글버튼 이용
            @Override
            public void onClick(View v) {
                if (btn_pause.isChecked()) {
                    tempText = txtResult.getText().toString();
                    isRunning = false;
                    naverRecognizer.getSpeechRecognizer().stop();
//                    btn_pause.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                    btn_pause.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_button));
                } else {
                    // TODO: 2016-08-03 다시 녹음되는지 확인하기!!
                    //naverRecognizer.onReady();
                    //naverRecognizer.onPartitialResult(txtResult.getText().toString());
                    isRunning = true;
                    naverRecognizer.recognize();
                    //naverRecognizer.onReady();
                    //naverRecognizer.onPartitialResult(tempText);
                    //txtResult.setText(tempText.toString());
                    //txtResult.append(tempText.toString() + txtResult.getText().toString());
                    btn_pause.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_pause_click));
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            // 중지, 누르면 txtResult창 초기화되고 마이크 있는 곳으로 다시 가기
            @Override
            public void onClick(View v) {
                txtResult.setText("");
                layout.setVisibility(View.VISIBLE);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메모 이름 입력받을 다이얼로그 띄우기
                MemoAddDialog memoAddDialog = new MemoAddDialog(getContext(), txtResult.getText().toString(), pager);
                memoAddDialog.show();

            }
        });
    }


    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<RecordFragment> mActivity;

        RecognitionHandler(RecordFragment activity) {
            mActivity = new WeakReference<RecordFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RecordFragment activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}