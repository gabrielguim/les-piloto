package com.example.semtempo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.semtempo.fragments.AddFragment;
import com.example.semtempo.fragments.HomeFragment;

import static android.R.attr.fragment;

public class PreferenciasActivity extends AppCompatActivity {

    private final int ADD_ICON = R.drawable.ic_add_white_24dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(0);

        if(getIntent().getExtras() != null){
            String value = getIntent().getExtras().getString("flag");
            if (value == "notificacao"){
                AddFragment fragment = new AddFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "Add_FRAGMENT");
                fragmentTransaction.commit();
            }
        }


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification(view);
                finish();
            }
        });

    }

    public void notification(View view){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_textsms_black_24dp)
                        .setContentTitle("Registre seu tempo investido!")
                        .setContentText("Você não registrou nenhuma atividade ontem. Clique para registrar.")
                        .setAutoCancel(true);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("Você não registrou nenhuma atividade ontem. Clique para registrar."));

        Intent resultIntent = new Intent(this, MainActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("flag", "notificacao");
        resultIntent.putExtras(mBundle);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT, mBundle);
        mBuilder.setContentIntent(resultPendingIntent);

        Intent preferencias = new Intent(this, PreferenciasActivity.class);


        PendingIntent prefs = PendingIntent.getActivity(this, 0, preferencias, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_menu_manage, "Alterar preferências", prefs));

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification not = mBuilder.build();
        not.vibrate =  new long[]{150, 300, 150, 600};
        not.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(0, not);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }catch(Exception e){}
    }

    private void callFragment(Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void startNotManager(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
