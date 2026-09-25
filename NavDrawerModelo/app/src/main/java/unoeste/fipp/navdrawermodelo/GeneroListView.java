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

import unoeste.fipp.navdrawermodelo.db.bean.Genero;
import unoeste.fipp.navdrawermodelo.db.dal.GeneroDAL;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneroListView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneroListView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;

    public GeneroListView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GeneroListView.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneroListView newInstance(String param1, String param2) {
        GeneroListView fragment = new GeneroListView();
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
        View view = inflater.inflate(R.layout.fragment_genero_list_view, container, false);
        listView = view.findViewById(R.id.generoListView);
        carregarGeneros();
        return view;
    }

    private void carregarGeneros() {
        ArrayList<Genero> generos = new GeneroDAL(requireContext()).get("");
        listView.setAdapter(new GeneroAdapter(generos));
    }

    private void editarGenero(Genero genero) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, CadastroGenero.editar(genero.getId()))
                .commit();
    }

    private void confirmarExclusao(Genero genero) {
        if (new GeneroDAL(requireContext()).possuiMusicas(genero.getId())) {
            Toast.makeText(requireContext(), "Exclua ou altere as músicas deste gênero primeiro", Toast.LENGTH_LONG).show();
            return;
        }
        new AlertDialog.Builder(requireContext())
                .setMessage("Excluir o gênero '" + genero.getNome() + "'?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Excluir", (dialog, which) -> {
                    boolean apagou = new GeneroDAL(requireContext()).apagar(genero.getId());
                    Toast.makeText(requireContext(), apagou ? "Gênero excluído" : "Não foi possível excluir o gênero", Toast.LENGTH_SHORT).show();
                    if (apagou) carregarGeneros();
                }).show();
    }

    private class GeneroAdapter extends ArrayAdapter<Genero> {
        GeneroAdapter(ArrayList<Genero> generos) {
            super(requireContext(), R.layout.item_genero, generos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView == null ? getLayoutInflater().inflate(R.layout.item_genero, parent, false) : convertView;
            Genero genero = getItem(position);
            int cor = genero.getCor();
            int corTexto = (Color.red(cor) * 299 + Color.green(cor) * 587 + Color.blue(cor) * 114) / 1000 < 128 ? Color.WHITE : Color.BLACK;
            item.setBackgroundColor(cor);
            TextView nome = item.findViewById(R.id.tvNomeGenero);
            nome.setText(genero.getNome());
            nome.setTextColor(corTexto);
            ((ImageButton) item.findViewById(R.id.btEditarGenero)).setOnClickListener(v -> editarGenero(genero));
            ((ImageButton) item.findViewById(R.id.btExcluirGenero)).setOnClickListener(v -> confirmarExclusao(genero));
            return item;
        }
    }
}
