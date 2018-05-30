package com.example.necky0.musicplayer;


import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.Nullable;

public class MusicService extends IntentService {
    public final static String NOTIFICATION_NAME = "Music Player";
    private MediaPlayer mediaPlayer;
    private static final int SEEK_RATE = 10000;
    private SongListAdapter adapter;
    private Context context;

    public MusicService() {
        super("Background music service");
        mediaPlayer = new MediaPlayer();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    public void playSong(Context context, Song song, SongListAdapter adapter){
        if(mediaPlayer.isPlaying()) mediaPlayer.stop();

        mediaPlayer = MediaPlayer.create(context, song.getSongLocation());
        this.adapter = adapter;
        this.context = context;

        setOnCompletionListener();

        sendNotification(song.getTitle(), song.getAuthor());

        mediaPlayer.start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotification(String title, String author) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        Notification n  = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(author)
                .setSmallIcon(R.drawable.stop)
                .setAutoCancel(true)
                .addAction(R.drawable.stop, NOTIFICATION_NAME, pIntent)
                .build();

        NotificationManager notificationManager;
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }


    public void setOnCompletionListener(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                String option = MainActivity.getRadioStatus();

                if(option.equals(MainActivity.RADIO_NONE)){
                    stopSong();
                }
                else if(option.equals(MainActivity.RADIO_ONE)){
                    restartCurrentSong();
                }
                else if(option.equals(MainActivity.RADIO_ALL)){
                    playNextSong();
                }
            }
        });
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void playSong(){
        mediaPlayer.start();
    }

    public void pauseSong(){
        mediaPlayer.pause();
    }

    public void stopSong(){
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
        adapter.setButtonOnPlay();
    }

    private void playNextSong() {
        adapter.playNextSong();
    }

    private void restartCurrentSong() {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }

    public void seekForward(){
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + SEEK_RATE);
    }

    public void seekBackward(){
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - SEEK_RATE);
    }
}
