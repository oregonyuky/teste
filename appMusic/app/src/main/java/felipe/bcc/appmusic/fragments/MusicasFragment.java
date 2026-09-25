package felipe.bcc.appmusic.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import felipe.bcc.appmusic.MainActivity;
import felipe.bcc.appmusic.R;
import felipe.bcc.appmusic.adapters.MusicaAdapter;
import felipe.bcc.appmusic.db.bean.Musica;
import felipe.bcc.appmusic.db.dal.MusicaDAL;

public class MusicasFragment extends Fragment {
    private ListView lv_musicas;
    private MainActivity mainActivity;

    public MusicasFragment() {
        // Requer construtor público vazio
    }

    public static MusicasFragment newInstance() {
        MusicasFragment fragment = new MusicasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicas, container, false);
        lv_musicas = view.findViewById(R.id.lv_musicas);
        lv_musicas.setOnItemClickListener((parent, itemView, position, id) -> {
            Musica musica = (Musica) parent.getItemAtPosition(position);
            String[] opcoes = {getString(R.string.alterar), getString(R.string.apagar)};
            new MaterialAlertDialogBuilder(requireContext(), R.style.AlertaCustomizado)
                    .setTitle(musica.getTitulo())
                    .setItems(opcoes, (dialog, which) -> {
                        if (which == 0) {
                            mainActivity.cadastrarMusicas(musica);
                        } else {
                            confirmarExclusao(musica);
                        }
                    })
                    .show();
        });
        carregarMusicas(view);
        return view;
    }

    private void carregarMusicas(View view) {
        filtrar("");
    }

    public void filtrar(String termo) {
        if (!isAdded() || lv_musicas == null) return;
        MusicaDAL dal = new MusicaDAL(requireContext());
        String pesquisa = termo == null ? "" : termo.trim();
        List<Musica> musicaList = pesquisa.isEmpty() ? dal.get("") : dal.pesquisar(pesquisa);
        lv_musicas.setAdapter(new MusicaAdapter(requireContext(), R.layout.musica_item_layout, musicaList));
    }

    private void confirmarExclusao(Musica musica) {
        new MaterialAlertDialogBuilder(requireContext(), R.style.AlertaCustomizado)
                .setTitle("Confirmar exclusão")
                .setMessage("Tem certeza que deseja apagar a música '" + musica.getTitulo() + "'?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    MusicaDAL dal = new MusicaDAL(requireContext());
                    if (dal.apagar(musica.getId())) {
                        filtrar(mainActivity.getPesquisaAtual());
                        Toast.makeText(requireContext(), "Música apagada!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }


}
