package clientSide;

import serverSide.Server;
import serverSide.listaEncadeada.RegistroClimatico;

public class Dispositivo {

    public String idDispositivo;

    public RegistroClimatico[] registros; 
    public int quantRegisPorDispositivo;

    // Construtor do dispositivo
    public Dispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
        this.registros = new RegistroClimatico[10];
        this.quantRegisPorDispositivo = 0;
    }

    // Construtor do controlador de dispositivo
    public Dispositivo(){

    }

    // Método que adiciona novo registro
    public void cadastrarRegistro(Server server, RegistroClimatico registro){
        server.inserir(registro);
    }

    // Método que altera um registro passado
    public void alterarRegistro(Server server, int chave, String campo, Object dado){
        server.alteracao(chave, campo, dado);
    }

    // Método que irá adicionar o registro ao array do dispositivo
    public void addRegisterToArray(RegistroClimatico registro) {

        // verifica se está cheio
        if (quantRegisPorDispositivo == registros.length) {
            
            // cria novo tamanho
            int novoTam = registros.length * 2;
            RegistroClimatico[] newArray = new RegistroClimatico[novoTam];

            // copia os elementos do antigo array para o novo
            for (int i = 0; i < registros.length; i++) {
                newArray[i] = registros[i];
            }

            // substitui
            this.registros = newArray;
        }

        // Adiciona o registro na próxima posição disponível
        this.registros[quantRegisPorDispositivo] = registro;
        this.quantRegisPorDispositivo++;
    }

    // Método que irá remover o registro ao array do dispositivo
    public void removeRegisterFromArray(int chave){
        int indexToRemove = -1;
        // Acha o index do registro a ser removido
        for (int i = 0; i < quantRegisPorDispositivo; i++) {
            if (registros[i].idRegistro == chave) { 
                indexToRemove = i;
                break;
            }
        }

        // Se for encontrado os elementos irão ser movimentados para a esquerda até o último elemento
        if (indexToRemove != -1) {
            for (int i = indexToRemove; i < quantRegisPorDispositivo - 1; i++) {
                registros[i] = registros[i + 1];
            }
            // Efetua remoção do último elemento e decrementa quantidade
            quantRegisPorDispositivo--;
            registros[quantRegisPorDispositivo] = null;
        }
    }
}
