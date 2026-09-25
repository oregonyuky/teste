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

import java.util.ArrayList;
import java.util.List;

import felipe.bcc.appmusic.MainActivity;
import felipe.bcc.appmusic.R;
import felipe.bcc.appmusic.adapters.GeneroAdapter;
import felipe.bcc.appmusic.db.bean.Genero;
import felipe.bcc.appmusic.db.bean.Musica;
import felipe.bcc.appmusic.db.dal.GeneroDAL;
import felipe.bcc.appmusic.db.dal.MusicaDAL;

public class GenerosFragment extends Fragment {
    private ListView  lv_generos;
    private MainActivity mainActivity;

    public GenerosFragment() {
        // Requer construtor público vazio
    }

    public static GenerosFragment newInstance() {
        GenerosFragment fragment = new GenerosFragment();
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
        View view =  inflater.inflate(R.layout.fragment_generos, container, false);
        lv_generos = view.findViewById(R.id.lv_generos);
        lv_generos.setOnItemClickListener((parent, itemView, position, id) -> {
            Genero genero = (Genero) parent.getItemAtPosition(position);
            String[] opcoes = {getString(R.string.alterar), getString(R.string.apagar)};
            new MaterialAlertDialogBuilder(requireContext(), R.style.AlertaCustomizado)
                    .setTitle(genero.getNome())
                    .setItems(opcoes, (dialog, which) -> {
                        if (which == 0) {
                            mainActivity.cadastrarGenero(genero);
                        } else {
                            apagarGenero(genero, view);
                        }
                    })
                    .show();
        });
        lv_generos.setOnItemLongClickListener((adapterView, view1, i, l) -> {
            Genero genero = (Genero) adapterView.getItemAtPosition(i);

            new MaterialAlertDialogBuilder(getContext(), R.style.AlertaCustomizado)
                    .setTitle("Confirmar Exclusão")
                    .setMessage("Tem certeza que deseja apagar o gênero '" + genero.getNome() + "'?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        MusicaDAL musicaDAL = new MusicaDAL(view.getContext());
                        ArrayList<Musica> result = musicaDAL.get("mus_genero = " + genero.getId());
                        if (!result.isEmpty())
                            Toast.makeText(getContext(), "Não foi possível apagar o gênero. Existem músicas cadastradas!", Toast.LENGTH_SHORT).show();
                        else {
                            GeneroDAL dal = new GeneroDAL(view.getContext());
                            dal.apagar(genero.getId());
                            carregarGeneros(view);
                            Toast.makeText(getContext(), "Gênero apagado!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
            return true;
        });
        carregarGeneros(view);
        return view;
    }

    private void carregarGeneros(View view) {
        GeneroDAL dal = new GeneroDAL(view.getContext());
        List<Genero> generoList =  dal.get("");
        lv_generos.setAdapter(new GeneroAdapter(view.getContext(), R.layout.genero_item_layout, generoList));
    }

    private void apagarGenero(Genero genero, View view) {
        new MaterialAlertDialogBuilder(requireContext(), R.style.AlertaCustomizado)
                .setTitle("Confirmar exclusão")
                .setMessage("Tem certeza que deseja apagar o gênero '" + genero.getNome() + "'?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    MusicaDAL musicaDAL = new MusicaDAL(requireContext());
                    ArrayList<Musica> musicas = musicaDAL.get("mus_genero = " + genero.getId());
                    if (!musicas.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Não foi possível apagar: existem músicas deste gênero.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        new GeneroDAL(requireContext()).apagar(genero.getId());
                        carregarGeneros(view);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }
}
