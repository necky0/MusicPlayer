package com.example.necky0.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;

class Songs {

    static Song[] set = new Song[] {
            new Song("Avengers", "Bro Russo", R.raw.avengers),
            new Song("Pirates", "Sparrow", R.raw.piraci),
            new Song("Avengers2", "Bro Russo", R.raw.avengers),
            new Song("Pirates2", "Sparrow", R.raw.piraci),
    };

    static void initTimes(Context context) {
        for(Song s: Songs.set) {
            s.setTime(calculateTime(context, s.getSongLocation()));
        }
    }

    private static String calculateTime(Context context, int resourceID){
        int duration = MediaPlayer.create(context, resourceID).getDuration();
        int minutes = duration / 60000;
        int seconds = (duration % 60000) / 1000;
        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
}
