package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Server {

    private List<PrintWriter> escritores;
    private char[] tabuleiro;
    private int jogadorAtual;
    private int numJogadas;
    
    public static void main(String[] args) {
        new Server().executar();
    }

    public void executar() {

        escritores = new ArrayList<>();
        tabuleiro = new char[9];
        for (int i = 0; i < 9; i++) {
            tabuleiro[i] = '-';
        }
        jogadorAtual = 1;

        try {
            var serverSocket = new ServerSocket(12345);
            System.out.println("Servidor aguardando conexões...");

            while (true) {
                var socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket);

                var escritor = new PrintWriter(socket.getOutputStream());

                if (escritores.size() > 1) {
                    System.out.println("O jogo so permite dois jogadores por vez");
                }

                escritores.add(escritor);
                var thread = new Thread(new TratadorCliente(socket, escritor));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TratadorCliente implements Runnable{
        private Socket socket;
        
            private PrintWriter escritor;
    
            public TratadorCliente(Socket socket, PrintWriter escritor) {
                this.socket = socket;
                this.escritor = escritor;
            } 
    
            @Override
            public void run() {
                try {
                    var leitor = new Scanner(socket.getInputStream());
                    escritor.println(escritores.size() == 1 ? "JOGADOR_1" : "JOGADOR_2");
                    escritor.flush();
    
                    if (escritores.size() == 2) {
                        enviarMensagemParaTodos("INICIAR_JOGO");
                        enviarMensagemParaTodos("JOGADOR_ATUAL;" + jogadorAtual);
                    }
                } catch (Exception e) {
                }
            }

            private void enviarMensagemParaTodos(String mensagem) {
                for (var escritor : escritores) {
                    escritor.println(mensagem);
                    escritor.flush();
                }
            }
    }
}

