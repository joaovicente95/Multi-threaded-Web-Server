
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe StartService
 * <p>
 * Thread para aceitar os pedidos ao servidor
 *
 * @author Joao Vicente
 */
class StartService extends Thread {
    private ServerSocket theServer;
    private List<Socket> Clients = new ArrayList<>();

    public void run() {
        try {
            theServer = new ServerSocket(4321);
            System.out.println("Server ready: " + theServer);
            try {
                while (true) {
                    // Blocks until receive a new connection
                    Socket sock = theServer.accept();
                    Clients.add(sock);
                    try {
                        new ClientThread(sock);
                    } catch (Exception e) {
                        sock.close();
                    }
                }

            } finally {
                theServer.close();
                Clients.clear();
                System.out.println("Server stop: " + theServer);
                theServer = null;

            }
        } catch (Exception e) {
        }

    }

    /**
     * Metodo fechar
     */
    public void fechar() {
        try {
            theServer.close();
            Clients.clear();
        } catch (Exception e) {
            System.out.println("Nenhum servidor lançado");
        }
    }

    /**
     * Método listCon
     * <p>
     * Tem o objetivo de listar todas as ligações ativas ao servidor
     */
    public void listCon() {
        if (theServer == null) {
            System.out.println("Sem Servidor");
        }else if (Clients.isEmpty()) {
            System.out.println("Sem ligações");
        }
        else {
            for (int i = 0; i < Clients.size(); i++) {
                System.out.println(Clients.get(i));
            }
        }
    }

}

/**
 * Classe WebService
 * <p>
 * Parte do servidor, onde é lançado e parado a thread para aceitar ligações, e onde
 * é mostrado todos os clientes ligados ao servidor
 *
 * @author Joao Vicente
 */
public class WebService {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        String f10;
        StartService s1 = new StartService();
        while (true) {
            System.out.print("> ");
            f10 = reader.next().toUpperCase();
            switch (f10) {
                case "START":
                    try {
                        s1 = new StartService();
                        s1.start();
                    } catch (Exception e) {
                        System.out.println("Servidor já lançado");
                    }
                    break;
                case "STOP":
                    s1.fechar();
                    break;
                case "EXIT":
                    System.exit(1);
                    break;
                case "LIST":
                    s1.listCon();
                    break;
                default:
                    System.out.println("Command Invalid");
            }
        }
    }
}