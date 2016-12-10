package com.example.semtempo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.example.semtempo.controllers.FirebaseController;
import com.example.semtempo.controllers.OnGetDataListener;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.services.AtividadeService;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelvin on 04-Dec-16.
 */

public class NotificationReceiver extends BroadcastReceiver {

    private ArrayList<Atividade> atividades;
    private boolean registrouAtividadeOntem;
    private SharedPreferences prefes;

    @Override
    public void onReceive(Context context, Intent intent){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_textsms_black_24dp)
                        .setContentTitle("Registre seu tempo investido!")
                        .setContentText("Você não registrou nenhuma atividade ontem. Clique para registrar.")
                        .setAutoCancel(true);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("Você não registrou nenhuma atividade ontem. Clique para registrar."));

        Intent resultIntent = new Intent(context, MainActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("flag", "notificacao");
        resultIntent.putExtras(mBundle);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 100, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT, mBundle);
        mBuilder.setContentIntent(resultPendingIntent);

        Intent preferencias = new Intent(context, PreferenciasActivity.class);


        PendingIntent prefs = PendingIntent.getActivity(context, 100, preferencias, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_menu_manage, "Alterar preferências", prefs));

        Notification not = mBuilder.build();
        not.vibrate =  new long[]{150, 300, 150, 600};
        not.flags = Notification.FLAG_AUTO_CANCEL;

        Context applicationContext = MainActivity.getContextOfApplication();
        prefes = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        System.out.println(prefes.getString("ontem", "false"));
        if (prefes.getString("ontem", "false").equals("false")) {
            mNotificationManager.notify(100, not);
            try {
                Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone toque = RingtoneManager.getRingtone(context, som);
                toque.play();
            } catch (Exception e) {
            }
        }
    }

}
