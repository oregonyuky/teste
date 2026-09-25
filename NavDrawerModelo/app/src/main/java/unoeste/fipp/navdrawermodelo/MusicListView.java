package unoeste.fipp.navdrawermodelo;

import android.os.Bundle;
import android.app.AlertDialog;
import android.graphics.Color;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import unoeste.fipp.navdrawermodelo.db.bean.Musica;
import unoeste.fipp.navdrawermodelo.db.dal.MusicaDAL;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicListView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicListView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private String filtroAtual = "";

    public MusicListView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicListView.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicListView newInstance(String param1, String param2) {
        MusicListView fragment = new MusicListView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list_view, container, false);
        listView = view.findViewById(R.id.musicListView);
        carregarMusicas();
        return view;
    }

    public void filtrar(String termo) {
        filtroAtual = termo == null ? "" : termo.trim();
        if (listView != null) {
            carregarMusicas();
        }
    }

    private void carregarMusicas() {
        MusicaDAL dal = new MusicaDAL(requireContext());
        ArrayList<Musica> musicas = filtroAtual.isEmpty()
                ? dal.get("")
                : dal.buscarPorTituloOuInterprete(filtroAtual);
        listView.setAdapter(new MusicaAdapter(musicas));
    }

    private void editarMusica(Musica musica) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, CadastroFragment.editar(musica.getId()))
                .commit();
    }

    private void confirmarExclusao(Musica musica) {
        new AlertDialog.Builder(requireContext())
                .setMessage("Excluir a música '" + musica.getTitulo() + "'?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Excluir", (dialog, which) -> {
                    boolean apagou = new MusicaDAL(requireContext()).apagar(musica.getId());
                    Toast.makeText(requireContext(), apagou ? "Música excluída" : "Não foi possível excluir a música", Toast.LENGTH_SHORT).show();
                    if (apagou) {
                        carregarMusicas();
                    }
                })
                .show();
    }

    private class MusicaAdapter extends ArrayAdapter<Musica> {
        MusicaAdapter(ArrayList<Musica> musicas) {
            super(requireContext(), R.layout.item_music, musicas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView == null
                    ? getLayoutInflater().inflate(R.layout.item_music, parent, false)
                    : convertView;
            Musica musica = getItem(position);
            int cor = musica.getGenero().getCor();
            int corTexto = (Color.red(cor) * 299 + Color.green(cor) * 587 + Color.blue(cor) * 114) / 1000 < 128
                    ? Color.WHITE : Color.BLACK;

            item.setBackgroundColor(cor);
            ((TextView) item.findViewById(R.id.tvTituloMusica)).setText(musica.getTitulo());
            TextView dados = item.findViewById(R.id.tvDadosMusica);
            dados.setText(musica.getAutor() + " • " + musica.getAno() + " • " + musica.getDuracao() + " min • " + musica.getGenero().getNome());
            ((TextView) item.findViewById(R.id.tvTituloMusica)).setTextColor(corTexto);
            dados.setTextColor(corTexto);
            ImageButton editar = item.findViewById(R.id.btEditarMusica);
            ImageButton excluir = item.findViewById(R.id.btExcluirMusica);
            editar.setOnClickListener(v -> editarMusica(musica));
            excluir.setOnClickListener(v -> confirmarExclusao(musica));
            return item;
        }
    }
}
