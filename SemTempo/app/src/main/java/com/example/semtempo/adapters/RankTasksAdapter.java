package com.example.semtempo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semtempo.R;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;

import java.util.List;

public class RankTasksAdapter extends BaseAdapter{

    private List<Atividade> atividades;
    private View rootView;
    private Context context;

    private static LayoutInflater inflater = null;

    public RankTasksAdapter(Context context, List<Atividade> atividades, View rootView) {

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
        TextView task_time;
        TextView task_date;
        TextView task_rank;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();

        View rowView;

        rowView = inflater.inflate(R.layout.rank_item, null);
        holder.task_name = (TextView) rowView.findViewById(R.id.task_name);
        holder.task_date = (TextView) rowView.findViewById(R.id.task_date);
        holder.task_time = (TextView) rowView.findViewById(R.id.task_time);
        holder.task_rank = (TextView) rowView.findViewById(R.id.task_rank);

        if (position == 0){
            holder.task_rank.animate().scaleX(1.2f).scaleY(1.2f);
        } else if (position == 1){
            holder.task_rank.animate().scaleX(1.1f).scaleY(1.1f);
        } else {
            holder.task_rank.animate().scaleX(1f).scaleY(1f);
        }

        String horasGastas = " Hora investida";

        if (atividades.get(position).getHorariosRealizDaAtv().getTotalHorasInvestidas() > 1)
            horasGastas = " Horas investidas";

        Horario horario = atividades.get(position).getHorariosRealizDaAtv();

        holder.task_name.setText(atividades.get(position).getNomeDaAtv());
        holder.task_time.setText(atividades.get(position).getHorariosRealizDaAtv().getTotalHorasInvestidas() + horasGastas);
        holder.task_date.setText(horario.getData());
        holder.task_rank.setText(position + 1 + "º");

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, atividades.get(position).getNomeDaAtv(), Toast.LENGTH_SHORT).show();

                return false;
            }

        });

        return rowView;
    }

}