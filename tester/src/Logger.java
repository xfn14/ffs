import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Logger {

    private File logs;
    private String mensagem;

    public Logger() {
        this.logs = new File("logs.txt");
        this.mensagem = "";
    }

    public void writeLog(String msg, File logs) throws IOException {
        if (logs.exists()) {
            try {
                FileWriter writer = new FileWriter("logs.txt", true);
                writer.write(msg + "\n");
                writer.close();
            } catch (IOException e) {
                System.out.println("Ocorreu um erro");
            }
        }
    }

    public void main(String[] args) throws IOException {
        File logs = new File("logs.txt");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduza os parâmetros: ");
        while (scanner.hasNext()) {
            String mensagem = scanner.next();
            writeLog(mensagem, logs);
        }
    }
}
