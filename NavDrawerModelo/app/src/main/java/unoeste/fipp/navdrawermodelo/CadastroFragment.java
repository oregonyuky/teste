package unoeste.fipp.navdrawermodelo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import unoeste.fipp.navdrawermodelo.db.bean.Genero;
import unoeste.fipp.navdrawermodelo.db.bean.Musica;
import unoeste.fipp.navdrawermodelo.db.dal.GeneroDAL;
import unoeste.fipp.navdrawermodelo.db.dal.MusicaDAL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CadastroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CadastroFragment extends Fragment {
    private EditText etTitulo, etAutor, etAno, etDuracao;
    private Button btCadastrar;
    private Spinner spinner;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_MUSICA_ID = "musica_id";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int musicaId;

    public CadastroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CadastroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CadastroFragment newInstance(String param1, String param2) {
        CadastroFragment fragment = new CadastroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CadastroFragment editar(int musicaId) {
        CadastroFragment fragment = new CadastroFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MUSICA_ID, musicaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            musicaId = getArguments().getInt(ARG_MUSICA_ID, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cadastro_music, container, false);
        etTitulo =view.findViewById(R.id.etTitulo);
        etAutor =view.findViewById(R.id.etAutor);
        etAno = view.findViewById(R.id.etAno);
        etDuracao = view.findViewById(R.id.etDuracao);
        spinner=view.findViewById(R.id.spinner);
        btCadastrar=view.findViewById(R.id.btCadastrar);
        btCadastrar.setOnClickListener(e->cadastar());
        ArrayAdapter<Genero> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new GeneroDAL(requireContext()).get("")
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);
        if (musicaId != 0) {
            preencherParaEdicao();
        }
        return view;
    }

    private void cadastar() {
        MusicaDAL musicaDAL = new MusicaDAL (this.getContext());
        Musica musica = new Musica(
                musicaId,
                Integer.parseInt(etAno.getText().toString()),
                etTitulo.getText().toString(),
                etAutor.getText().toString(),
                (Genero) spinner.getSelectedItem(),
                Float.parseFloat(etDuracao.getText().toString())
        );
        boolean salvo = musicaId == 0 ? musicaDAL.salvar(musica) : musicaDAL.alterar(musica);
        if (salvo) {
            mostrarFragment(new MusicListView());
        }
    }

    private void preencherParaEdicao() {
        Musica musica = new MusicaDAL(requireContext()).get(musicaId);
        if (musica == null) return;
        etTitulo.setText(musica.getTitulo());
        etAutor.setText(musica.getAutor());
        etAno.setText(String.valueOf(musica.getAno()));
        etDuracao.setText(String.valueOf(musica.getDuracao()));
        for (int i = 0; i < spinner.getCount(); i++) {
            Genero genero = (Genero) spinner.getItemAtPosition(i);
            if (genero.getId() == musica.getGenero().getId()) {
                spinner.setSelection(i);
                break;
            }
        }
        btCadastrar.setText("Salvar alterações");
    }


    private void mostrarFragment(Fragment fragment) {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

}
