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

import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by xukai on 2017/10/26.
 */

public class MusicPlayer implements OnBufferingUpdateListener,
        OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener{

    private static final String TAG = "XuKai_"+MusicPlayer.class.getName();
    private static MusicPlayer instance;
    private MusicPlayer(){}
    public static MusicPlayer getInstance(){
        if(instance==null){
            instance = new MusicPlayer();
        }
        return instance;
    }
    private MediaPlayer mediaPlayer;
    private SeekBar skbProgress;
    private TextView tv_totalTime;
    private TextView tv_playTime;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int current_progress;
    private int total_progress;

    public void init(SeekBar progress, TextView TV_CurrentPoi,TextView TV_TotalDuration){
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
        mTimer=new Timer();
        mTimer.schedule(mTimerTask, 0, 1000);
        if(this.skbProgress!=null){
            skbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        setPlayTimeText(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int press = seekBar.getProgress()*1000;
                    pause();
                    mediaPlayer.seekTo(press);
                    Log.e(TAG,"press:"+press);
                }
            });
        }
    }
    /**
     * 通过定时器和Handler来更新进度条
     */
    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {
            current_progress = mediaPlayer.getCurrentPosition()/1000;
            skbProgress.setProgress(current_progress);
            setPlayTimeText(current_progress);
        }
    };
    private void setPlayTimeText(int currentProgress){
        int poi_min = currentProgress/60;
        int poi_sec = currentProgress%60;
        String min = poi_min<10?"0"+poi_min:""+poi_min;
        String sec = poi_sec<10?"0"+poi_sec:""+poi_sec;
        tv_playTime.setText(min+":"+sec);
    }

    public void playUrl(String videoUrl){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();//prepare之后自动播放
        } catch (Exception e){

        }
    }

    public boolean isPlaying(){
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                return true;
            }
        }
        return false;
    }

    public void play(){
        mediaPlayer.start();
    }


    public void pause(){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void onDestroy(){
        stop();
        if(mTimerTask!=null){
            mTimerTask.cancel();
        }
        if(mTimer!=null){
            mTimer.cancel();
        }
        instance = null;
    }
    /**监听器监听方法*/
    @Override
    public void onPrepared(MediaPlayer player) {
        play();
        total_progress = player.getDuration()/1000;
        int total_min = total_progress/60;
        int total_sec = total_progress%60;
        String min = total_min<10?"0"+total_min:""+total_min;
        String sec = total_sec<10?"0"+total_sec:""+total_sec;
        tv_totalTime.setText(min+":"+sec);
        setPlayTimeText(0);
        skbProgress.setMax(total_progress);
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        Log.e(TAG, "onCompletion");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer player, int bufferingProgress) {
        Log.e(TAG,"bufferingProgress:"+bufferingProgress);
        skbProgress.setSecondaryProgress((int) (total_progress*bufferingProgress/100.0));
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.e(TAG,"onSeekComplete:"+mp.getCurrentPosition());
        if(mediaPlayer!=null){
            play();
        }
    }
}

