package clientSide;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import serverSide.Server;
import serverSide.listaEncadeada.RegistroClimatico;


public class Menu {
    
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    // Mostra Menu
    public void mostrarMenu(){
        System.out.println("MENU: ");
        System.out.println("1 - Listar");
        System.out.println("2 - Remover");
        System.out.println("3 - Buscar");
        System.out.println("4 - Cadastrar");
        System.out.println("5 - Alterar");
        System.out.println("6 - Quantidade Registros");
        System.out.println("7 - Teste Avaliação");
        System.out.println("8 - Sair");
        System.out.printf("Opção: ");
    }

    // Interface em que o cliente (usuário) irá interagir  
    public void interfaceCliente(Cliente cliente, Server server, Dispositivo dispositivo){
        
        Scanner scanner = new Scanner(System.in);

        int opcao;
        int chave;
        String chaveString;
        boolean encerrar = false;
        do{
            mostrarMenu();
            try{
                opcao = scanner.nextInt();
                switch(opcao){
                case 1:
                    cliente.listagem(server);
                    break;
                case 2:
                    System.out.printf("Informe a chave: ");
                    chave = scanner.nextInt();
                    
                    boolean resultado = cliente.removerRegistro(server, chave);
                    
                    if(resultado){
                        System.out.println("Removido com sucesso!");
                    }else{
                        System.out.println("Sem sucesso ao remover!");
                    }

                    break;
                case 3:
                    System.out.println("\n1 - IdRegistro\n2 - IdDispositivo" );
                    System.out.printf("Opção: ");
                    opcao = scanner.nextInt();
                    if(opcao == 1){
                        System.out.printf("Informe a chave: ");
                        chave = scanner.nextInt();
                        cliente.busca(server, chave);
                    }else if(opcao == 2){
                        System.out.printf("Informe a chave: ");
                        chaveString = scanner.next();
                        cliente.busca(server, chaveString);
                    }else{
                        System.out.println("Opção inválida!");
                    }
                    break;
                case 4:
                    String idDispositivo;
                    double temperatura, umidade, pressao;
                    System.out.printf("\nInforme o idDispositivo: ");
                    idDispositivo = scanner.next();
                    Dispositivo novoDispositivo = new Dispositivo(idDispositivo);
                    System.out.printf("Informe a temperatura: ");
                    temperatura = scanner.nextDouble();
                    System.out.printf("Informe a umidade: ");
                    umidade = scanner.nextDouble();    
                    System.out.printf("Informe a pressão: ");
                    pressao = scanner.nextDouble();
                    RegistroClimatico novo = new RegistroClimatico(novoDispositivo, temperatura, umidade, pressao);
                    dispositivo.cadastrarRegistro(server, novo);
                    System.out.println("Cadastrado com sucesso!");
                break;
                
                case 5:
                    System.out.printf("\nInforme o ID: ");
                    chave = scanner.nextInt();
                    cliente.busca(server, chave);

                    System.out.println("\nOpções de alteração:");
                    System.out.println("1 - Data e hora");
                    System.out.println("2 - Temperatura");
                    System.out.println("3 - Umidade");
                    System.out.println("4 - Pressao");
                    System.out.printf("\nInforme o campo: ");
                    opcao = scanner.nextInt();

                    if(opcao == 1){
                        dispositivo.alterarRegistro(server,chave, "datahora", LocalDateTime.now());
                        System.out.println("Alterado para : " + LocalDateTime.now().format(dateFormatter) + " " + LocalDateTime.now().format(timeFormatter) + " (horário atual)");
                        System.out.println("\nRegistro Atualizado: ");
                        cliente.busca(server, chave);
                    }else if(opcao == 2){
                        System.out.printf("\nInforme a Temperatura: ");
                        opcao = scanner.nextInt();
                        dispositivo.alterarRegistro(server, chave, "temperatura", opcao);
                        System.out.println("\nRegistro Atualizado: ");
                        cliente.busca(server, chave);
                    }else if(opcao == 3){
                        System.out.printf("\nInforme a umidade: ");
                        opcao = scanner.nextInt();
                        dispositivo.alterarRegistro(server, chave, "umidade", opcao);
                        System.out.println("\nRegistro Atualizado: ");
                        cliente.busca(server, chave);
                    }else if(opcao == 4){
                        System.out.printf("\nInforme a pressao: ");
                        opcao = scanner.nextInt();
                        dispositivo.alterarRegistro(server, chave, "pressao", opcao);
                        System.out.println("\nRegistro Atualizado: ");
                        cliente.busca(server, chave);
                    }else{
                        System.out.println("Opção inválida!");
                    }
                    break;
                case 6:
                    System.out.println("Atualmente temos " + cliente.quantidadeRegistros(server) + " registros");
                    break;
                case 7:
                    scanner.nextLine();
                    // buscas
                    buscas(cliente, server);
                    System.out.println("\n----------Pressione 'enter' para prosseguir...");
                    scanner.nextLine();
                    
                    // cadastros
                    cadastros(dispositivo, server);
                    cliente.listagem(server);
                    System.out.println("\n----------Pressione 'enter' para prosseguir...");
                    scanner.nextLine();

                    // alterações
                    alteracoes(dispositivo,cliente, server, scanner);
                    cliente.listagem(server);
                    System.out.println("\n----------Pressione 'enter' para prosseguir...");
                    scanner.nextLine();
                    
                    // remoções
                    remocoes(cliente, server, scanner);
                    cliente.listagem(server);
                    System.out.println("\n----------Pressione 'enter' para prosseguir...");
                    scanner.nextLine();

                    break;
                case 8:
                try {
                    
                    StringBuilder formattedOutput = new StringBuilder("Quantidade de registros por dispositivo:\n");

                    for (int i = 1; i <= server.tabelaDispositivos.n; i++) {
                        String dispo = "dispo" + i;
                        int quantRegis = server.tabelaDispositivos.buscar(dispo).quantRegisPorDispositivo;

                        formattedOutput.append(String.format("Dispositivo %s: %d\n", dispo, quantRegis));
                    }

                    server.writer.write(formattedOutput.toString());
                    } catch (IOException e) {
                        System.out.println("Erro ao escrever");
                    }

                    encerrar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
            System.out.println();
            }catch(InputMismatchException  e)
            {
                System.out.println("Entrada inválida!\n");
                scanner.nextLine();
            };
    
        }while(!encerrar);

        scanner.close();
    }

    // Realiza buscas para demonstração da atividade
    public void buscas(Cliente cliente, Server server){
        System.out.println("\nBuscando pela chave '50'");
        cliente.busca(server, 50);
        System.out.println("\nBuscando pela chave '101'");
        cliente.busca(server,101);
        System.out.println("\nBuscando pela chave '30'");
        cliente.busca(server, 30);
        System.out.println("\nBuscando pela chave '60'");
        cliente.busca(server, 60);
        System.out.println("\nBuscando pela chave '90'");
        cliente.busca(server, 90);
        System.out.println("\nBuscando pela chave '9999'");
        cliente.busca(server, 9999);
        System.out.println("\nBuscando pela chave '1000'");
        cliente.busca(server, 1000);
        System.out.println("\nBuscando pela chave 'dispo1'");
        cliente.busca(server, "dispo1");
        System.out.println("\nBuscando pela chave '1000000'");
        cliente.busca(server, 1000000);
        System.out.println("\nBuscando pela chave '-1'");
        cliente.busca(server, -1);
    }

    // Realiza cadastros para demonstração da atividade
    public void cadastros(Dispositivo dispositivos,  Server server){
        Dispositivo dispo1 = server.tabelaDispositivos.buscar("dispo1");
        RegistroClimatico registro1 = new RegistroClimatico(dispo1, 3, 10, 2);
        dispositivos.cadastrarRegistro(server, registro1);

        Dispositivo dispo2 = server.tabelaDispositivos.buscar("dispo20");
        RegistroClimatico registro2 = new RegistroClimatico(dispo2, 7, 14, 1);
        dispositivos.cadastrarRegistro(server, registro2);

        Dispositivo dispo3 = server.tabelaDispositivos.buscar("dispo30");
        RegistroClimatico registro3 = new RegistroClimatico(dispo3, 30, 4, 6);
        dispositivos.cadastrarRegistro(server, registro3);

        Dispositivo dispo4 = server.tabelaDispositivos.buscar("dispo4");
        RegistroClimatico registro4 = new RegistroClimatico(dispo4, 34, 8, 3);
        dispositivos.cadastrarRegistro(server, registro4);

        Dispositivo dispo5 = server.tabelaDispositivos.buscar("dispo9");
        RegistroClimatico registro5 = new RegistroClimatico(dispo5, 14, 8, 6);
        dispositivos.cadastrarRegistro(server, registro5);

        Dispositivo dispo6 = server.tabelaDispositivos.buscar("dispo2");
        RegistroClimatico registro6 = new RegistroClimatico(dispo6, 1, 9, 8);
        dispositivos.cadastrarRegistro(server, registro6);

        Dispositivo dispo7 = server.tabelaDispositivos.buscar("dispo100");
        RegistroClimatico registro7 = new RegistroClimatico(dispo7, 37, 3, 1);
        dispositivos.cadastrarRegistro(server, registro7);

        Dispositivo dispo8 = server.tabelaDispositivos.buscar("dispo37");
        RegistroClimatico registro8 = new RegistroClimatico(dispo8, 14, 7,9);
        dispositivos.cadastrarRegistro(server, registro8);

        Dispositivo dispo9 = server.tabelaDispositivos.buscar("dispo51");
        RegistroClimatico registro9 = new RegistroClimatico(dispo9, 11, 5, 2);
        dispositivos.cadastrarRegistro(server, registro9);

        Dispositivo dispo10 = server.tabelaDispositivos.buscar("dispo92");
        RegistroClimatico registro10 = new RegistroClimatico(dispo10, 21, 10, 1);
        dispositivos.cadastrarRegistro(server, registro10);
    }

    // Realiza alterações para demonstração da atividade
    public void alteracoes(Dispositivo dispositivo, Cliente cliente, Server server,Scanner scanner){
        System.out.println("\nAlterando registro com chave '20':");
        cliente.busca(server,20);
        dispositivo.alterarRegistro(server, 20, "datahora", LocalDateTime.now());
        System.out.println("\nRegistro '20' após alteração de data e hora: ");
        cliente.busca(server,20);
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        System.out.println("\nAlterando registro com chave '201':");
        cliente.busca(server,201);
        dispositivo.alterarRegistro(server, 201, "umidade", 6.77);
        System.out.println("\nRegistro '201' após alteração da umidade: ");
        cliente.busca(server,201);
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        System.out.println("\nAlterando registro com chave '522':");
        cliente.busca(server,522);
        dispositivo.alterarRegistro(server, 522, "temperatura", 36.61);
        System.out.println("\nRegistro '522' após alteração da temperatura: ");
        cliente.busca(server,522);
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        System.out.println("\nAlterando registro com chave '133':");
        cliente.busca(server,133);
        dispositivo.alterarRegistro(server, 133, "pressao", 2.01);
        System.out.println("\nRegistro '133' após alteração da pressao: ");
        cliente.busca(server,133);
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        System.out.println("\nAlterando registro com chave '999':");
        cliente.busca(server,999);
        dispositivo.alterarRegistro(server, 999, "datahora", LocalDateTime.now());
        System.out.println("\nRegistro '999' após alteração da data e hora: ");
        cliente.busca(server,999);
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        System.out.println("\nAlterando registro com chave '10.005':");
        cliente.busca(server,10005);
        dispositivo.alterarRegistro(server, 10005, "umidade", 82.1);
        System.out.println("\nRegistro '10.005' após alteração da umidade: ");
        cliente.busca(server,10005);
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        System.out.println("\nAlterando registro com chave '9696':");
        cliente.busca(server,9696);
        dispositivo.alterarRegistro(server, 9696, "temperatura", 12.5);
        System.out.println("\nRegistro '9696' após alteração da temperatura: ");
        cliente.busca(server,9696);
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        System.out.println("\nAlterando registro com chave '6':");
        cliente.busca(server,6);
        dispositivo.alterarRegistro(server, 6, "pressao", 1.5);
        System.out.println("\nRegistro '6' após alteração da pressao: ");
        cliente.busca(server,6);
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        System.out.println("\nAlterando registro com chave '8':");
        cliente.busca(server,8);
        dispositivo.alterarRegistro(server, 8, "datahora", LocalDateTime.now());
        System.out.println("\nRegistro '8' após alteração da data e hora: ");
        cliente.busca(server,8);
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        System.out.println("\nAlterando registro com chave '45':");
        cliente.busca(server,45);
        dispositivo.alterarRegistro(server, 45, "datahora", LocalDateTime.now());
        System.out.println("\nRegistro '45' após alteração da data e hora: ");
        cliente.busca(server,45);
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();
        
    }

    // Realiza remoções para demonstração da atividade
    public void remocoes(Cliente cliente, Server server, Scanner scanner){

        {System.out.println("\nRemovendo registro com chave '10007':");
        cliente.busca(server,10007);
        cliente.removerRegistro(server, 10007);
        System.out.println("\nTentativa de busca pelo registro com chave '10007':");
        cliente.busca(server,10007);

        System.out.println("\nRemovendo registro com chave '99':");
        cliente.busca(server,99);
        cliente.removerRegistro(server, 99);
        System.out.println("\nTentativa de busca pelo registro com chave '99':");
        cliente.busca(server,99);

        System.out.println("\nRemovendo registro com chave '79':");
        cliente.busca(server,79);
        cliente.removerRegistro(server, 79);
        System.out.println("\nTentativa de busca pelo registro com chave '79':");
        cliente.busca(server,79);

        System.out.println("\nRemovendo registro com chave '777':");
        cliente.busca(server,777);
        cliente.removerRegistro(server, 777);
        System.out.println("\nTentativa de busca pelo registro com chave '777':");
        cliente.busca(server,777);

        System.out.println("\nRemovendo registro com chave '23':");
        cliente.busca(server,23);
        cliente.removerRegistro(server, 23);
        System.out.println("\nTentativa de busca pelo registro com chave '23':");
        cliente.busca(server,23);

        System.out.println("\nRemovendo registro com chave '1069':");
        cliente.busca(server,1069);
        cliente.removerRegistro(server, 1069);
        System.out.println("\nTentativa de busca pelo registro com chave '1069':");
        cliente.busca(server,1069);

        System.out.println("\nRemovendo registro com chave '828':");
        cliente.busca(server,828);
        cliente.removerRegistro(server, 828);
        System.out.println("\nTentativa de busca pelo registro com chave '828':");
        cliente.busca(server,828);

        System.out.println("\nRemovendo registro com chave '9998':");
        cliente.busca(server, 9998);
        cliente.removerRegistro(server,  9998);
        System.out.println("\nTentativa de busca pelo registro com chave '9998':");
        cliente.busca(server, 9998);

        System.out.println("\nRemovendo registro com chave '10010':");
        cliente.busca(server,10010);
        cliente.removerRegistro(server, 10010);
        System.out.println("\nTentativa de busca pelo registro com chave '10010':");
        cliente.busca(server,10010);

        System.out.println("\nRemovendo registro com chave '1':");
        cliente.busca(server,1);
        cliente.removerRegistro(server, 1);
        System.out.println("\nTentativa de busca pelo registro com chave '1':");
        cliente.busca(server,1);
        }
        
        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        {System.out.println("\nRemovendo registro com chave '125':");
        cliente.busca(server,125);
        cliente.removerRegistro(server, 125);
        System.out.println("\nTentativa de busca pelo registro com chave '125':");
        cliente.busca(server,125);

        System.out.println("\nRemovendo registro com chave '867':");
        cliente.busca(server,867);
        cliente.removerRegistro(server, 867);
        System.out.println("\nTentativa de busca pelo registro com chave '867':");
        cliente.busca(server,867);

        System.out.println("\nRemovendo registro com chave '1238':");
        cliente.busca(server,1238);
        cliente.removerRegistro(server, 1238);
        System.out.println("\nTentativa de busca pelo registro com chave '1238':");
        cliente.busca(server,1238);

        System.out.println("\nRemovendo registro com chave '9721':");
        cliente.busca(server,9721);
        cliente.removerRegistro(server, 9721);
        System.out.println("\nTentativa de busca pelo registro com chave '9721':");
        cliente.busca(server,9721);

        System.out.println("\nRemovendo registro com chave '123':");
        cliente.busca(server,123);
        cliente.removerRegistro(server, 123);
        System.out.println("\nTentativa de busca pelo registro com chave '123':");
        cliente.busca(server,123);

        System.out.println("\nRemovendo registro com chave '888':");
        cliente.busca(server,888);
        cliente.removerRegistro(server, 888);
        System.out.println("\nTentativa de busca pelo registro com chave '888':");
        cliente.busca(server,888);

        System.out.println("\nRemovendo registro com chave '100':");
        cliente.busca(server,100);
        cliente.removerRegistro(server, 100);
        System.out.println("\nTentativa de busca pelo registro com chave '100':");
        cliente.busca(server,100);

        System.out.println("\nRemovendo registro com chave '127':");
        cliente.busca(server, 127);
        cliente.removerRegistro(server,  127);
        System.out.println("\nTentativa de busca pelo registro com chave '127':");
        cliente.busca(server, 127);

        System.out.println("\nRemovendo registro com chave '1886':");
        cliente.busca(server,1886);
        cliente.removerRegistro(server, 1886);
        System.out.println("\nTentativa de busca pelo registro com chave '1886':");
        cliente.busca(server,1886);
        
        System.out.println("\nRemovendo registro com chave '358':");
        cliente.busca(server,358);
        cliente.removerRegistro(server, 358);
        System.out.println("\nTentativa de busca pelo registro com chave '358':");
        cliente.busca(server,358);}

        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        {System.out.println("\nRemovendo registro com chave '1927':");
        cliente.busca(server,1927);
        cliente.removerRegistro(server, 1927);
        System.out.println("\nTentativa de busca pelo registro com chave '1927':");
        cliente.busca(server,1927);

        System.out.println("\nRemovendo registro com chave '19':");
        cliente.busca(server,19);
        cliente.removerRegistro(server, 19);
        System.out.println("\nTentativa de busca pelo registro com chave '19':");
        cliente.busca(server,19);

        System.out.println("\nRemovendo registro com chave '181':");
        cliente.busca(server,181);
        cliente.removerRegistro(server, 181);
        System.out.println("\nTentativa de busca pelo registro com chave '181':");
        cliente.busca(server,181);

        System.out.println("\nRemovendo registro com chave '49':");
        cliente.busca(server,49);
        cliente.removerRegistro(server, 49);
        System.out.println("\nTentativa de busca pelo registro com chave '49':");
        cliente.busca(server,49);

        System.out.println("\nRemovendo registro com chave '449':");
        cliente.busca(server,449);
        cliente.removerRegistro(server, 449);
        System.out.println("\nTentativa de busca pelo registro com chave '449':");
        cliente.busca(server,449);

        System.out.println("\nRemovendo registro com chave '7769':");
        cliente.busca(server,7769);
        cliente.removerRegistro(server, 7769);
        System.out.println("\nTentativa de busca pelo registro com chave '7769':");
        cliente.busca(server,7769);

        System.out.println("\nRemovendo registro com chave '6669':");
        cliente.busca(server,6669);
        cliente.removerRegistro(server, 6669);
        System.out.println("\nTentativa de busca pelo registro com chave '6669':");
        cliente.busca(server,6669);

        System.out.println("\nRemovendo registro com chave '1790':");
        cliente.busca(server, 1790);
        cliente.removerRegistro(server,  1790);
        System.out.println("\nTentativa de busca pelo registro com chave '1790':");
        cliente.busca(server, 1790);

        System.out.println("\nRemovendo registro com chave '1500':");
        cliente.busca(server,1500);
        cliente.removerRegistro(server, 1500);
        System.out.println("\nTentativa de busca pelo registro com chave '1500':");
        cliente.busca(server,1500);
        
        System.out.println("\nRemovendo registro com chave '1822':");
        cliente.busca(server,1822);
        cliente.removerRegistro(server, 1822);
        System.out.println("\nTentativa de busca pelo registro com chave '1822':");
        cliente.busca(server,1822);}

        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        {System.out.println("\nRemovendo registro com chave '166':");
        cliente.busca(server,166);
        cliente.removerRegistro(server, 166);
        System.out.println("\nTentativa de busca pelo registro com chave '166':");
        cliente.busca(server,166);

        System.out.println("\nRemovendo registro com chave '192':");
        cliente.busca(server,192);
        cliente.removerRegistro(server, 192);
        System.out.println("\nTentativa de busca pelo registro com chave '192':");
        cliente.busca(server,192);

        System.out.println("\nRemovendo registro com chave '9991':");
        cliente.busca(server,9991);
        cliente.removerRegistro(server, 9991);
        System.out.println("\nTentativa de busca pelo registro com chave '9991':");
        cliente.busca(server,9991);

        System.out.println("\nRemovendo registro com chave '7777':");
        cliente.busca(server,7777);
        cliente.removerRegistro(server, 7777);
        System.out.println("\nTentativa de busca pelo registro com chave '7777':");
        cliente.busca(server,7777);

        System.out.println("\nRemovendo registro com chave '77':");
        cliente.busca(server,77);
        cliente.removerRegistro(server, 77);
        System.out.println("\nTentativa de busca pelo registro com chave '77':");
        cliente.busca(server,77);

        System.out.println("\nRemovendo registro com chave '3':");
        cliente.busca(server,3);
        cliente.removerRegistro(server, 3);
        System.out.println("\nTentativa de busca pelo registro com chave '3':");
        cliente.busca(server,3);

        System.out.println("\nRemovendo registro com chave '577':");
        cliente.busca(server,577);
        cliente.removerRegistro(server, 577);
        System.out.println("\nTentativa de busca pelo registro com chave '577':");
        cliente.busca(server,577);

        System.out.println("\nRemovendo registro com chave '1347':");
        cliente.busca(server, 1347);
        cliente.removerRegistro(server,  1347);
        System.out.println("\nTentativa de busca pelo registro com chave '1347':");
        cliente.busca(server, 1347);

        System.out.println("\nRemovendo registro com chave '5432':");
        cliente.busca(server,5432);
        cliente.removerRegistro(server, 5432);
        System.out.println("\nTentativa de busca pelo registro com chave '5432':");
        cliente.busca(server,5432);
        
        System.out.println("\nRemovendo registro com chave '8577':");
        cliente.busca(server,8577);
        cliente.removerRegistro(server, 8577);
        System.out.println("\nTentativa de busca pelo registro com chave '8577':");
        cliente.busca(server,8577);}

        System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();

        {System.out.println("\nRemovendo registro com chave '8919':");
        cliente.busca(server,8919);
        cliente.removerRegistro(server, 8919);
        System.out.println("\nTentativa de busca pelo registro com chave '8919':");
        cliente.busca(server,8919);

        System.out.println("\nRemovendo registro com chave '2719':");
        cliente.busca(server,2719);
        cliente.removerRegistro(server, 2719);
        System.out.println("\nTentativa de busca pelo registro com chave '2719':");
        cliente.busca(server,2719);

        System.out.println("\nRemovendo registro com chave '8641':");
        cliente.busca(server,8641);
        cliente.removerRegistro(server, 8641);
        System.out.println("\nTentativa de busca pelo registro com chave '8641':");
        cliente.busca(server,8641);

        System.out.println("\nRemovendo registro com chave '8797':");
        cliente.busca(server,8797);
        cliente.removerRegistro(server, 8797);
        System.out.println("\nTentativa de busca pelo registro com chave '8797':");
        cliente.busca(server,8797);

        System.out.println("\nRemovendo registro com chave '2332':");
        cliente.busca(server,2332);
        cliente.removerRegistro(server, 2332);
        System.out.println("\nTentativa de busca pelo registro com chave '2332':");
        cliente.busca(server,2332);

        System.out.println("\nRemovendo registro com chave '8777':");
        cliente.busca(server,8777);
        cliente.removerRegistro(server, 8777);
        System.out.println("\nTentativa de busca pelo registro com chave '8777':");
        cliente.busca(server,8777);

        System.out.println("\nRemovendo registro com chave '9777':");
        cliente.busca(server,9777);
        cliente.removerRegistro(server, 9777);
        System.out.println("\nTentativa de busca pelo registro com chave '9777':");
        cliente.busca(server,9777);

        System.out.println("\nRemovendo registro com chave '1234':");
        cliente.busca(server, 1234);
        cliente.removerRegistro(server,  1234);
        System.out.println("\nTentativa de busca pelo registro com chave '1234':");
        cliente.busca(server, 1234);

        System.out.println("\nRemovendo registro com chave '9':");
        cliente.busca(server,9);
        cliente.removerRegistro(server, 9);
        System.out.println("\nTentativa de busca pelo registro com chave '9':");
        cliente.busca(server,9);
        
        System.out.println("\nRemovendo registro com chave '680':");
        cliente.busca(server,680);
        cliente.removerRegistro(server, 680);
        System.out.println("\nTentativa de busca pelo registro com chave '680':");
        cliente.busca(server,680);
}
        
    System.out.println("\n----------Pressione 'enter' para prosseguir...");
        scanner.nextLine();
    }
}
