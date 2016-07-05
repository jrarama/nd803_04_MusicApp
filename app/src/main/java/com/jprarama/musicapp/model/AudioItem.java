package com.jprarama.musicapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joshua on 5/7/16.
 */
public class AudioItem implements Parcelable {

    private String title;

    private String path;

    private String artist;

    private String album;

    public AudioItem() {
    }

    public AudioItem(String title, String path, String artist, String album) {
        this.title = title;
        this.path = path;
        this.artist = artist;
        this.album = album;
    }

    protected AudioItem(Parcel in) {
        title = in.readString();
        path = in.readString();
        artist = in.readString();
        album = in.readString();
    }

    public static final Creator<AudioItem> CREATOR = new Creator<AudioItem>() {
        @Override
        public AudioItem createFromParcel(Parcel in) {
            return new AudioItem(in);
        }

        @Override
        public AudioItem[] newArray(int size) {
            return new AudioItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(path);
        parcel.writeString(artist);
        parcel.writeString(album);
    }
}
