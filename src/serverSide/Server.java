package serverSide;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

import clientSide.Dispositivo;
import serverSide.hashTable.hashTable;
import serverSide.hashTable.hashTableDispositivo;
import serverSide.listaEncadeada.ListaCircular;
import serverSide.listaEncadeada.RegistroClimatico;

public class Server {
    

    ListaCircular baseDeDados;
    public hashTable tabelaRegistros;
    public hashTableDispositivo tabelaDispositivos;
    public FileWriter writer;
    public int quantidadeRegistros = 0;

    // Construtor do Server
    public Server(){
        this.baseDeDados = new ListaCircular();
        this.tabelaRegistros = new hashTable(17);
        this.tabelaDispositivos = new hashTableDispositivo(17);

        try {
            this.writer = new FileWriter("./log/serverLog.txt");
        } catch (IOException e){
            System.out.println("Erro ao criar arquivo.");
        }

        quantidadeRegistros = inicializacao(baseDeDados);

        // pega ultimo para interar por todos os dados, comparar e verificar se já passou por todos de fato
        RegistroClimatico atual = baseDeDados.ultimo;

        if(atual != null){
            do{
                // Indexando banco de dados com a tabela hash
                tabelaRegistros.inserir(atual);
                tabelaDispositivos.inserir(atual.idDispositivo);

                // passa para próximo registro da base de dados
                atual = atual.proximo;

                // intera até chegar no último registro
            }while(atual != baseDeDados.ultimo);
        }

        arquivo("Inicialização");
    
    }

    // Inicialização dos dados no banco de dados
    private int inicializacao(ListaCircular baseDeDados){
        Dispositivo [] arrayInicial = new Dispositivo[101];
        
        for(int i = 1; i < 101; i++){
            Dispositivo dispo = new Dispositivo("dispo"+i);
            arrayInicial[i] = dispo; 
        }

        Random rand = new Random();
        int j = 1;
        for(int i = 1; i < 10001; i++){
            Dispositivo dispo = arrayInicial[j];
            if (dispo != null) {
                RegistroClimatico registro = new RegistroClimatico(dispo, rand.nextInt(0,41), rand.nextInt(10,41), rand.nextInt(1,11)); 
                
                // Adiciona registro no array do dispositivo que ele representa
                dispo.addRegisterToArray(registro);
                baseDeDados.inserir(registro); 
            }
            j++;
            if(i%100 == 0){
                j= 1;
            }
        }
        return baseDeDados.ultimo.idRegistro;
    }

    // Adiciona novo registro - para dispositivo já existente
    public void inserir(RegistroClimatico registro){
        baseDeDados.inserir(registro);
        tabelaRegistros.inserir(registro);
        registro.idDispositivo.addRegisterToArray(registro);
        quantidadeRegistros++;

        arquivo("Inserção");
    }

    // Adiciona novo registro - para dispositivo novo
    public void inserir(RegistroClimatico registro, Dispositivo dispositivo){
        baseDeDados.inserir(registro);
        tabelaRegistros.inserir(registro);
        tabelaDispositivos.inserir(dispositivo);
        dispositivo.addRegisterToArray(registro);
        quantidadeRegistros++;

        arquivo("Inserção");
    }

    // Remove registro de acordo com chave
    public boolean remover(int chave){
        RegistroClimatico registro = tabelaRegistros.buscar(chave);

        if(registro == null){
            System.out.println("Registro não encontrado!");
            return false;
        }

        // Remove referencia ao registro da array que contem os registros vinculados ao dispositivo 
        registro.idDispositivo.removeRegisterFromArray(chave);

        if(registro.idDispositivo.quantRegisPorDispositivo == 0){
            // Se a quantidade de registros no dispositivo chegar a 0 remove o dispositivo em si
            tabelaDispositivos.remover(registro.idDispositivo.idDispositivo);
        }

        tabelaRegistros.remover(chave); // remove referência ao registro
        baseDeDados.remover(registro); // remove registro da base de dados
        quantidadeRegistros--;
        
        arquivo("Remoção");
        return true;
    } 

    // Efetua alteração de acordo com campo informado
    public void alteracao(int chave, String campo, Object dado){
        RegistroClimatico registro = tabelaRegistros.buscar(chave);
        if(registro == null){
            System.out.println("Valor não encontrado");
        }else{
            switch (campo.toLowerCase()){
                case "datahora":
                    if(dado instanceof LocalDateTime){
                        registro.dataHora = (LocalDateTime)dado;
                    }
                    break;
                case "temperatura":
                    if (dado instanceof Number){
                        registro.temperatura = ((Number) dado).doubleValue();
                    }
                    break;
                case "umidade":
                    if(dado instanceof Number){
                        registro.umidade = ((Number)dado).doubleValue();
                    }
                    break;
                case "pressao":
                    if(dado instanceof Number){
                        registro.pressao = ((Number)dado).doubleValue();
                    }
                    break;
                default:
                    System.out.println("Campo inexistente");
            }  
        }
    }

    // Gera saída para ser escrito no arquivo
    public void arquivo(String titulo){
        try {
            if(titulo == "Inicialização"){
                double fatorCarga = ((double) tabelaDispositivos.n / tabelaDispositivos.m)*100;
                String formattedOutput = String.format(
                "%s:\nTotal de elementos: %d\nTamanho da tabela (m): %d\nFator de carga: %.2f%%\nRedimensionamentos: %d\nColisões detectadas: %d\n\n",
                titulo + " da tabela de Dispositivos",
                tabelaDispositivos.n,
                tabelaDispositivos.m,
                fatorCarga,
                tabelaDispositivos.redimensionamentos,
                tabelaDispositivos.colisoes
            );
            writer.write(formattedOutput);
            fatorCarga = ((double) tabelaRegistros.n / tabelaRegistros.m)*100;
            formattedOutput = String.format(
                "%s:\nTotal de elementos: %d\nTamanho da tabela (m): %d\nFator de carga: %.2f%%\nRedimensionamentos: %d\nColisões detectadas: %d\n\n",
                titulo + " da tabela de Registros",
                tabelaRegistros.n,
                tabelaRegistros.m,
                fatorCarga,
                tabelaRegistros.redimensionamentos,
                tabelaRegistros.colisoes
            );
            writer.write(formattedOutput);
            }else{
                double fatorCarga = ((double) tabelaRegistros.n / tabelaRegistros.m)*100;
                String formattedOutput = String.format(
                    "%s:\nTotal de elementos: %d\nTamanho da tabela (m): %d\nFator de carga: %.2f%%\nRedimensionamentos: %d\nColisões detectadas: %d\n\n",
                    titulo,
                    tabelaRegistros.n,
                    tabelaRegistros.m,
                    fatorCarga,
                    tabelaRegistros.redimensionamentos,
                    tabelaRegistros.colisoes
                );
                writer.write(formattedOutput);
            }
        } catch (IOException e) {
                System.out.println("Erro ao escrever");
            }
    }  
}
