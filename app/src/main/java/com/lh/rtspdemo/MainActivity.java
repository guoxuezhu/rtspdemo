package com.lh.rtspdemo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import org.videolan.vlc.listener.MediaListenerEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MediaListenerEvent {

    @BindView(R.id.et_url)
    EditText et_url;

    @BindView(R.id.rtsp_video)
    MyVideoView videoView;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        String path = "rtsp://192.168.1.90/c=0&s=0";
//        String path = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        et_url.setText("rtsp://192.168.1.90/c=0&s=0");

    }

    @OnClick(R.id.btn_start)
    public void btn_start() {
        progressDialog = new ProgressDialog(this);
        videoView.setMediaListenerEvent(this);
        videoView.setPath(et_url.getText().toString());
        videoView.startPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lh", "=======onStop============");
        videoView.onStop();
    }

    @Override
    public void eventBuffing(int event, float buffing) {
        Log.i("lh", "=======eventBuffing============" + event + "=====buffing====" + buffing);
    }

    long time;

    @Override
    public void eventPlayInit(boolean openClose) {
        Log.i("lh", "=======eventPlayInit============" + openClose);
        if (!openClose) {
            time = System.currentTimeMillis();
        } else {
            long useTime = System.currentTimeMillis() - time;
            Log.i("yyl", "打开地址时间========" + useTime);
        }
        if (openClose)
            progressDialog.show();
    }

    @Override
    public void eventStop(boolean isPlayError) {
        Log.i("lh", "=======eventStop============" + isPlayError);
        progressDialog.hide();
    }

    @Override
    public void eventError(int event, boolean show) {
        Log.i("lh", "=======eventError============" + event + "=====show=====" + show);
        progressDialog.hide();
    }

    @Override
    public void eventPlay(boolean isPlaying) {
        Log.i("lh", "=======eventPlay============" + isPlaying);
        if (isPlaying) {
            long useTime = System.currentTimeMillis() - time;
            Log.i("yyl", "延迟时间========" + useTime);
            progressDialog.hide();
        }
    }
}
