package com.dam.t08p01.vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.dam.t08p01.R;
import com.dam.t08p01.modelo.Departamento;
import com.dam.t08p01.repositorio.AuthGoogle;
import com.dam.t08p01.vista.dialogos.DlgConfirmacion;
import com.dam.t08p01.vista.fragmentos.LoginFragment;
import com.dam.t08p01.vista.fragmentos.MainFragment;
import com.dam.t08p01.vistamodelo.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements MainFragment.MainFragInterface,
        LoginFragment.LoginFragInterface,
        DlgConfirmacion.DlgConfirmacionListener {

    private DrawerLayout mDrawer;
    private final String salir = "SALIR";
    private NavController mNavC;

    private MainViewModel mMainVM;
    private Departamento mLogin;
    //Declaramos un objeto firebaseAuth
   //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inits Navigation Drawer
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(navView_OnNavigationItemSelected);

        // FindViewByIds

        // Inits
        mNavC = Navigation.findNavController(this, R.id.navhostfrag_main);
        mMainVM = new ViewModelProvider(this).get(MainViewModel.class);
        mLogin = mMainVM.getLogin();    // Recuperamos el login del ViewModel
        // Initialize Firebase Auth
        //mAuth = FirebaseAuth.getInstance();

        if (mLogin == null && savedInstanceState == null) {
            // Esta condición de savedInstanceState==null evita que ante un "giro" se ejecute estas inicializaciones de nuevo.
            // En concreto, el navigate fallaría ya que no hace falta, pues se relanza del fragmento de login solo tras un "giro"!!

            // Establecemos preferencias
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

            // Splash
            boolean mostrarSplash = pref.getBoolean(getResources().getString(R.string.splashApp_key), false);
            if (mostrarSplash) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // Lanzamos el LoginFragment
            boolean mostrarLogin = pref.getBoolean(getString(R.string.login_key), true);
            if (mostrarLogin) {
                mNavC.navigate(R.id.action_mainFragment_to_loginFragment);
            }
        }

        // Listeners

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menuSalir:
                mostrarDlgSalir();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarDlgSalir() {
        //salir firebase
        new AuthGoogle(MainActivity.this).logout();
        // Lanzamos DlgConfirmacion
        Bundle bundle = new Bundle();
        bundle.putInt("titulo", R.string.app_name);
        bundle.putInt("mensaje", R.string.msg_DlgConfirmacion_Salir);
        DlgConfirmacion dlg= new DlgConfirmacion();
        dlg.setTitulo(R.string.app_name);
        dlg.setMensaje(R.string.msg_DlgConfirmacion_Salir);
        dlg.show(getSupportFragmentManager(),salir);
    }

    @Override
    public void onDlgConfirmacionPositiveClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDlgConfirmacionNegativeClick(DialogFragment dialog) {
        ;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (mNavC.getCurrentDestination() != null && mNavC.getCurrentDestination().getId() == R.id.mainFragment) {
                mostrarDlgSalir();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private NavigationView.OnNavigationItemSelectedListener navView_OnNavigationItemSelected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menuDptos:
                    if (mLogin != null) {
                        if (mLogin.getId() == 0) {    // admin
                            if (mNavC.getCurrentDestination() != null && mNavC.getCurrentDestination().getId() == R.id.mainFragment) {
                                Intent i = new Intent(MainActivity.this, DptosActivity.class);
                                i.putExtra("login", mLogin);
                                startActivity(i);
                            }
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), R.string.msg_LoginPermisos, Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.msg_NoLogin, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.menuIncs:
                    if (mLogin != null) {
                        Intent i = new Intent(MainActivity.this, IncsActivity.class);
                        i.putExtra("login", mLogin);
                        startActivity(i);
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.msg_NoLogin, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.menuLogin:
                    if (mNavC.getCurrentDestination() != null && mNavC.getCurrentDestination().getId() == R.id.mainFragment) {
                        // Lanzamos LoginFragment
                        mNavC.navigate(R.id.action_mainFragment_to_loginFragment);
                    }
                    break;
                case R.id.menuPreferencias:
                    if (mNavC.getCurrentDestination() != null && mNavC.getCurrentDestination().getId() == R.id.mainFragment) {
                        // Lanzamos PrefFragment
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("login", mLogin);
                        mNavC.navigate(R.id.action_mainFragment_to_prefFragment, bundle);
                    }
                    break;
            }
            mDrawer.closeDrawer(GravityCompat.START);
            return true;
        }
    };

    @Override
    public void onClickBottomNavMainFrag(int menuItem) {
        switch (menuItem) {
            case R.id.menuDptos:
                if (mLogin != null) {
                    if (mLogin.getId() == 0) {    // admin
                        if (mNavC.getCurrentDestination() != null && mNavC.getCurrentDestination().getId() == R.id.mainFragment) {
                            Intent i = new Intent(MainActivity.this, DptosActivity.class);
                            i.putExtra("login", mLogin);
                            startActivity(i);
                        }
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.msg_LoginPermisos, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_NoLogin, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.menuIncs:

                if (mLogin != null) {
                    Intent i = new Intent(MainActivity.this, IncsActivity.class);
                    i.putExtra("login", mLogin);
                    startActivity(i);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_NoLogin, Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onCancelarLoginFrag() {
        // Cerramos LoginFragment
        mNavC.navigateUp();

        mMainVM.setLogin(mLogin);   // Guardamos el login en el ViewModel
    }

    @Override
    public void onAceptarLoginFrag(Departamento dpto) {
        // Cerramos LoginFragment


        mLogin = dpto;
        AuthGoogle au=new AuthGoogle(MainActivity.this);
        if(!au.login(mLogin.getNombre()+"@chirinos.com",mLogin.getClave()))
            au.registrarUsuario(mLogin.getNombre()+"@chirinos.com",mLogin.getClave());
        //registrarUsuario(mLogin.getNombre()+"@chirinos.com",mLogin.getClave());
        mMainVM.setLogin(mLogin);   // Guardamos el login en el ViewModel
        Snackbar.make(findViewById(android.R.id.content), R.string.msg_LoginOK, Snackbar.LENGTH_SHORT).show();
        mNavC.navigateUp();
    }
}
