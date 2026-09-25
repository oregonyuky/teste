package unoeste.fipp.navdrawermodelo;

import android.os.Bundle;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import unoeste.fipp.navdrawermodelo.db.bean.Genero;
import unoeste.fipp.navdrawermodelo.db.dal.GeneroDAL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CadastroGenero#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CadastroGenero extends Fragment {
    private static final int[] CORES_PRIMARIAS = {
            Color.RED,
            Color.YELLOW,
            Color.BLUE,
            Color.GREEN,
            0xFFFF9800, // Laranja
            0xFF9C27B0, // Roxo
            0xFFE91E63, // Rosa
            Color.BLACK,
            Color.WHITE,
            Color.GRAY
    };

    private EditText etNomeGenero;
    private Button btCadastrarGenero;
    private Spinner spCorGenero;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_GENERO_ID = "genero_id";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int generoId;

    public CadastroGenero() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CadastroGenero.
     */
    // TODO: Rename and change types and number of parameters
    public static CadastroGenero newInstance(String param1, String param2) {
        CadastroGenero fragment = new CadastroGenero();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CadastroGenero editar(int generoId) {
        CadastroGenero fragment = new CadastroGenero();
        Bundle args = new Bundle();
        args.putInt(ARG_GENERO_ID, generoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            generoId = getArguments().getInt(ARG_GENERO_ID, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_genero, container, false);
        etNomeGenero = view.findViewById(R.id.etNomeGenero);
        spCorGenero = view.findViewById(R.id.spCorGenero);
        btCadastrarGenero = view.findViewById(R.id.btCadastrarGenero);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.cores_primarias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCorGenero.setAdapter(adapter);
        if (generoId != 0) {
            preencherParaEdicao();
        }

        btCadastrarGenero.setOnClickListener(v -> cadastrarGenero());
        return view;
    }

    private void cadastrarGenero() {
        String nome = etNomeGenero.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            etNomeGenero.setError("Informe o nome do gênero");
            etNomeGenero.requestFocus();
            return;
        }

        int cor = CORES_PRIMARIAS[spCorGenero.getSelectedItemPosition()];

        Genero genero = new Genero(generoId, nome, cor);
        boolean salvo = generoId == 0
                ? new GeneroDAL(requireContext()).salvar(genero)
                : new GeneroDAL(requireContext()).alterar(genero);
        if (salvo) {
            Toast.makeText(requireContext(), "Gênero cadastrado", Toast.LENGTH_SHORT).show();
            etNomeGenero.setText("");
            spCorGenero.setSelection(0);
            etNomeGenero.requestFocus();
            mostrarFragment(new GeneroListView());
        } else {
            Toast.makeText(requireContext(), "Não foi possível cadastrar o gênero", Toast.LENGTH_SHORT).show();
        }
    }

    private void preencherParaEdicao() {
        Genero genero = new GeneroDAL(requireContext()).get(generoId);
        if (genero == null) return;
        etNomeGenero.setText(genero.getNome());
        for (int i = 0; i < CORES_PRIMARIAS.length; i++) {
            if (CORES_PRIMARIAS[i] == genero.getCor()) {
                spCorGenero.setSelection(i);
                break;
            }
        }
        btCadastrarGenero.setText("Salvar alterações");
    }

    private void mostrarFragment(Fragment fragment) {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }
}
