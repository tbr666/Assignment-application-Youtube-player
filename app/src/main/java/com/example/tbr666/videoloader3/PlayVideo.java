package com.example.tbr666.videoloader3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Config;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * @author Tibor Žukina
 * @version 1.0
 * This class provides a video player that reproduces a selected video from the search results list.
 */
public class PlayVideo  extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private String video;
    private Boolean paused=false;
    private Boolean ended=false;


    // YouTube player view
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;
    private ProgressBar progressBar;
    private Boolean full=false;
   /*
    This method defines that the player resources must be released before the PlayVideo activity is finished
    */
    @Override
    protected void onDestroy() {
        if(player!=null){
            player.pause();
            player.release();
            player=null;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * The activity receives search results and gets the video id from it.
         */
        Result result = (Result)
                getIntent().getSerializableExtra("video");
        video=result.getId();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**
         * THe layout is set for this activity
         */
        setContentView(R.layout.activity_playvideo);

        final Drawable playimage = getResources().getDrawable(R.drawable.play);
        final Drawable pauseimage=getResources().getDrawable(R.drawable.pause);
        final EditText seconds=(EditText) findViewById(R.id.editText);
        final ImageButton stop = (ImageButton) findViewById(R.id.stopButton);
        final TextView progress=(TextView)findViewById(R.id.textView3);
        /**
         * When the stop button is pressed a youtube player releases it's resources and the PlayVideo acitivity stops.
         */
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.release();
                player=null;
                finish();
                Log.i("destroy","destroying player");
            }
        });
        stop.setBackgroundResource(R.drawable.stop);
        final ImageButton pause = (ImageButton) findViewById(R.id.pauseButton);
        /**
         * When the pause button is pressed, the video is paused if its' currently playing or continues playing if its' currently stopped.
         * The button image is also changed to play if the video is paused and it is changed to pause if the video is playing. When the video stops the
         * button image is changed into replay and the video starts playing from the begining if it's pressed.
         */
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ended==true){
                    player.seekToMillis(0);
                    ended=false;
                    paused=false;
                    pause.setBackgroundResource(R.drawable.pause);
                    player.play();
                }
                else {
                    if (paused == false) {
                        Log.i("pause", "pause pressed");
                        paused = true;
                        pause.setBackgroundResource(R.drawable.play);
                        player.pause();
                    } else {
                        Log.i("play", "play pressed");
                        paused = false;
                        pause.setBackgroundResource(R.drawable.pause);
                        player.play();
                    }
                }
            }
        });
        pause.setBackgroundResource(R.drawable.pause);
        final ImageButton fwd = (ImageButton) findViewById(R.id.fwdbutton);
        /**
         * When the foastforward button is pressed, the video is set to position specified by input pf test view progress.
         */
        fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.seekToMillis(Integer.parseInt(seconds.getText().toString()) * 1000);
            }
        });
        fwd.setBackgroundResource(R.drawable.fastforward);
        /**The progress bar that shows video player time is initialized
         */
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        /*
        The handler is defined that periodically refreshed progressBar and report about current time of the playing video.
         */
        final Handler handler =new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                /**
                 * The report of handler is periodically refreshed every second (1000 miliseconds)
                 */
                handler.postDelayed(this, 1000);
                if(player!=null) {
                    int all = player.getDurationMillis();
                    int set = player.getCurrentTimeMillis();
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.setProgress((100 * set) / all);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.refreshDrawableState();
                    if( ((all/1000)%60<10)&&((set/1000)%60<10)) progress.setText(set / 60000 + ":" +"0"+ (set / 1000) % 60 + "/" + all / 60000 +":"+"0"+ (all / 1000) % 60);
                    else if( ((all/1000)%60<10)&&((set/1000)%60>=10)) progress.setText(set / 60000 + ":" + (set / 1000) % 60 + "/" + all / 60000 +":"+"0"+ (all / 1000) % 60);
                    else if( ((all/1000)%60>=10)&&((set/1000)%60<10)) progress.setText(set / 60000 + ":"+"0" + (set / 1000) % 60 + "/" + all / 60000 +":"+ (all / 1000) % 60);
                    else progress.setText(set / 60000 + ":" + (set / 1000) % 60 + "/" + all / 60000 +":"+ (all / 1000) % 60);
                    if( (set==all)&&(set>1000)) { pause.setBackgroundResource(R.drawable.replay); ended=true;}
                }
            }
        };
        handler.postDelayed(r, 1000);
        /*
        The youtube view is initialized
         */
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize("AIzaSyDGjFLYDZRaUbbhpnEZ-UTJXZm5lQuiZhI", this);
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    /**
     * If the youtube player is successfully initialized the video is loaded
     * @param provider youtube player provider
     * @param player2 youtube player that needs to be loaded
     * @param wasRestored determines if video player has been recovered
     */
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player2, boolean wasRestored) {
        if (!wasRestored) {
            player=player2;
            // loadVideo() will auto play video
            player.loadVideo(video);
            // Hiding player controls, they are defined outside of video window
            player.setPlayerStyle(PlayerStyle.CHROMELESS);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(String.valueOf(R.string.developer_key), this);
        }
    }

    /**
     * @return youtube player service provider used to play selected video which is represented by  YoutubePlayerView object
     */
    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
}