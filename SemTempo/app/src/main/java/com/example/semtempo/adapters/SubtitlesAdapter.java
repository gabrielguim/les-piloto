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

import java.util.List;

public class SubtitlesAdapter extends BaseAdapter{

    private List<String> valores;
    private List<Integer> colors;
    private List<Float> perc;
    private TextView perc_text;
    private View rootView;

    private LayoutInflater inflater;
    public SubtitlesAdapter(Context context, List<String> valores, List<Integer> colors, List<Float> perc, TextView perc_text, View rootView) {

        this.valores = valores;
        this.colors = colors;
        this.perc = perc;
        this.perc_text = perc_text;
        this.rootView = rootView;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return valores.size();
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
        try {
            holder.subtitle_text.setText(valores.get(position));
            holder.subtitle_color.setColorFilter(colors.get(position));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                perc_text.animate().scaleX(0).scaleY(0).start();

                rootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            perc_text.setText(Math.round(perc.get(position)) + "%");
                            perc_text.setTextColor(colors.get(position));
                            perc_text.animate().scaleX(1).scaleY(1).start();
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
                }, 200);
            }
        });

        return rowView;
    }

} 