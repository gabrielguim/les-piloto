package com.example.semtempo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.fragments.AddFragment;
import com.example.semtempo.fragments.CategoriesFragment;
import com.example.semtempo.fragments.HistoryFragment;
import com.example.semtempo.fragments.PreferenceFragment;
import com.example.semtempo.fragments.RankFragment;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;
import com.example.semtempo.fragments.HomeFragment;
import com.example.semtempo.model.Prioridade;
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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleApiClient mGoogleApiClient;
    private TextView email;
    private ImageView photo;
    private TextView name;

    private final int ADD_ICON = R.drawable.ic_add_white_24dp;
    private NavigationView navigationView = null;
    private Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.add_fab);

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
            System.out.println(value);
            if (value.equals("notificacao")){
                Fragment fragment1 = new AddFragment();
                callFragment(fragment1);
            }
        }else {
            HomeFragment fragment = new HomeFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "HOME_FRAGMENT");
            fragmentTransaction.commit();
        }


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
}
