package unoeste.fipp.navdrawermodelo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public static List<String> peixesList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_menu,
                R.string.close_menu);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mostrarFragment(new MusicListView());
        navigationView.setNavigationItemSelectedListener(item-> {
            if(item.getItemId() == R.id.imCadastrarMusica) {
                mostrarFragment(new CadastroFragment());
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            if(item.getItemId() == R.id.imVisualizarMusicas){
                mostrarFragment(new MusicListView());
                drawerLayout.closeDrawer(GravityCompat.START);
            }


            if(item.getItemId() == R.id.imCadastrarGenero){
                mostrarFragment(new CadastroGenero());
                drawerLayout.closeDrawer(GravityCompat.START);
            }

            if (item.getItemId() == R.id.imVisualizarGenero){
                mostrarFragment(new GeneroListView());
                drawerLayout.closeDrawer(GravityCompat.START);
            }


            if(item.getItemId()== R.id.imFinalizar)
                    finish();
            return true;
        });
    }

    private void mostrarFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_busca, menu);
        SearchView busca = (SearchView) menu.findItem(R.id.imPesquisarMusicas).getActionView();
        busca.setQueryHint("Título ou cantor");
        busca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarMusicas(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String novoTexto) {
                filtrarMusicas(novoTexto);
                return true;
            }
        });
        return true;
    }

    private void filtrarMusicas(String texto) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment instanceof MusicListView) {
            ((MusicListView) fragment).filtrar(texto);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
