package com.example.necky0.musicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final static String RADIO_STATUS = "RADIO_STATUS";
    public final static String RADIO_NONE = "RADIO_NONE";
    public final static String RADIO_ONE = "RADIO_ONE";
    public final static String RADIO_ALL = "RADIO_ALL";

    private ListView songsList;
    private SongListAdapter songListAdapter;
    private MusicService musicService;

    private Button buttonRewind;
    private Button buttonForward;
    private Button buttonPlayStop;

    private Handler handler;

    private SeekBar progressBar;

    private TextView title;
    private TextView author;

    private static SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initSharedPreferences();
        initService();
        initViews();
        initAdapters();
        initListeners();
        Songs.initTimes(this);
        start();
    }

    private void init() {
        handler = new Handler();
    }

    private void initListeners() {
        buttonPlayStop.setOnClickListener(buttonPlayStopListener);
        buttonForward.setOnClickListener(buttonForwardListener);
        buttonRewind.setOnClickListener(buttonRewindListener);
        progressBarListener(progressBar);
    }

    private void initService() {
        musicService = MusicServiceInstance.getInstance();
        getApplicationContext().startService(new Intent(this, MusicService.class));
    }

    private void initSharedPreferences() {
        if (sp == null) {
            sp = getSharedPreferences(RADIO_STATUS, MODE_PRIVATE);
        }
    }

    public static String getRadioStatus() {
        return sp.getString(RADIO_STATUS, "");
    }

    public static void setRadioStatus(String status) {
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(RADIO_STATUS, status);
        spe.apply();
    }

    private void initAdapters() {
        songListAdapter = new SongListAdapter(this, Songs.set);
        songsList.setAdapter(songListAdapter);
    }

    private void initViews() {
        songsList = findViewById(R.id.songsList);
        buttonRewind = findViewById(R.id.buttonRewind);
        buttonForward = findViewById(R.id.buttonForward);
        buttonPlayStop = findViewById(R.id.buttonPlayStop);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        progressBar = findViewById(R.id.progressBar);
    }

    private void start() {
        songListAdapter.setInfoPlaySong();
        updateProgressBar.run();
    }

    public static void changeSong(MainActivity instance, Song song) {
        if(song != null) {
            instance.title.setText(song.getTitle());
            instance.author.setText(song.getAuthor());
            if (instance.musicService.isPlaying()) {
                instance.buttonPlayStop.setBackgroundResource(R.drawable.pause);
            } else {
                instance.buttonPlayStop.setBackgroundResource(R.drawable.play);
            }
        } else {
            instance.title.setText("");
            instance.author.setText("");
        }
    }

    public static void setButtonOnPause(MainActivity instance) {
        instance.buttonPlayStop.setBackgroundResource(R.drawable.pause);
    }

    public static void setButtonOnPlay(MainActivity instance) {
        instance.buttonPlayStop.setBackgroundResource(R.drawable.play);
    }

    public void progressBarListener(SeekBar seekBar){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    MusicServiceInstance.getInstance().getMediaPlayer().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    View.OnClickListener buttonPlayStopListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (musicService.isPlaying()) {
                musicService.pauseSong();
                buttonPlayStop.setBackgroundResource(R.drawable.play);
            } else if (SongListAdapter.isSongLoaded()) {
                musicService.playSong();
                buttonPlayStop.setBackgroundResource(R.drawable.pause);
            }
        }
    };

    View.OnClickListener buttonForwardListener = new View.OnClickListener() {
        public void onClick(View v) {
            musicService.seekForward();
        }
    };

    View.OnClickListener buttonRewindListener = new View.OnClickListener() {
        public void onClick(View v) {
            musicService.seekBackward();
        }
    };

    private Runnable updateProgressBar = new Runnable() {
        @Override
        public void run() {
        progressBar.setMax(MusicServiceInstance.getInstance().getMediaPlayer().getDuration());
        progressBar.setProgress(MusicServiceInstance.getInstance().getMediaPlayer().getCurrentPosition());
        handler.postDelayed(this, 50);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_me:
                AboutMe.start(MainActivity.this);
                return true;
            case R.id.settings:
                Settings.start(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
