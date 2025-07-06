
import java.io.IOException;

import serverSide.Server;
import clientSide.Cliente;
import clientSide.Dispositivo;
import clientSide.Menu;

public class Main {
    public static void main(String[] args) {
        
        // Simulador de Server
        Server server = new Server();
        
        // Simulador de Cliente
        Cliente cliente = new Cliente(); 
        
        // Controle de dispositivos para menu
        Dispositivo controleDispositivo = new Dispositivo(); 
        
        // Inicializa menu
        Menu menu = new Menu(); 
        

        // inicializa interface do cliente (usuário)
        menu.interfaceCliente(cliente, server, controleDispositivo); 

        // fecha arquivo
        try {
            server.writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao fechar arquivo");
        }
    }
}
