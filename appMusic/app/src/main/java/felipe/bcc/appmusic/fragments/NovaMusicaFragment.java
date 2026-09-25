package felipe.bcc.appmusic.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import felipe.bcc.appmusic.R;
import felipe.bcc.appmusic.adapters.GeneroAdapter;
import felipe.bcc.appmusic.db.bean.Genero;
import felipe.bcc.appmusic.db.bean.Musica;
import felipe.bcc.appmusic.db.dal.GeneroDAL;
import felipe.bcc.appmusic.db.dal.MusicaDAL;

public class NovaMusicaFragment extends Fragment {
    private Button btConfirma;
    public static Musica musica=null;
    private EditText etnMinutos;
    private EditText etnSegundos;
    private EditText etnAno;
    private TextInputEditText tiInterprete;
    private TextInputEditText tiTitulo;
    private Spinner sp_generos;

    private MusicaDAL musicaDAL;

    public NovaMusicaFragment() {
        // Requer construtor público vazio
    }
    public static NovaMusicaFragment newInstance(String param1, String param2) {
        NovaMusicaFragment fragment = new NovaMusicaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nova_musica, container, false);

        sp_generos = view.findViewById(R.id.sp_generos);
        btConfirma = view.findViewById(R.id.btConfirma);
        btConfirma.setText(musica != null ? "Alterar" : "Cadastrar");

        tiInterprete = view.findViewById(R.id.tiInterprete);
        tiTitulo = view.findViewById(R.id.tiTitulo);
        etnMinutos = view.findViewById(R.id.etnMinutos);
        etnSegundos = view.findViewById(R.id.etnSegundos);
        etnAno = view.findViewById(R.id.etnAno);
        musicaDAL = new MusicaDAL(view.getContext());

        btConfirma.setOnClickListener(v -> {
            try {
                String titulo = tiTitulo.getText().toString().trim();
                String interprete = tiInterprete.getText().toString().trim();

                int ano = Integer.parseInt(etnAno.getText().toString().trim());
                int minutos = Integer.parseInt(etnMinutos.getText().toString().trim());
                int segundos = Integer.parseInt(etnSegundos.getText().toString().trim());
                if (minutos > 59) {
                    Snackbar.make(view, "Os minutos devem estar entre 0 e 59.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (segundos > 59) {
                    Snackbar.make(view, "Os segundos devem estar entre 0 e 59.", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                double duracao = minutos + (segundos / 100.0);
                Genero genero = (Genero) sp_generos.getSelectedItem();
                if (titulo.isEmpty() || interprete.isEmpty() || genero == null) {
                    Snackbar.make(view, "Preencha todos os campos.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Musica nova = new Musica(ano, titulo, interprete, genero, duracao);

                if (NovaMusicaFragment.musica != null) {
                    nova.setId(musica.getId());
                    if (musicaDAL.alterar(nova)) {
                        Snackbar.make(view, "Música alterada com sucesso!", Snackbar.LENGTH_SHORT).show();
                        NovaMusicaFragment.musica = null;
                        limparCampos();
                    }
                } else {
                    if (musicaDAL.salvar(nova)) {
                        Snackbar.make(view, "Música salva com sucesso!", Snackbar.LENGTH_SHORT).show();
                        NovaMusicaFragment.musica = null;
                        limparCampos();
                    }
                }

            } catch (NumberFormatException e) {
                Snackbar.make(view, "Ano e duração devem ser números válidos.", Snackbar.LENGTH_SHORT).show();
            }
        });

        carregarGeneros(view); // carrega o adapter, mas não chama addValores() aqui
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // só depois que a tela está visível e o spinner populado
        addValores();
    }

    private void addValores() {
        SpinnerAdapter adapter = sp_generos.getAdapter();
        if (adapter == null) {
            return;
        }

        if (musica != null) {
            tiTitulo.setText(musica.getTitulo());
            tiInterprete.setText(musica.getInterprete());
            etnAno.setText(String.valueOf(musica.getAno()));
            int minutos = (int) musica.getDuracao();
            int segundos = (int) Math.round((musica.getDuracao() - minutos) * 100);
            etnMinutos.setText(String.valueOf(minutos));
            etnSegundos.setText(String.valueOf(segundos));

            int i;
            for (i = 0; i < adapter.getCount()
                    && ((Genero) adapter.getItem(i)).getId() != musica.getGenero().getId(); i++);

            if (i < adapter.getCount()) {
                sp_generos.setSelection(i);
            }
        } else {
            limparCampos();
        }
    }

    private void limparCampos() {
        tiTitulo.setText("");
        tiInterprete.setText("");
        etnAno.setText("");
        etnMinutos.setText("");
        etnSegundos.setText("");
        if (sp_generos.getAdapter() != null && sp_generos.getAdapter().getCount() > 0) {
            sp_generos.setSelection(0);
        }
    }

    private void carregarGeneros(View view) {
        GeneroDAL dal = new GeneroDAL(view.getContext());
        List<Genero> generoList =  dal.get("");

        GeneroAdapter adapter = new GeneroAdapter(requireContext(), R.layout.genero_item_layout, generoList);
        adapter.setDropDownViewResource(R.layout.genero_item_layout);
        sp_generos.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        musica=null;
    }
}
