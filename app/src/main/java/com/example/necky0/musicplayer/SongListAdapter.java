package com.example.necky0.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class SongListAdapter extends BaseAdapter {
    private Song[] dataset;
    private Context context;
    private LayoutInflater inflater;
    private static int currentPosition = -1;
    private static int lastPosition = -1;
    private static boolean isSongLoaded = false;
    private ArrayList<ViewHolder> holders;
    private ArrayList<View> views;

    static class ViewHolder {
        TextView item_title;
        TextView item_author;
        TextView item_time;
        Button item_button_play;

        ViewHolder(View view) {
            item_title = view.findViewById(R.id.item_title);
            item_author = view.findViewById(R.id.item_author);
            item_time = view.findViewById(R.id.item_time);
            item_button_play = view.findViewById(R.id.item_button_play);
        }
    }

    SongListAdapter(Context context, Song[] dataset) {
        this.dataset = dataset;
        this.context = context;
        holders = new ArrayList<>();
        views = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static boolean isSongLoaded() {
        return isSongLoaded;
    }

    @Override
    public int getCount() {
        return dataset.length;
    }

    @Override
    public Song getItem(int i) {
        return dataset[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null) view = inflater.inflate(R.layout.list_item, null);

        final ViewHolder holder;
        holder = new ViewHolder(view);
        holders.add(holder);
        views.add(view);
        holderListener(holder, position);
        setItemList(holder, position);

        return view;
    }

    private void holderListener(final ViewHolder holder, final int position) {
        holder.item_button_play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lastPosition = currentPosition;
                currentPosition = position;
                playStopSong(position, v, holder);
                setInfoPlaySong();
            }
        });
    }

    void setInfoPlaySong() {
        if (isSongLoaded) {
            MainActivity.changeSong((MainActivity) context, getItem(currentPosition));
        } else {
            MainActivity.changeSong((MainActivity) context, null);
        }
    }

    void setButtonOnPlay() {
        MainActivity.setButtonOnPlay((MainActivity) context);
    }

    private void setItemList(ViewHolder holder, int position) {
        holder.item_title.setText(dataset[position].getTitle());
        holder.item_author.setText(dataset[position].getAuthor());
        holder.item_time.setText(dataset[position].getTime());
        if (isSongLoaded && position == currentPosition) {
            holder.item_button_play.setBackgroundResource(R.drawable.stop);
            lastPosition = position;
        }
    }

    private void playStopSong(int index, View v, ViewHolder holder){
        MusicService musicService = MusicServiceInstance.getInstance();
        if (musicService.isPlaying()) {
            musicService.stopSong();
            if(index != lastPosition) {
                isSongLoaded = true;
                musicService.playSong(v.getContext(), getItem(index), this);
                holder.item_button_play.setBackgroundResource(R.drawable.stop);
                if (lastPosition >= 0) holders.get(lastPosition).item_button_play.setBackgroundResource(R.drawable.play);
                MainActivity.setButtonOnPause((MainActivity) context);
            } else {
                isSongLoaded = false;
                holder.item_button_play.setBackgroundResource(R.drawable.play);
                MainActivity.setButtonOnPlay((MainActivity) context);
            }
        } else {
            isSongLoaded = true;
            musicService.playSong(v.getContext(), getItem(index), this);
            holder.item_button_play.setBackgroundResource(R.drawable.stop);
            if (lastPosition >= 0) holders.get(lastPosition).item_button_play.setBackgroundResource(R.drawable.play);
            MainActivity.setButtonOnPause((MainActivity) context);
        }
    }

    void playNextSong() {
        MusicService musicService = MusicServiceInstance.getInstance();
        lastPosition = currentPosition;
        currentPosition = (currentPosition + 1) % dataset.length;
        musicService.playSong(views.get(currentPosition).getContext(), getItem(currentPosition), this);
        holders.get(currentPosition).item_button_play.setBackgroundResource(R.drawable.stop);
        if (lastPosition >= 0) holders.get(lastPosition).item_button_play.setBackgroundResource(R.drawable.play);
        setInfoPlaySong();
        MainActivity.setButtonOnPause((MainActivity) context);
    }
}
