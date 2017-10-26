package com.wxh.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by xukai on 2017/10/26.
 */

public class Player implements OnBufferingUpdateListener,OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener{
    private static final String TAG = "XuKai_"+Player.class.getName();
    private MediaPlayer mediaPlayer;
    private SeekBar skbProgress;
    private TextView tv_totalTime;
    private TextView tv_playTime;
    private Timer mTimer=new Timer();
    private boolean isPlayer;
    private TimerTask mTimerTask;
    public Player(SeekBar progress, TextView TV_CurrentPoi,TextView TV_TotalDuration){
        this.skbProgress=progress;
        this.tv_totalTime = TV_TotalDuration;
        this.tv_playTime = TV_CurrentPoi;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(mediaPlayer==null)
                    return;
                if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                    handleProgress.sendEmptyMessage(0);
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);

        if(this.skbProgress!=null){
            skbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int press = seekBar.getProgress();
                    if(isPlayer) {
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(press);
                        isPlayer = false;
                    }
                }
            });
        }
    }
    /**
     * 通过定时器和Handler来更新进度条
     */
    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            int min = (position/1000)/60;
            int sec = (position/1000)%60;
            if(sec<10){
                tv_playTime.setText(min+":0"+sec);
            }else {
                tv_playTime.setText(min+":"+sec);
            }
            if (duration > 0) {
                skbProgress.setProgress(position);
            }
        }
    };

    public void play(){
        mediaPlayer.start();
        isPlayer = true;
    }

    public void playUrl(String videoUrl){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();//prepare之后自动播放
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void pause(){
        mediaPlayer.pause();
    }

    public void stop(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    /**监听器监听方法*/
    @Override
    public void onPrepared(MediaPlayer player) {
        play();
        int duration = player.getDuration();
        int total_min = duration/1000/60;
        int total_sec = (duration/100)%60;
        if(total_sec<10){
            tv_totalTime.setText(total_min+":0"+total_sec);
        }else {
            tv_totalTime.setText(total_min+":"+total_sec);
        }
        tv_playTime.setText("00:00");
        skbProgress.setMax(duration);
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        Log.e(TAG, "onCompletion");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer player, int bufferingProgress) {
        skbProgress.setSecondaryProgress(bufferingProgress);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if(mediaPlayer!=null){
            play();
        }
    }
}

