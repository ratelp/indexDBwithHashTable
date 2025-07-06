package serverSide.hashTable;

import clientSide.Dispositivo;

public class hashTableDispositivo {
    

    public int m, n;
    final int tamBase = 17; // Número primo base utilizado
    final int tamMax = 163; // Numero primo que suporta em 0.61 os 100 que serão adicionados
    NoDispositivo[] tabela;

    public int redimensionamentos = 0;
    public int colisoes = 0;

    // Tombstone
    private static final NoDispositivo usado = new NoDispositivo(); 

    // Construtor
    public hashTableDispositivo(int tam) {

        // 17 é o tamanho mínimo estabelecido
        if(tam >17){
            this.m = tam;
        }else {
            this.m = 17;
        }
        this.tabela = new NoDispositivo[m];
        this.n = 0;
    }

    // Método multiplicativo
    // Formato:
    // m * ((key * A) % 1)
    public int hash1(int key){
        double A = 0.6180339887;
        return (((int) Math.floor(this.m * (key * A % 1)))& 0x7FFFFFFF);
    }

    // Função Duplo Hash
    // Formato:
    // (h1 + k * (R - (key % R))) % m
    public int hash(int key, int k) {
        int R = 11;

        int h1 = hash1(key); 
        int h2 = (R - (key % R))& 0x7FFFFFFF;

        int result = (h1 + k * h2) % m;

        // Se o resultado for negativo, soma o valor da tabela
        return result < 0 ? result + this.m : result;
    }
 
    // Chama função para inserção de dispositivo (sempre passando true pois essa é a inserção que não é usada na reorganização)
    public void inserir(Dispositivo chave){
        // Para adição direta do valor é necessário efetuar a reorganização da tabela (caso ultrapasse os limites estabelecidos)
        // Portanto é inicializado como true em inserções noDispositivormais, em caso de inserção por reorganização da tabela é passado falso
        inserir(chave,chave.idDispositivo,true);
    }

    // insere dispositivo
    public void inserir(Dispositivo dispositivo,String chave, boolean resize){

        int tentativas = 0;
        int primeiroSlotLivre = -1;
        while (tentativas < m) { 
            int h = hash(chave.hashCode(), tentativas);

            // Se localizado slot que já foi utilizado salva o primeiro que localizou
            if (tabela[h] == usado && primeiroSlotLivre == -1) {
                primeiroSlotLivre = h;
            }

            if (tabela[h] == null) {
                // localizou slot nulo, portanto verifica se já havia achado slot usado, se não, utiliza slot nulo
                int indiceInsercao = (primeiroSlotLivre != -1) ? primeiroSlotLivre : h;
                
                tabela[indiceInsercao] = new NoDispositivo();
                tabela[indiceInsercao].dispositivo = dispositivo;
                n++;
                if(resize){
                    examinarCarga();
                }
                return;
            }

            // Não insere duplicatas
            if (tabela[h] != usado && tabela[h].dispositivo.idDispositivo.equals(chave)) {
                return;
            }

            tentativas++;
            colisoes++;
        }
        // Se a tabela estiver sem slots nulos (mais de 25% de slots usados)
        if (primeiroSlotLivre != -1) {
            tabela[primeiroSlotLivre] = new NoDispositivo();
            tabela[primeiroSlotLivre].dispositivo = dispositivo;
            n++;
            if(resize){
                examinarCarga();
            }
        } 
    }

    // Remove determinado dispositivo a partir da chave
    public boolean remover(String chave){
        int tentativa = 0;

        while(tentativa < m){
            int h = hash(chave.hashCode(),tentativa);

            if(tabela[h] == null){
                return false;
            }

            if(tabela[h] != usado && tabela[h].dispositivo.idDispositivo.equals(chave)){
                tabela[h] = usado;
                n--;
                examinarCarga();
                return true;
            }
            tentativa++;
        }

        return false;
    }

    // Busca determinado dispositivo a partir da chave
    public Dispositivo buscar(String chave){
        int tentativa = 0;
        int h = hash(chave.hashCode(), tentativa);

        while(tabela[h] != null) {

            if (tabela[h] != usado && tabela[h].dispositivo.idDispositivo.equals(chave)) {
                return tabela[h].dispositivo;
            }

            h = hash(chave.hashCode(), ++tentativa);
        }
        return null;

    }
 
    // Verifica se há necessidade de modificar tamanho da tabela a partir do fator de carga
    private void examinarCarga(){
        double fatorCarga = (double) n/m;

        if(fatorCarga >= 0.75){
            reorganizar(Math.min(proximoPrimo(m * 2), tamMax));
        }else if(fatorCarga < 0.25 && m > tamBase){
            reorganizar(Math.max(proximoPrimo(m/2),tamBase));
        }
    }
    
    // Reorganiza tabela de acordo com novo tamanho
    private void reorganizar(int tamanho){
        redimensionamentos++;
        NoDispositivo[] antiga = tabela;

        m = tamanho;
        tabela = new NoDispositivo[m];
        n = 0;

        for(NoDispositivo noDispositivo: antiga){
            if (noDispositivo != null && noDispositivo != usado) {
                inserir(noDispositivo.dispositivo,noDispositivo.dispositivo.idDispositivo, false);
            }
        }
    }

    // Localizar o próximo primo para que o tamanho da tabela seja sempre primo
    private int proximoPrimo(int numero) {
        if (numero % 2 == 0) {
            numero++;
        }
        while (!verificaPrimo(numero)) {
            numero += 2;
        }
        return numero;
    }

    // verifica se é primo
    private boolean verificaPrimo(int numero) {
        if (numero <= 1) return false;
        for (int i = 3; i * i <= numero; i += 2) {
            if (numero % i == 0) {
                return false;
            }
        }
        return true;
    }

    // Imprime todos os Dispositivos na tabela
    public void imprimir(){
        
         for(int i = 0; i < m; i++) {

            if (tabela[i] != usado && tabela[i] != null)
                System.out.println(i + " --> " + tabela[i].dispositivo.idDispositivo);
            else
                System.out.println(i);
        }
    }
}
