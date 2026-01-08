package com.example.moviematch;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.moviematch.datos.modelo.Pelicula;
import com.example.moviematch.datos.preferencias.GestorPreferenciasUsuario;
import com.example.moviematch.ui.bienvenida.BienvenidaFragment;
import com.example.moviematch.ui.detalle.DetallePeliculaFragment;
import com.example.moviematch.ui.inicio.InicioFragment;
import com.example.moviematch.ui.modogrupo.ModoGrupoFragment;
import com.example.moviematch.ui.onboarding.OnboardingFragment;
import com.example.moviematch.ui.recomendaciones.RecomendacionesFragment;
import com.example.moviematch.ui.verdespues.VerDespuesFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class ActividadPrincipal extends AppCompatActivity {

    private GestorPreferenciasUsuario gestorPreferenciasUsuario;
    private MaterialToolbar topAppBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        gestorPreferenciasUsuario = new GestorPreferenciasUsuario(this);

        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        if (topAppBar != null) {
            topAppBar.setTitle("");
            setSupportActionBar(topAppBar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        configurarDrawer();
        aplicarInsets();

        if (savedInstanceState == null) {
            irABienvenida();
        }
    }

    private void configurarDrawer() {
        if (drawerLayout == null || navigationView == null || topAppBar == null) {
            return;
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                topAppBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            manejarNavegacion(item.getItemId());
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void aplicarInsets() {
        View root = findViewById(R.id.rootMain);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    private void manejarNavegacion(int itemId) {
        if (itemId == R.id.nav_welcome) {
            irABienvenida();
        } else if (itemId == R.id.nav_home) {
            irAInicio();
        } else if (itemId == R.id.nav_onboarding) {
            irAOnboarding();
        } else if (itemId == R.id.nav_watchlist) {
            irAVerDespues();
        } else if (itemId == R.id.nav_group) {
            irAModoGrupo();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_watchlist) {
            irAVerDespues();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public GestorPreferenciasUsuario obtenerGestorPreferenciasUsuario() {
        return gestorPreferenciasUsuario;
    }

    public void irABienvenida() {
        resaltarMenu(R.id.nav_welcome);
        mostrarFragmento(new BienvenidaFragment(), false);
    }

    public void irAOnboarding() {
        resaltarMenu(R.id.nav_onboarding);
        mostrarFragmento(new OnboardingFragment(), false);
    }

    public void irAInicio() {
        resaltarMenu(R.id.nav_home);
        mostrarFragmento(new InicioFragment(), false);
    }

    public void irARecomendaciones(String mood, Integer duracionMaxMinutos, String compania) {
        RecomendacionesFragment fragment = RecomendacionesFragment.nuevaInstancia(mood, duracionMaxMinutos, compania);
        mostrarFragmento(fragment, true);
    }

    public void irADetalle(Pelicula pelicula) {
        DetallePeliculaFragment fragment = DetallePeliculaFragment.nuevaInstancia(pelicula);
        mostrarFragmento(fragment, true);
    }

    public void irAVerDespues() {
        resaltarMenu(R.id.nav_watchlist);
        mostrarFragmento(new VerDespuesFragment(), true);
    }

    public void irAModoGrupo() {
        resaltarMenu(R.id.nav_group);
        mostrarFragmento(new ModoGrupoFragment(), true);
    }

    private void resaltarMenu(int itemId) {
        if (navigationView != null) {
            navigationView.setCheckedItem(itemId);
        }
    }

    private void mostrarFragmento(Fragment fragment, boolean agregarAStack) {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if (agregarAStack) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
