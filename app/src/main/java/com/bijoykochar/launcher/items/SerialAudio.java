package com.bijoykochar.launcher.items;

import java.io.Serializable;

import me.everything.providers.android.media.Audio;

/**
 * Created by bijoy on 10/19/15.
 */
public class SerialAudio implements Serializable {
    public String artist;
    public String album;
    public String title;
    public String displayName;
    public boolean isMusic;

    public SerialAudio(Audio audio) {
        displayName = audio.displayName;
        isMusic = audio.isMusic;
        album = audio.album;
        artist = audio.artist;
        title = audio.title;
    }

}
