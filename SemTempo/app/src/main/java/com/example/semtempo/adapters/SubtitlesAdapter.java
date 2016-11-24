package com.example.semtempo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semtempo.R;

public class SubtitlesAdapter extends BaseAdapter{

    private String[] valores;
    private int[] colors;
    private float[] perc;
    private TextView perc_text;
    private View rootView;

    private static LayoutInflater inflater = null;
    public SubtitlesAdapter(Context context, String[] valores, int[] colors, float[] perc, TextView perc_text, View rootView) {

        this.valores = valores;
        this.colors = colors;
        this.perc = perc;
        this.perc_text = perc_text;
        this.rootView = rootView;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return valores.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView subtitle_text;
        ImageView subtitle_color;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();

        View rowView;

        rowView = inflater.inflate(R.layout.subtitles_item, null);
        holder.subtitle_text = (TextView) rowView.findViewById(R.id.subtitle_text);
        holder.subtitle_color = (ImageView) rowView.findViewById(R.id.subtitle_color);

        holder.subtitle_text.setText(valores[position]);
        holder.subtitle_color.setColorFilter(colors[position]);
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                perc_text.animate().scaleX(0).scaleY(0).start();

                rootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        perc_text.setText(Math.round(perc[position]) + "%");
                        perc_text.setTextColor(colors[position]);
                        perc_text.animate().scaleX(1).scaleY(1).start();
                    }
                }, 200);
            }
        });

        return rowView;
    }

} 