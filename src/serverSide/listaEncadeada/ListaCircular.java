package serverSide.listaEncadeada;


public class ListaCircular {

    public RegistroClimatico ultimo;

    // Construtor da lista encadeada
    public ListaCircular(){
        ultimo = null;
    }

    // Insere na lista encadeada
    public void inserir(RegistroClimatico registro){
        if (ultimo == null){
            ultimo = registro;
            ultimo.proximo = registro;
        }else{
            registro.proximo = ultimo.proximo;
            ultimo.proximo = registro;
            ultimo = registro;
        }
    }

    // Remove na lista encadeada
    public void remover(RegistroClimatico registro){
        
        // Se possuir somente 1 nó 
        if (ultimo == null || ultimo == ultimo.proximo){
            ultimo = null;
            return;
        }

        // Coleta dados do próximo registro e aponta para o próximo do próximo
        registro.idRegistro = registro.proximo.idRegistro;
        registro.idDispositivo = registro.proximo.idDispositivo;
        registro.dataHora = registro.proximo.dataHora;
        registro.temperatura = registro.proximo.temperatura;
        registro.pressao = registro.proximo.pressao;
        registro.umidade = registro.proximo.umidade;
        registro.proximo = registro.proximo.proximo;

    }

}
