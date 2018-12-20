package com.lh.rtspdemo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import org.videolan.vlc.VlcVideoView;
import org.videolan.vlc.listener.MediaListenerEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MediaListenerEvent {

    @BindView(R.id.et_url)
    EditText et_url;

    @BindView(R.id.rtsp_video)
    VlcVideoView videoView;


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
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        videoView.setMediaListenerEvent(this);
        videoView.setPath(et_url.getText().toString());
        videoView.startPlay();
    }


    @Override
    public void eventPlayInit(boolean openClose) {
        Log.i("lh", "=======eventPlayInit============" + openClose);
        if (openClose) {
            showDialog();
        }
    }

    @Override
    public void eventBuffing(int event, float buffing) {
        Log.i("lh", "=======eventBuffing============" + event + "=====buffing====" + buffing);
        if (buffing == 100.0) {
            stopDialog();
        }
    }

    @Override
    public void eventPlay(boolean isPlaying) {
        Log.i("lh", "=======eventPlay============" + isPlaying);
        if (!isPlaying) {
            stopDialog();
        }
    }

    @Override
    public void eventStop(boolean isPlayError) {
        Log.i("lh", "=======eventStop============" + isPlayError);
        stopDialog();
    }

    @Override
    public void eventError(int event, boolean show) {
        Log.i("lh", "=======eventError============" + event + "=====show=====" + show);
        stopDialog();
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.startPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lh", "=======onStop============");
        videoView.onStop();
        stopDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void stopDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    private void showDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }
}
