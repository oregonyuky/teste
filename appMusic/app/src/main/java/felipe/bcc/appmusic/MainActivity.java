package felipe.bcc.appmusic;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import felipe.bcc.appmusic.db.bean.Musica;
import felipe.bcc.appmusic.db.bean.Genero;
import felipe.bcc.appmusic.fragments.GenerosFragment;
import felipe.bcc.appmusic.fragments.MusicasFragment;
import felipe.bcc.appmusic.fragments.NovaMusicaFragment;
import felipe.bcc.appmusic.fragments.NovoGeneroFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private boolean opcaoSelecionada;
    private String pesquisaAtual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_menu, R.string.close_menu);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(@NonNull android.view.View drawerView) {
                if (!opcaoSelecionada) {
                    mostrarMusicas();
                }
                opcaoSelecionada = false;
            }
        });
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.it_lmusicas);
            mostrarMusicas();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        opcaoSelecionada = true;
        int id = item.getItemId();
        if (id == R.id.it_nmusica) {
            NovaMusicaFragment.musica = null;
            showFragment(new NovaMusicaFragment(), getString(R.string.nova_musica));
        } else if (id == R.id.it_ngenero) {
            showFragment(NovoGeneroFragment.novo(null), getString(R.string.novo_genero));
        } else if (id == R.id.it_lmusicas) {
            mostrarMusicas();
        } else if (id == R.id.it_lgeneros) {
            showFragment(new GenerosFragment(), getString(R.string.generos));
        } else if (id == R.id.it_fechar) {
            finishAffinity();
            return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cadastrarMusicas(Musica musica) {
        NovaMusicaFragment.musica = musica;
        showFragment(new NovaMusicaFragment(), getString(R.string.editar_musica));
    }

    public void cadastrarGenero(Genero genero) {
        showFragment(NovoGeneroFragment.novo(genero), getString(R.string.editar_genero));
    }

    public void mostrarMusicas() {
        MusicasFragment fragment = new MusicasFragment();
        showFragment(fragment, getString(R.string.musicas));
        getSupportFragmentManager().executePendingTransactions();
        fragment.filtrar(pesquisaAtual);
    }

    public void mostrarGeneros() {
        showFragment(new GenerosFragment(), getString(R.string.generos));
    }

    public String getPesquisaAtual() {
        return pesquisaAtual;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.pesquisar_musicas));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    aplicarPesquisa(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    aplicarPesquisa(newText);
                    return true;
                }
            });
        }
        return true;
    }

    private void aplicarPesquisa(String termo) {
        pesquisaAtual = termo == null ? "" : termo;
        Fragment atual = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (atual instanceof MusicasFragment) {
            ((MusicasFragment) atual).filtrar(pesquisaAtual);
        } else if (!pesquisaAtual.isEmpty()) {
            mostrarMusicas();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_close) {
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        setTitle(title);
    }

}
