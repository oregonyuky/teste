package felipe.bcc.prova.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Cheque {
    private final double valor;
    private final LocalDate dataVencimento;
    private final int diasParaDeposito;
    private final double valorJuros;

    public Cheque(double valor, LocalDate dataVencimento, double jurosMensais,
                  LocalDate dataDeCalculo) {
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.diasParaDeposito = (int) ChronoUnit.DAYS.between(dataDeCalculo, dataVencimento);
        this.valorJuros = calcularJuros(valor, jurosMensais, diasParaDeposito);
    }

    public static double calcularJuros(double valor, double jurosMensais, int dias) {
        return valor * (((jurosMensais / 30.0) * dias) / 100.0);
    }

    public double getValor() { return valor; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public int getDiasParaDeposito() { return diasParaDeposito; }
    public double getValorJuros() { return valorJuros; }
}
