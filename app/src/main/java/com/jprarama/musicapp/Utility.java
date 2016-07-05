package com.jprarama.musicapp;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

import com.jprarama.musicapp.model.AudioItem;

import java.util.ArrayList;

/**
 * Created by joshua on 5/7/16.
 */
public class Utility {

    public static ArrayList<AudioItem> getAudioFiles(Context context) {
        String selection = Media.IS_MUSIC + " != 0";
        final Cursor mCursor = context.getContentResolver().query(
                Media.EXTERNAL_CONTENT_URI,
                new String[]{Media.TITLE, Media.DATA, Media.ARTIST, Media.ALBUM},
                selection, null, "LOWER(" + Media.TITLE + ") ASC");

        int count = mCursor.getCount();
        ArrayList<AudioItem> items = new ArrayList<>(count);

        if (mCursor.moveToFirst()) {
            do {
                AudioItem item = new AudioItem();
                item.setTitle(mCursor.getString(mCursor.getColumnIndexOrThrow(Media.TITLE)));
                item.setPath(mCursor.getString(mCursor.getColumnIndexOrThrow(Media.DATA)));
                item.setArtist(mCursor.getString(mCursor.getColumnIndexOrThrow(Media.ARTIST)));
                item.setAlbum(mCursor.getString(mCursor.getColumnIndexOrThrow(Media.ALBUM)));

                items.add(item);
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return items;
    }
}
