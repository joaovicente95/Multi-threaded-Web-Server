import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe ServFiles
 *
 * @author Joao Vicente
 */
public class ServFiles {

    /**
     * Método ReadPag
     * <p>
     * Tem como objetivo ler um ficheiro, caso esse ficheiro nao exista
     * é lido a pagina de erro
     *
     * @param uri página a ser carregada
     * @return Retorna um array de bytes com a página pretendida ou a página de erro     
     */
    public byte[] readPag(String uri) {
        Path path;
        byte[] con = null;
        try {
            path = Paths.get("Site", uri);
            con = Files.readAllBytes(path);
        } catch (Exception e) {
            path = Paths.get("Site", "status404.html");
            try {
                con = Files.readAllBytes(path);
                // Parte para colocar o link que foi pedido na página
                String str = new String(con, "UTF-8");
                str = str.replace("[PAGE]", uri);
                con = str.getBytes();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return con;
    }

    /**
     * Método fileType
     * <p>
     * Tem como objetivo de verificar a extenção de um ficheiro
     *
     * @param filename Ficheiro pretendido
     * @return Retorna uma string com o tipo de ficheiro
     */
    public String fileType(String filename) {
        String fileType = "undertermined";
        // Parte para verificar se o url tem parametros no link
        if (filename.contains("?")) {
            String f[] = filename.split("\\?");
            filename = f[0].toString();
        }
        final File file = new File(filename);

        try {
            fileType = Files.probeContentType(file.toPath());
        } catch (IOException ioException) {
            System.out.println(
                    "ERROR: Unable to determine file type for " + filename + "due to Exception" + ioException
            );
        }

        return fileType;
    }

    /**
     * Método lookFile
     * <p>
     * Tem como obejtivo de verificar se um ficheiro existe
     *
     * @param path Caminho para o ficheiro
     * @return Retorna verdadeiro se o ficheiro existir e falso se não existir
     */
    public boolean lookFile(String path) {
        boolean flag = false;
        File fl = new File("Site" + path);
        if (fl.exists()) {
            flag = true;
        }
        return flag;
    }

    /**
     * Método gPostPar
     * <p>
     * Tem como objetivo de ir buscar os parametros de um request feito com o método POST
     *
     * @param req StringBuuilder que contém o request
     * @return Retorna uma string com os parametros
     */
    public String gPostPar(StringBuilder req) {
        String s22[] = new String[]{};
        s22 = req.toString().split("\r\n");
        return s22[s22.length - 1];
    }

    /**
     * Método pagPost
     * <p>
     * Tem como objetivo de construir a página com os dados recebidos de um post
     *
     * @param pPost    String com os dados que foram recebidos do post
     * @param method   Método de envio
     * @param uri      Tipo de dados a serem enviados
     * @param protocol Protocolo de envio
     * @param code     Codigo de envio
     * @param des      Descriçao do codigo de envio
     * @param status   Estado da ligação
     * @return Retorna ja o header e a página
     */
    public String pagPost(String pPost, String method, String uri, String protocol, String code, String des, String status) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        String formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").format(new Date()).toString();
        String S[] = pPost.split("&");


        sb2.append("<html><head><title>Post</title></head><body><h1>Dados recebidos</h1>");
        sb2.append(S[0]).append("<br/>");
        sb2.append(S[1]).append("<br/>");
        sb2.append(S[2]).append("<br/>");
        sb2.append("</body></html>");

        sb.append(method + protocol + code + des).append("\r\n");
        sb.append("Date: ").append(formatter).append("\r\n");
        sb.append("Server: Test Server").append("\r\n");
        sb.append("Connection: " + status).append("\r\n");
        sb.append("Content-Type: " + uri).append("\r\n");
        sb.append("Content-length: " + sb2.length()).append("\r\n");
        sb.append("\r\n");
        sb.append(sb2.toString());

        return sb.toString();
    }
}
