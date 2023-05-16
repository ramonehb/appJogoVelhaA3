package Server;

import java.io.IOException;
import java.net.ServerSocket;
public class Server {
    public static void main(String[] args) {
        new Server().executar();
    }

    public void executar() {
        try {
            var serverSocket = new ServerSocket(12345);
            System.out.println("Servidor aguardando conexões...");

            while (true) {
                var socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}