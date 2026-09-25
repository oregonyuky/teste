package felipe.bcc.appmusic.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import felipe.bcc.appmusic.MainActivity;
import felipe.bcc.appmusic.R;
import felipe.bcc.appmusic.db.bean.Genero;
import felipe.bcc.appmusic.db.dal.GeneroDAL;

public class NovoGeneroFragment extends Fragment {
    private static Genero generoEmEdicao;
    private TextInputEditText nomeInput;
    private View amostraCor;
    private int corSelecionada = Color.rgb(68, 71, 90);

    public static NovoGeneroFragment novo(Genero genero) {
        generoEmEdicao = genero;
        return new NovoGeneroFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novo_genero, container, false);
        nomeInput = view.findViewById(R.id.et_nome_genero);
        amostraCor = view.findViewById(R.id.cor_selecionada);

        if (generoEmEdicao != null) {
            nomeInput.setText(generoEmEdicao.getNome());
            corSelecionada = generoEmEdicao.getCor();
        }
        atualizarAmostra();

        view.findViewById(R.id.bt_escolher_cor).setOnClickListener(v -> escolherCor());
        view.findViewById(R.id.bt_salvar_genero).setOnClickListener(v -> salvar(view));
        return view;
    }

    private void escolherCor() {
        View conteudo = getLayoutInflater().inflate(R.layout.dialog_seletor_cor, null);
        View amostraDialog = conteudo.findViewById(R.id.amostra_cor_dialog);
        SeekBar matiz = conteudo.findViewById(R.id.sb_matiz);
        SeekBar saturacao = conteudo.findViewById(R.id.sb_saturacao);
        SeekBar brilho = conteudo.findViewById(R.id.sb_brilho);

        float[] hsv = new float[3];
        Color.colorToHSV(corSelecionada, hsv);
        matiz.setProgress(Math.round(hsv[0]));
        saturacao.setProgress(Math.round(hsv[1] * 100));
        brilho.setProgress(Math.round(hsv[2] * 100));

        final int[] novaCor = {corSelecionada};
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                novaCor[0] = Color.HSVToColor(new float[]{
                        matiz.getProgress(),
                        saturacao.getProgress() / 100f,
                        brilho.getProgress() / 100f
                });
                amostraDialog.setBackgroundColor(novaCor[0]);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        };
        matiz.setOnSeekBarChangeListener(listener);
        saturacao.setOnSeekBarChangeListener(listener);
        brilho.setOnSeekBarChangeListener(listener);
        amostraDialog.setBackgroundColor(novaCor[0]);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.escolher_cor)
                .setView(conteudo)
                .setPositiveButton(R.string.confirmar, (dialog, which) -> {
                    corSelecionada = novaCor[0];
                    atualizarAmostra();
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void atualizarAmostra() {
        amostraCor.setBackgroundColor(corSelecionada);
    }

    private void salvar(View view) {
        String nome = nomeInput.getText() == null ? "" : nomeInput.getText().toString().trim();
        if (nome.isEmpty()) {
            nomeInput.setError(getString(R.string.nome_genero));
            return;
        }

        GeneroDAL dal = new GeneroDAL(requireContext());
        int idIgnorado = generoEmEdicao == null ? 0 : generoEmEdicao.getId();
        if (dal.nomeExiste(nome, idIgnorado)) {
            nomeInput.setError(getString(R.string.nome_genero_ja_cadastrado));
            nomeInput.requestFocus();
            return;
        }
        if (dal.corExiste(corSelecionada, idIgnorado)) {
            Snackbar.make(view, R.string.cor_ja_cadastrada, Snackbar.LENGTH_SHORT).show();
            return;
        }

        boolean sucesso;
        if (generoEmEdicao == null) {
            sucesso = dal.salvar(new Genero(nome, corSelecionada));
        } else {
            generoEmEdicao.setNome(nome);
            generoEmEdicao.setCor(corSelecionada);
            sucesso = dal.alterar(generoEmEdicao);
        }

        if (sucesso) {
            generoEmEdicao = null;
            ((MainActivity) requireActivity()).mostrarGeneros();
        } else {
            int idAtual = generoEmEdicao == null ? 0 : generoEmEdicao.getId();
            int mensagem;
            if (dal.nomeExiste(nome, idAtual)) {
                mensagem = R.string.nome_genero_ja_cadastrado;
            } else if (dal.corExiste(corSelecionada, idAtual)) {
                mensagem = R.string.cor_ja_cadastrada;
            } else {
                mensagem = R.string.erro_salvar_genero;
            }
            Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (requireActivity().isFinishing()) generoEmEdicao = null;
    }
}
