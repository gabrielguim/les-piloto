package com.example.semtempo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.fragments.AddFragment;
import com.example.semtempo.fragments.CategoriesFragment;
import com.example.semtempo.fragments.HistoryFragment;
import com.example.semtempo.fragments.RankFragment;
import com.example.semtempo.fragments.HomeFragment;
import com.example.semtempo.utils.CircleTransform;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleApiClient mGoogleApiClient;
    private TextView email;
    private ImageView photo;
    private TextView name;

    private NavigationView navigationView = null;
    private Toolbar toolbar = null;

    SharedPreferences prefs;
    public static Context contextOfApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        initUserInfor(navigationView);

        if(getIntent().getExtras() != null){
            String value = getIntent().getExtras().getString("flag");
            if (value.equals("notificacao")){
                Fragment fragment1 = new AddFragment();
                Bundle bundle = new Bundle();
                bundle.putString("flag", "notificacao");
                fragment1.setArguments(bundle);
                callFragment(fragment1);
            }
        }else {
            HomeFragment fragment = new HomeFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "HOME_FRAGMENT");
            fragmentTransaction.commit();
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(prefs.getString("notificacao", "erro").equals("erro")){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("notificacao", "ativa");
            editor.putString("checkbox", "checked");
            editor.putString("time", "08:00");
            editor.commit();
        }

        if(prefs.getString("notificacao", "erro").equals("ativa")){
            startRepeatingNotification();
        }
        contextOfApplication = getApplicationContext();
    }

    private void initUserInfor(NavigationView navigationView ) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) {
            mountView(opr);

            GoogleSignInAccount currentUser = UsuarioController.getInstance().getCurrentUser();

        }
    }

    private void mountView( OptionalPendingResult<GoogleSignInResult> opr ){
        GoogleSignInResult result = opr.get();
        View header = navigationView.getHeaderView(0);
        photo = (ImageView)header.findViewById(R.id.imageView);
        email = (TextView)header.findViewById(R.id.textView);
        name = (TextView)header.findViewById(R.id.nameUserView);

        email.setText(result.getSignInAccount().getEmail());
        name.setText(result.getSignInAccount().getDisplayName());
        photo.setImageURI(result.getSignInAccount().getPhotoUrl());
        Picasso.with(this).load(result.getSignInAccount().getPhotoUrl()).transform(new CircleTransform()).resize(160, 160).into(photo);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            callFragment(fragment);

        } else if (id == R.id.nav_rank) {
            fragment = new RankFragment();
            callFragment(fragment);

        } else if (id == R.id.nav_history) {
            fragment = new HistoryFragment();
            callFragment(fragment);

        } else if(id == R.id.nav_categories) {
            fragment = new CategoriesFragment();
            callFragment(fragment);
        }else if (id == R.id.nav_logout) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_pref) {
            Intent n = new Intent(this, PreferenciasActivity.class);
            startActivity(n);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callFragment(Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
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
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendIntent);
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}
