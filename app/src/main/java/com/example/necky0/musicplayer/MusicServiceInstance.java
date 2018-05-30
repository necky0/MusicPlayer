package com.example.necky0.musicplayer;


class MusicServiceInstance {
    private static MusicService instance;

    static MusicService getInstance() {
        if(instance == null) {
            instance = new MusicService();
        }
        return instance;
    }
}
