import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ContentHandler;
import java.net.Socket;

/**
 * Classe HttpRR
 *
 * @author Joao Vicente
 */
public class ClientThread extends Thread {
    private Socket sock;
    private PrintStream out;
    private BufferedReader in;

    /**
     * Construtor da class
     */
    public ClientThread(Socket s) throws IOException {
        sock = s;
        out = new PrintStream(sock.getOutputStream());
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        start();
    }


    public void run() {
        System.out.println("Accepted connection: " + sock);
        StringBuilder sb = new StringBuilder();
        // Ficheiros e algumas verificações
        ServFiles gt = new ServFiles();
        // Para request e response
        HttpRR gtr = new HttpRR();
        String param[];
        byte[] file;
        while (true) {
            try {
                sb.delete(0, sb.length());
                sb = gtr.httpRequest(in);

                //Verificar se chegou algum erro
                if (sb.toString().equals("E11")) break;

                //String com os seguintes campos: param[0] = method, param[1] = uri, param[2] = protocol
                param = sb.toString().split(" ", 4);

                if (sb.length() != 0) {
                    //Verificar se no pedido o url só tem uma /
                    if (param[1].equals("/"))
                        param[1] += "index.html";

                    //Verificar se o metodo é POST
                    if (param[0].equals("POST")) {
                        out.println(gt.pagPost(gt.gPostPar(sb), param[0], gt.fileType(param[1]), param[2], "200", " ok", "Keep Alive"));
                        continue;
                    }
                    //Verificar se o ficheiro existe
                    if (gt.lookFile(param[1]) == true) {
                        file = gt.readPag(param[1]);
                        out.println(gtr.httpResponse(param[0], gt.fileType(param[1]), param[2], "200", " ok", "Keep Alive", file.length));
                    } else {
                        file = gt.readPag(param[1]);
                        out.println(gtr.httpResponse(param[0], gt.fileType(param[1]), param[2], "404", " Not Found", "Keep Alive", file.length));
                    }

                    //Enviar o ficheiro
                    out.write(file);

                }


            } catch (Exception o) {
                break;
            }
        }
        //No final fechar tudo
        try {
            out.close();
            in.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connection with " + sock + " terminated!");
    }
}
