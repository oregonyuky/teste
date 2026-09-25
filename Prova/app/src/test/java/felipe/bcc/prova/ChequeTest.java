package felipe.bcc.prova;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import felipe.bcc.prova.entities.Cheque;

public class ChequeTest {
    @Test
    public void calculaJurosSimplesProporcionaisAosDias() {
        assertEquals(35.0, Cheque.calcularJuros(1000.0, 3.5, 30), 0.001);
        assertEquals(17.5, Cheque.calcularJuros(1000.0, 3.5, 15), 0.001);
    }
}
