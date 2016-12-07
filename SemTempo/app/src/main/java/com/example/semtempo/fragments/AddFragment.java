package com.example.semtempo.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.semtempo.R;
import com.example.semtempo.controllers.FirebaseController;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.controllers.OnGetDataListener;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;
import com.example.semtempo.model.Tag;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddFragment extends Fragment {

    private final int SEND_ICON = R.drawable.ic_send_white_24dp;

    private List<Atividade> atividades;
    private List<String> ATIVIDADES;
    private ImageView high_priority;
    private ImageView medium_priority;
    private ImageView low_priority;
    private EditText spent_time;
    private EditText label_priority;
    private EditText categories_selection;
    private AutoCompleteTextView autoCompleteTextView;
    private AlertDialog levelDialog;
    private View rootView;
    private Tag tag;
    private FloatingActionButton camera_fab;
    private FloatingActionButton gallery_fab;
    private ImageView taskImage;
    private Bitmap currentImage;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add, container, false);
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setImageResource(SEND_ICON);
        addFab.setVisibility(View.VISIBLE);

        taskImage = (ImageView) rootView.findViewById(R.id.atv_photo);

        taskImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog settingsDialog = new Dialog(getActivity());

                View newView = inflater.inflate(R.layout.image_layout, null);

                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(newView);

                ImageView imageView = (ImageView) newView.findViewById(R.id.task_image);
                imageView.setImageBitmap(currentImage);

                settingsDialog.show();

            }
        });

        FloatingActionButton photoFab = (FloatingActionButton) rootView.findViewById(R.id.photo_fab);
        camera_fab = (FloatingActionButton) rootView.findViewById(R.id.camera_fab);
        gallery_fab = (FloatingActionButton) rootView.findViewById(R.id.gallery_fab);
        photoFab.bringToFront();
        photoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera_fab.getScaleX() == 0f){
                    camera_fab.animate().scaleX(1f).scaleY(1f).x(v.getX() - 140).setDuration(100);
                    gallery_fab.animate().scaleX(1f).scaleY(1f).x(v.getX() - 280).setDuration(100);
                } else {
                    gallery_fab.animate().scaleX(0f).scaleY(0f).x(v.getX()).setDuration(100);
                    camera_fab.animate().scaleX(0f).scaleY(0f).x(v.getX()).setDuration(100);
                }

            }
        });

        View.OnClickListener listenerIntent = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = null;
                switch (v.getId()){
                    case R.id.camera_fab:
                        if (ContextCompat.checkSelfPermission(getActivity(), CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA},0);
                        } else {
                            intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 0);
                        }

                        break;
                    case R.id.gallery_fab:
                        if (ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE},0);
                        } else {
                            intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select File"), 1234);
                        }

                        break;
                }
            }
        };

        camera_fab.setOnClickListener(listenerIntent);
        gallery_fab.setOnClickListener(listenerIntent);

        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.name_atv);

        setUp();
        configureAutoComplete();

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateFields()) {
                    Prioridade priority;
                    if (isSelected(high_priority)) {
                        priority = Prioridade.ALTA;
                    } else if (isSelected(medium_priority)) {
                        priority = Prioridade.MEDIA;
                    } else {
                        priority = Prioridade.BAIXA;
                    }

                    Calendar creation_date = new GregorianCalendar();

                    if (currentImage == null){
                        try {
                            currentImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse("android.resource://com.example.semtempo/drawable/doge_img"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    currentImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] bytes = baos.toByteArray();
                    String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

                    Horario horario = new Horario(Integer.parseInt(spent_time.getText().toString()), creation_date);
                    Atividade atv = new Atividade(autoCompleteTextView.getText().toString(), priority, horario, tag);
                    atv.setFoto(base64Image);

                    FirebaseController.saveActivity(UsuarioController.getInstance().getCurrentUser().getDisplayName(), atv);

                    showProgressDialog();

                }

            }
        });


        high_priority = (ImageView) rootView.findViewById(R.id.high_priority);
        medium_priority = (ImageView) rootView.findViewById(R.id.medium_priority);
        low_priority = (ImageView) rootView.findViewById(R.id.low_priority);
        high_priority.animate().rotation(180);
        medium_priority.animate().rotation(180);
        low_priority.animate().rotation(180);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.high_priority:
                        high_priority.animate().scaleX(1.3f).scaleY(1.3f).rotation(360).setDuration(200);
                        medium_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        low_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        break;
                    case R.id.medium_priority:
                        high_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        medium_priority.animate().scaleX(1.3f).scaleY(1.3f).rotation(360).setDuration(200);
                        low_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        break;
                    case R.id.low_priority:
                        high_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        medium_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        low_priority.animate().scaleX(1.3f).scaleY(1.3f).rotation(360).setDuration(200);
                        break;
                }
            }
        };

        high_priority.setOnClickListener(listener);
        medium_priority.setOnClickListener(listener);
        low_priority.setOnClickListener(listener);

        label_priority = (EditText) rootView.findViewById(R.id.label_priority);
        label_priority.setEnabled(false);

        spent_time = (EditText) rootView.findViewById(R.id.spent_hours);

        tag = Tag.SEMCATEGORIA;
        categories_selection = (EditText) rootView.findViewById(R.id.categorie_text);
        categories_selection.setText("Sem categoria");
        categories_selection.setFocusable(false);
        categories_selection.setClickable(true);

        categories_selection.setClickable(true);
        categories_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAlertDialog();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1234)
                onSelectFromGalleryResult(data);
            else if (requestCode == 0)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap img = null;
        if (data != null) {
            try {
                img = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap decoded = img;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decoded.compress(Bitmap.CompressFormat.JPEG, 100, out);
        decoded = Bitmap.createScaledBitmap(img, decoded.getWidth() - 200, decoded.getHeight()  - 200, false);
        taskImage.setImageBitmap(decoded);

        currentImage = img;
    }

    private void onCaptureImageResult(Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null){
                Bitmap img = (Bitmap) bundle.get("data");

                taskImage.setImageBitmap(img);

                currentImage = img;
            }
        }
    }

    private void initAlertDialog(){
        final CharSequence[] items = {" Sem Categoria "," Lazer "," Trabalho "};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Selecione uma categoria: ");

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {


                switch(item)
                {
                    case 0:
                        categories_selection.setText("Sem categoria");
                        tag = Tag.SEMCATEGORIA;
                        break;
                    case 1:
                        categories_selection.setText("Lazer");
                        tag = Tag.LAZER;
                        break;
                    case 2:
                        categories_selection.setText("Trabalho");
                        tag = Tag.TRABALHO;
                        break;
                }

                levelDialog.dismiss();

            }
        });

        levelDialog = builder.create();
        levelDialog.show();


    }

    private void configureAutoComplete(){
        ATIVIDADES = new ArrayList<String>();

        for (int i = 0; i < atividades.size(); i++) {
            if(!(ATIVIDADES.contains(atividades.get(i).getNomeDaAtv()))){
                ATIVIDADES.add(atividades.get(i).getNomeDaAtv());
            }
        }
    }

    private void showProgressDialog(){
        final int TIME = 1000;
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Adicionando atividade...");
        dialog.setCancelable(false);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new HomeFragment(), "NewFragmentTag");
                ft.commit();
            }
        }, TIME);
    }

    private void setUp() {
        atividades = new ArrayList<>();
        GoogleSignInAccount currentUser = UsuarioController.getInstance().getCurrentUser();

        final int TIME = 1000; //Timeout
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Carregando dados...");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseController.retrieveActivities(currentUser.getDisplayName(), new OnGetDataListener() {

            @Override
            public void onStart() {}

            @Override
            public void onSuccess(final List<Atividade> data) {
                atividades = data;
                configureAutoComplete();
                if(getActivity() != null) {
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, ATIVIDADES);
                    autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.name_atv);
                    autoCompleteTextView.setThreshold(1);
                    autoCompleteTextView.setAdapter(adapter);
                }

                dialog.dismiss();
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, TIME);

    }

    private boolean validateFields(){
        boolean fields_ok = true;
        String err_msg = "";

        if (!isSelected(high_priority) && !isSelected(medium_priority) && !isSelected(low_priority)){
            err_msg += "Selecione uma prioridade!";
            fields_ok = false;

        } if (autoCompleteTextView.getText().toString().isEmpty()) {
            if (!err_msg.equals(""))
                err_msg += "\n";
            err_msg += "Informe-nos o nome da atividade";
            fields_ok = false;

        } if (spent_time.getText().toString().isEmpty()){
            if (!err_msg.equals(""))
                err_msg += "\n";
            err_msg += "Insira um tempo válido";
            fields_ok = false;

        }

        if (!fields_ok) {
            Toast.makeText(getActivity(), err_msg, Toast.LENGTH_SHORT).show();
        }

        return fields_ok;
    }

    private boolean isSelected(ImageView priority){
        return priority.getScaleX() == 1.3f;
    }

}