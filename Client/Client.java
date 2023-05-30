package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Conectado ao servidor.");

            PrintWriter escritor = new PrintWriter(socket.getOutputStream());
            Thread thread = new Thread(new RecebeMensagemServer(socket));
            thread.start();

            while (true) {
                var mensagem = leitor.nextLine();
                escritor.println("jogada:" + mensagem);
                escritor.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            leitor.close();
        }
    }
}