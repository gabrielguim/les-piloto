package com.example.semtempo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.semtempo.R;
import com.example.semtempo.adapters.RankTasksAdapter;
import com.example.semtempo.controllers.FirebaseController;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.controllers.OnGetDataListener;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.services.AtividadeService;
import com.example.semtempo.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class RankFragment extends Fragment {

    private final int ADD_ICON = R.drawable.ic_add_white_24dp;
    private View rootView;
    private List<Atividade> atividadesRank;
    private ListView rankList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_rank, container, false);

        setFab();
        setUp();

        return rootView;
    }

    private void setFab(){
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setImageResource(ADD_ICON);
        addFab.setVisibility(View.VISIBLE);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new AddFragment(), "NewFragmentTag");
                ft.commit();
            }
        });
    }


    private void setUp() {
        rankList = (ListView) rootView.findViewById(R.id.rank_tasks);
        rankList.setDivider(null);
        atividadesRank = new ArrayList<>();

        final int TIME = 3000;

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Carregando dados...");
        dialog.setCancelable(false);
        dialog.show();

        int limit = 10;

        FirebaseController.retrieveActivities(UsuarioController.getInstance().getCurrentUser().getDisplayName(), new OnGetDataListener() {
            @Override
            public void onStart() {}

            @Override
            public void onSuccess(final List<Atividade> data) {
                dialog.dismiss();

                int semanaAtual = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
                atividadesRank = AtividadeService.filterActivitiesByWeek(data, semanaAtual);

                Utils.sortByHours(atividadesRank);
                Collections.reverse(atividadesRank);

                if (getActivity() != null) {
                    rankList.setAdapter(new RankTasksAdapter(getActivity(), atividadesRank, rootView));
                    Utils.setListViewHeightBasedOnChildren(rankList);
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