package com.wxh.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

//    private ImageView playButton;
//    private TextView playTime;
//    private MusicPlayer player;
//    private SeekBar progressBar;
//    private TextView totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.test_layout);
//        playTime=(TextView) findViewById(R.id.playTime);
//        totalTime=(TextView) findViewById(R.id.totalTime);
//        playButton = (ImageView) findViewById(R.id.button_play);
//        progressBar = (SeekBar) findViewById(R.id.progress_bar);
//        player = MusicPlayer.getInstance();
//        player.init(progressBar,playTime,totalTime);
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                player.playUrl("http://wting.info:81/asdb/fiction/xuanhuan/doupocq/3tiozb1a.mp3");
//            }
//        });
    }
}
