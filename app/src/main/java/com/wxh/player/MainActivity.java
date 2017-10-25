package com.wxh.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageButton playButton;
    private boolean isPlaying;
    private TextView playTime;
    private StreamingMediaPlayer audioStreamer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }


    private void initControls() {
        playTime=(TextView) findViewById(R.id.playTime);
        playButton = (ImageButton) findViewById(R.id.button_play);
        playButton.setEnabled(false);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (audioStreamer.getMediaPlayer().isPlaying()) {
                    audioStreamer.getMediaPlayer().pause();
                    playButton.setImageResource(R.drawable.ic_launcher_round);
                } else {
                    audioStreamer.getMediaPlayer().start();
                    audioStreamer.startPlayProgressUpdater();
                    playButton.setImageResource(R.drawable.ic_launcher_round);
                }
                isPlaying = !isPlaying;
            }
        });
        startStreamingAudio();
    }

    private void startStreamingAudio() {
        try {
            final SeekBar progressBar = (SeekBar) findViewById(R.id.progress_bar);
            if ( audioStreamer != null) {
                audioStreamer.interrupt();
            }
            audioStreamer = new StreamingMediaPlayer(this, playButton,progressBar,playTime);
            audioStreamer.startStreaming("http://wting.info:81/asdb/fiction/xuanhuan/doupocq/3tiozb1a.mp3",5208, 216);
        } catch (IOException e) {
            Log.e(getClass().getName(), "Error starting to stream audio.", e);
        }

    }
}
