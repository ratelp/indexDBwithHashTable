package serverSide.listaEncadeada;


import java.time.LocalDateTime;
import clientSide.Dispositivo;


public class RegistroClimatico {

    static int count = 0;
    public int idRegistro;
    public Dispositivo idDispositivo;
    public LocalDateTime dataHora;
    public double temperatura;
    public double umidade;
    public double pressao;
    public RegistroClimatico proximo;

    // Construtor do registro climático
    public RegistroClimatico(Dispositivo dispositivo, double temperatura, double umidade, double pressao) {

        this.idRegistro = ++count;
        this.idDispositivo = dispositivo;
        this.dataHora = LocalDateTime.now();
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.pressao = pressao;
        this.proximo = null;
    }

}
