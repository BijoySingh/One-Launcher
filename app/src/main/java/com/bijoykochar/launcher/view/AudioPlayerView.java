package com.bijoykochar.launcher.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.items.SerialAudio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.everything.providers.android.media.Audio;
import me.everything.providers.android.media.MediaProvider;
import me.everything.providers.core.Data;
import me.everything.providers.core.Entity;

/**
 * Created by bijoy on 10/18/15.
 */
public class AudioPlayerView extends LinearLayout {

    ImageView playArt;

    TextView songName, songArtist;

    SerialAudio audio;

    public AudioPlayerView(Context context) {
        super(context);
    }

    public AudioPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AudioPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public List<SerialAudio> getSerialAudios(Context context) {
        MediaProvider provider = new MediaProvider(context);
        Data<Audio> audios = provider.getAudios(MediaProvider.Storage.EXTERNAL);

        List<SerialAudio> sAudios = new ArrayList<>();
        Cursor cursor = audios.getCursor();
        if (cursor == null) {
            return sAudios;
        }
        try {
            while (cursor.moveToNext()) {
                Audio audio = Entity.create(cursor, Audio.class);
                if (audio.isMusic && !audio.isRingtone && !audio.isAlarm && !audio.isNotification && !audio.isPodcast) {
                    sAudios.add(new SerialAudio(audio));
                }
            }
        } finally {
            cursor.close();
        }

        return sAudios;
    }

    public SerialAudio getAudio(Context context) {
        List<SerialAudio> audios = getSerialAudios(context);
        Collections.shuffle(audios);
        if (audios.isEmpty()) {
            return null;
        }
        return audios.get(0);
    }

    public void init(final Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = mInflater.inflate(R.layout.audio_player, this, true);

        playArt = (ImageView) rootView.findViewById(R.id.play_art);

        songName = (TextView) rootView.findViewById(R.id.song_name);
        songArtist = (TextView) rootView.findViewById(R.id.song_artist);

        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                context.startActivity(intent);
            }
        });
    }

    public void getData(Context context) {
        audio = getAudio(context);
    }

    public void displayAudios(Context context) {
        if (audio != null) {
            songName.setText(audio.title);
            songArtist.setText(audio.artist + ", " + audio.album);
        }
    }


}
