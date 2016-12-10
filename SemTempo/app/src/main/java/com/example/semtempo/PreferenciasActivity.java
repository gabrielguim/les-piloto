package com.example.semtempo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.semtempo.fragments.AddFragment;

import java.util.Calendar;

public class PreferenciasActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private EditText notificationHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(0);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        notificationHour = (EditText) findViewById(R.id.notification_hour);
        notificationHour.setText(prefs.getString("time", "08:00"));

        notificationHour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PreferenciasActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedMinuteString = String.valueOf(selectedMinute);
                        String selectedHourString = String.valueOf(selectedHour);

                        if (selectedHour < 10)
                            selectedHourString = "0" + selectedHour;
                        if (selectedMinute < 10)
                            selectedMinuteString = "0" + selectedMinute;

                        notificationHour.setText(selectedHourString + ":" + selectedMinuteString);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("time", selectedHourString + ":" + selectedMinuteString);
                        editor.commit();

                        Toast.makeText(PreferenciasActivity.this, "Notificação alterada para as " + selectedHourString + ":" + selectedMinuteString, Toast.LENGTH_SHORT).show();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();

            }
        });

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

        final CheckBox satView = (CheckBox)findViewById(R.id.notification_check);
        if(prefs.getString("checkbox", "erro").equals("checked")){
            satView.setChecked(true);
        }else {
            satView.setChecked(false);
        }

        satView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(satView.isChecked()){
                    Toast.makeText(PreferenciasActivity.this, "Notificação ativada", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("notificacao", "ativa");
                    editor.putString("checkbox", "checked");
                    editor.commit();
                    startRepeatingNotification();
                }else{
                    Toast.makeText(PreferenciasActivity.this, "Notificação desativada", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("notificacao", "desativa");
                    editor.putString("checkbox", "unchecked");
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
                    PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.cancel(sender);
                }
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

    public void startRepeatingNotification(){
        int hourNotification = Integer.parseInt(prefs.getString("time", "08:00").split(":")[0]);
        int minuteNotification = Integer.parseInt(prefs.getString("time", "08:00").split(":")[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourNotification);
        calendar.set(Calendar.MINUTE, minuteNotification);
        calendar.set(calendar.SECOND, 10);
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendIntent);
    }

}
