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

import java.text.SimpleDateFormat;
import java.util.List;

import com.example.semtempo.controllers.model.Atividade;
import com.example.semtempo.controllers.model.Horario;
import com.example.semtempo.controllers.model.Prioridade;

public class RecentTasksAdapter extends BaseAdapter{

    private List<Atividade> atividades;
    private View rootView;
    private Context context;

    private static LayoutInflater inflater = null;

    public RecentTasksAdapter(Context context, List<Atividade> atividades, View rootView) {

        this.rootView = rootView;
        this.atividades = atividades;
        this.context = context;

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
        TextView task_date;
        TextView task_time;
        ImageView task_prority;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();

        View rowView;

        rowView = inflater.inflate(R.layout.recent_task_item, null);
        holder.task_name = (TextView) rowView.findViewById(R.id.task_name);
        holder.task_date = (TextView) rowView.findViewById(R.id.task_date);
        holder.task_time = (TextView) rowView.findViewById(R.id.task_time);
        holder.task_prority = (ImageView) rowView.findViewById(R.id.task_priority);

        String color;
        if (atividades.get(position).getPrioridade() == Prioridade.ALTA){
            color = "#f10714";
        } else if (atividades.get(position).getPrioridade() == Prioridade.MEDIA){
            color = "#ffbf00";
        } else {
            color = "#4169e1";
        }

        String horasGastas = " Hora investida";
        if (atividades.get(position).calcularTotalDeHorasInvestidas() > 1)
            horasGastas = " Horas investidas";

        List<Horario> horarios = (List<Horario>) atividades.get(position).getHorarios();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        holder.task_name.setText(atividades.get(position).getNome());
        holder.task_time.setText(atividades.get(position).calcularTotalDeHorasInvestidas() + horasGastas);
        holder.task_date.setText(format.format(horarios.get(horarios.size() - 1).getDataQueRealizou().getTime()));
        holder.task_prority.setColorFilter(Color.parseColor(color));

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, atividades.get(position).getNome(), Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }

} 