package com.example.semtempo.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semtempo.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;
import com.example.semtempo.services.AtividadeService;
import com.example.semtempo.utils.CircleTransform;
import com.squareup.picasso.Picasso;

public class AllTasksAdapter extends BaseAdapter{

    private List<Atividade> atividades;
    private View rootView;
    private Context context;
    private static boolean verCategorias;
    private static boolean selecting;
    private static LayoutInflater inflater = null;
    private static List<Atividade> selectedActivities;

    public AllTasksAdapter(Context context, List<Atividade> atividades, View rootView) {

        this.rootView = rootView;
        this.atividades = atividades;
        this.context = context;
        this.selectedActivities = new ArrayList<>();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return atividades.size();
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
        TextView task_name;
        TextView task_time;
        TextView task_date;
        ImageView task_prority;
        ImageView task_photo;
        CardView card_view;
    }

    public void setVerCategorias(boolean ver){
        this.verCategorias = ver;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();

        View rowView;

        rowView = inflater.inflate(R.layout.recent_task_item, null);
        holder.task_name = (TextView) rowView.findViewById(R.id.task_name);
        holder.task_date = (TextView) rowView.findViewById(R.id.task_date);
        holder.task_time = (TextView) rowView.findViewById(R.id.task_time);
        holder.task_prority = (ImageView) rowView.findViewById(R.id.task_priority);
        holder.task_photo = (ImageView) rowView.findViewById(R.id.task_photo);
        holder.card_view = (CardView) rowView.findViewById(R.id.card_view);

        String color;
        if (atividades.get(position).getPrioridade() == Prioridade.ALTA){
            color = "#f10714";
        } else if (atividades.get(position).getPrioridade() == Prioridade.MEDIA){
            color = "#ffbf00";
        } else {
            color = "#4169e1";
        }

        String horasGastas = " Hora investida";

        if (atividades.get(position).getHorario().getTotalHorasInvestidas() > 1)
            horasGastas = " Horas investidas";

        Horario horario = atividades.get(position).getHorario();

        holder.task_name.setText(atividades.get(position).getNomeDaAtv());
        holder.task_time.setText(atividades.get(position).getHorario().getTotalHorasInvestidas() + horasGastas);
        holder.task_date.setText(horario.getData());
        holder.task_prority.setColorFilter(Color.parseColor(color));

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), convertImageToUri(atividades.get(position).getFoto()));
        } catch (IOException e) {}

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);

        holder.task_photo.setImageBitmap(bitmap);
        Picasso.with(context).load(convertImageToUri(atividades.get(position).getFoto())).transform(new CircleTransform()).resize(160, 160).into(holder.task_photo);

        holder.task_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog settingsDialog = new Dialog(context);

                View newView = inflater.inflate(R.layout.image_layout, null);

                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(newView);

                ImageView imageView = (ImageView) newView.findViewById(R.id.task_image);

                Uri image = convertImageToUri(atividades.get(position).getFoto());

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), image);
                } catch (IOException e) {}

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);

                imageView.setImageBitmap(bitmap);

                Picasso.with(context).load(image).resize(350, 350).into(imageView);

                settingsDialog.show();
            }
        });

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selecting) {
                    if (selectedActivities.contains(atividades.get(position))) {
                        selectedActivities.remove(atividades.get(position));
                        holder.card_view.setBackgroundColor(Color.parseColor("#ffffff"));
                    } else {
                        holder.card_view.setBackgroundColor(Color.parseColor("#f88794ce"));
                        selectedActivities.add(atividades.get(position));
                    }

                    AtividadeService.setAllActivities(selectedActivities);
                    selecting = !selectedActivities.isEmpty();
                }
            }
        });

        holder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!verCategorias){
                    Toast.makeText(context, atividades.get(position).getNomeDaAtv(), Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedActivities.contains(atividades.get(position))){
                        selectedActivities.remove(atividades.get(position));
                        holder.card_view.setBackgroundColor(Color.parseColor("#ffffff"));
                    } else {
                        holder.card_view.setBackgroundColor(Color.parseColor("#f88794ce"));
                        selectedActivities.add(atividades.get(position));
                    }

                    AtividadeService.setAllActivities(selectedActivities);
                    selecting = !selectedActivities.isEmpty();
                }

                return true;
            }

        });

        return rowView;
    }

    private Uri convertImageToUri(String base64Image){
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap inImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }

}