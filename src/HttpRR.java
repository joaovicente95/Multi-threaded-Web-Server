import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe HttpRR
 *
 * @author Joao Vicente
 */
public class HttpRR {
    protected static final DateFormat HTTP_DATE_FORMAT = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z");

    /**
     * Método httpResponse
     * <p>
     * Tem o objetivo de construir o header de response para o cliente
     *
     * @param method   Método de envio
     * @param uri      Tipo de dados a serem enviados
     * @param protocol Protocolo de envio
     * @param code     Codigo de envio
     * @param des      Descriçao do codigo de envio
     * @param status   Estado da ligação
     * @param tam      Tamanho dos dados que vai ser enviados
     * @return String com o header do response
     */
    public String httpResponse(String method, String uri, String protocol, String code, String des, String status, int tam) {
        StringBuilder sb = new StringBuilder();

        sb.append(method + protocol + code + des).append("\r\n");
        sb.append("Date: ").append(HTTP_DATE_FORMAT.format(new Date())).append("\r\n");
        sb.append("Server: Test Server").append("\r\n");
        sb.append("Connection: " + status).append("\r\n");
        sb.append("Content-Type: " + uri).append("\r\n");
        sb.append("Content-length: " + tam).append("\r\n");

        return sb.toString();
    }

    /**
     * Método httpRequest
     * <p>
     * Tem o objetivo de esperar por um pedido de resquest e ler esse pedido. Caso aconteça algum erro
     * é enviado na string o texto "E11"
     *     
     * @param in Ligação do bufferreader
     * @return Retorna uma StringBuilder com o request lido ou com um erro
     */
    public StringBuilder httpRequest(BufferedReader in) {
        StringBuilder sb = new StringBuilder();
        String line;
        String metodo = "";
        String total[] = new String[1];
        int lineNumber = 0;

        try {
            while (!(line = in.readLine()).equals("\r\n")) {
                if (line.isEmpty()) break;
                // Buscar o método de envio
                if (lineNumber == 0) {
                    String s[] = line.split(" ");
                    if (s.length == 3) {
                        metodo = s[0];
                        lineNumber++;
                    }
                }
                //Buscar o tamanho de dados a serem lidos, caso existam
                if (line.toUpperCase().contains("LENGTH")) {
                    total = line.split(":");
                }

                sb.append(line + "\r\n");
            }
            // Em caso de ocorrer algum erro
        } catch (Exception e) {
            sb.append("E11");
        }
        // Verificar se o método de envio é post para ler os parametros enviados
        if (metodo.equals("POST")) {
            for (int i = 0; i < Integer.parseInt(total[1].trim()); i++) {
                try {
                    lineNumber = in.read();
                    sb.append((char) lineNumber);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sb.append("\r\n");
        }
        return sb;
    }
}
