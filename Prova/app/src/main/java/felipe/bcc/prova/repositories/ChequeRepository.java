package felipe.bcc.prova.repositories;

import java.util.ArrayList;
import java.util.List;

import felipe.bcc.prova.entities.Cheque;

public class ChequeRepository {
    private final ArrayList<Cheque> cheques = new ArrayList<>();

    public List<Cheque> listar() { return cheques; }
    public void incluir(Cheque cheque) { cheques.add(cheque); }
    public void remover(int posicao) { cheques.remove(posicao); }

    public double calcularTotalLiquido() {
        double total = 0.0;
        for (Cheque cheque : cheques) {
            total += cheque.getValor() - cheque.getValorJuros();
        }
        return total;
    }
}
