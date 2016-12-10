package com.example.semtempo.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.semtempo.R;
import com.example.semtempo.adapters.AllTasksAdapter;
import com.example.semtempo.adapters.SubtitlesAdapter;
import com.example.semtempo.controllers.FirebaseController;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.controllers.OnGetDataListener;
import com.example.semtempo.services.AtividadeService;
import com.example.semtempo.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.semtempo.model.Atividade;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

public class SeeMoreFragment extends Fragment {

    private ListView allTasks;
    private View rootView;
    private List<Atividade> atividades;
    private List<Atividade> todasAtividades;
    private Spinner sort_spinner;
    private ToggleButton toggle;
    private String option;
    private LayoutInflater inflater;
    private final int VIEW_ALL_ICON = R.drawable.ic_insert_chart_white_24dp;
    private String[] chartColors = {"#66CCFF", "#667FFF", "#9966FF", "#E666FF", "#FF66CC", "#FF667F", "#FF9966",
            "#FFE666", "#CCFF66", "#7FFF66", "#66FF99", "#66FFE6", "#29B8FF", "#009CEB",
            "#FF7029", "#EB4E00", "#FFE666", "#CCFF66", "#7FFF66", "#66FF99", "#66FFE6",
            "#66CCFF", "#667FFF", "#9966FF", "#E666FF", "#FF66CC", "#FF667F", "#FF7029",
            "#EB4E00", "#29B8FF", "#009CEB"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_see_more, container, false);
        this.inflater = inflater;

        setUp();
        setUpSpinner();
        initToogleButton();
        setFab();

        return rootView;
    }


    private void initToogleButton(){
        toggle = (ToggleButton) rootView.findViewById(R.id.toogle_hist_type);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    option = "Todas as Atividades";
                } else {
                    option = "Atividades da Semana";
                }

                updateList();
            }
        });
    }

    private void updateList(){

        int TIME = 3000;
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Atualizando lista...");
        dialog.setCancelable(false);
        dialog.show();

        if (option.equals("Atividades da Semana")){
            Calendar cal = new GregorianCalendar();
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            atividades = AtividadeService.filterActivitiesByWeek(atividades, week);
        } else {
            atividades = todasAtividades;
        }

        AllTasksAdapter adapter = new AllTasksAdapter(getActivity(), atividades, rootView);
        adapter.setVerCategorias(true);
        allTasks.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(allTasks);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, TIME);
    }

    private void setFab(){
        FloatingActionButton moreFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        moreFab.setImageResource(VIEW_ALL_ICON);
        moreFab.setVisibility(View.VISIBLE);

        moreFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Atividade> allActivities = AtividadeService.getAllActivities();

                if (allActivities != null) {
                    if (!allActivities.isEmpty())
                        showChartDialog();
                    else
                        Toast.makeText(getContext(), "Por favor, selecione atividades para visualizar o relatório!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showChartDialog(){
        Dialog settingsDialog = new Dialog(getContext());

        View newView = inflater.inflate(R.layout.chart_layout, null);

        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(newView);

        TextView totalHours = (TextView) newView.findViewById(R.id.total_hours);

        final FitChart fitChart = (FitChart) newView.findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);
        fitChart.setMaxValue(100f);

        List<Atividade> allActivities = AtividadeService.getAllActivities();

        int horasTotais = AtividadeService.getTotalSpentHours(allActivities);
        totalHours.setText("Horas totais: " + String.valueOf(horasTotais));

        List<Float> perc = new ArrayList<>();
        for (int i = 0; i < allActivities.size(); i++) {
            float porcentagem = allActivities.get(i).getHorario().getTotalHorasInvestidas()/(float)horasTotais;
            perc.add(porcentagem * 100f);
        }

        List<Integer> subtitlesColors = new ArrayList<>();
        for (int i = 0; i < allActivities.size(); i++) {
            int color = Color.parseColor(chartColors[i]);
            subtitlesColors.add(color);
        }

        Collection<FitChartValue> values = new ArrayList<>();

        for (int i = 0; i < allActivities.size(); i++) {
            values.add(new FitChartValue(perc.get(i), subtitlesColors.get(i)));
        }

        fitChart.setValues(values);

        ListView subtitles = (ListView) newView.findViewById(R.id.subtitles);
        TextView perc_text =(TextView) newView.findViewById(R.id.text_perc);

        List<String> valores = new ArrayList<>();
        for (int i = 0; i < allActivities.size(); i++) {
            valores.add(allActivities.get(i).getNomeDaAtv() + " - Total de horas: " + allActivities.get(i).getHorario().getTotalHorasInvestidas());
        }

        subtitles.setAdapter(new SubtitlesAdapter(getActivity(), valores, subtitlesColors, perc, perc_text, rootView));
        subtitles.setDivider(null);
        Utils.setListViewHeightBasedOnChildren(subtitles);

        settingsDialog.show();
    }

    private void setUpSpinner(){

        sort_spinner = (Spinner) rootView.findViewById(R.id.filter_spinner);

        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                if (item.equals("Menor prioridade - Maior prioridade")){
                    Utils.sortByPriority(atividades);
                    Collections.reverse(atividades);
                } else if (item.equals("Menos horas - Mais horas")){
                    Utils.sortByHours(atividades);
                } else if (item.equals("Maior prioridade - Menor prioridade")){
                    Utils.sortByPriority(atividades);
                } else if (item.equals("Mais horas - Menos horas")) {
                    Utils.sortByHours(atividades);
                    Collections.reverse(atividades);
                } else if (item.equals("Mais recente - Menos recente")){
                    Utils.sortByDate(atividades);
                } else {
                    Utils.sortByDate(atividades);
                    Collections.reverse(atividades);
                }

                AllTasksAdapter adapter = new AllTasksAdapter(getActivity(), atividades, rootView);
                adapter.setVerCategorias(true);
                allTasks.setAdapter(adapter);
                Utils.setListViewHeightBasedOnChildren(allTasks);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("Maior prioridade - Menor prioridade");
        categories.add("Menor prioridade - Maior prioridade");
        categories.add("Menos horas - Mais horas");
        categories.add("Mais horas - Menos horas");
        categories.add("Mais recente - Menos recente");
        categories.add("Menos recente - Mais recente");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sort_spinner.setAdapter(dataAdapter);
    }

    private void setUp() {
        int TIME = 3000;
        allTasks = (ListView) rootView.findViewById(R.id.all_tasks);
        allTasks.setDivider(null);
        atividades = new ArrayList<>();
        todasAtividades = new ArrayList<>();


        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Carregando dados...");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseController.retrieveActivities(UsuarioController.getInstance().getCurrentUser().getDisplayName(), new OnGetDataListener() {
            @Override
            public void onStart() {}

            @Override
            public void onSuccess(final List<Atividade> data) {
                atividades = data;
                todasAtividades = data;

                if (getActivity() != null) {
                    AllTasksAdapter adapter = new AllTasksAdapter(getActivity(), atividades, rootView);
                    adapter.setVerCategorias(true);
                    allTasks.setAdapter(adapter);
                    Utils.setListViewHeightBasedOnChildren(allTasks);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();

            }
        }, TIME);

    }


}