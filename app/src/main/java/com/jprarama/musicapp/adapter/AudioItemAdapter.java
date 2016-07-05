package com.jprarama.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jprarama.musicapp.R;
import com.jprarama.musicapp.model.AudioItem;

import java.util.List;

/**
 * Created by joshua on 5/7/16.
 */
public class AudioItemAdapter extends ArrayAdapter<AudioItem> {

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvArtist;
    }

    public AudioItemAdapter(Context context, int resource, List<AudioItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AudioItem audioItem = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.music_item, parent, false);

            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvArtist = (TextView) convertView.findViewById(R.id.tvArtist);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(audioItem.getTitle());
        viewHolder.tvArtist.setText(audioItem.getArtist());
        return convertView;
    }
}
