package clientSide;

import java.time.format.DateTimeFormatter;

import serverSide.Server;
import serverSide.listaEncadeada.RegistroClimatico;

public class Cliente {
    
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Busca pelo id (INT)
    public void busca(Server server, int chave){
        try{
            RegistroClimatico registro = server.tabelaRegistros.buscar(chave);
            System.out.println("--\nId Registro: " + registro.idRegistro + "\nId Dispositivo: " + registro.idDispositivo.idDispositivo + "\nData: " + registro.dataHora.format(dateFormatter) + "\nHora: " + registro.dataHora.format(timeFormatter) + "\nTemperatura: " + registro.temperatura + "\nUmidade: " + registro.umidade + "\nPressão: " + registro.pressao);
        }catch(Exception e){
            System.out.println("Registro não localizado");
        }
    }

    // Busca pelo id (STRING)
    public void busca(Server server, String dispoId){
        try {
            Dispositivo dispo = server.tabelaDispositivos.buscar(dispoId);
            for (int i = 0; i < dispo.quantRegisPorDispositivo; i++) {
                 RegistroClimatico registro = dispo.registros[i];
                 System.out.println("  - ID: " + registro.idRegistro + 
                                   ", Temperatura: " + registro.temperatura + 
                                   ", Umidade: " + registro.umidade + 
                                   ", Pressão: " + registro.pressao + 
                                   ", Data: " + registro.dataHora.format(dateFormatter) +
                                   ", Hora: " + registro.dataHora.format(timeFormatter)); 
            }
        } catch (Exception e) {
                        System.out.println("Dispositivo não localizado ou não possui registros");

        }       
    }

    // Lista todos os registros
    public void listagem(Server server){
        server.tabelaRegistros.imprimir();
    }

    // Remove registro pelo id
    public boolean removerRegistro(Server server, int chave){
        boolean resultado = server.remover(chave);
        return resultado;
    }

    // Acessar quantidade de registros
    public int quantidadeRegistros(Server server){
        return server.quantidadeRegistros;
    }

}
