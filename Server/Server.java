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
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor aguardando conexões...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket);

                PrintWriter escritor = new PrintWriter(socket.getOutputStream());

                if (escritores.size() > 1) {
                    System.out.println("O jogo so permite dois jogadores por vez");
                }

                escritores.add(escritor);
                Thread thread = new Thread(new TratadorCliente(socket, escritor));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TratadorCliente implements Runnable {
        private Socket socket;

        private PrintWriter escritor;

        public TratadorCliente(Socket socket, PrintWriter escritor) {
            this.socket = socket;
            this.escritor = escritor;
        }

        @Override
        public void run() {
            try {
                Scanner leitor = new Scanner(socket.getInputStream());
                escritor.println(escritores.size() == 1 ? "JOGADOR_1" : "JOGADOR_2");
                escritor.flush();

                if (escritores.size() == 2) {
                    enviarMensagemParaTodos("INICIAR_JOGO");
                    enviarMensagemParaTodos("JOGADOR_ATUAL;" + jogadorAtual);
                }

                while (leitor.hasNextLine()) {
                    String mensagem = leitor.nextLine();

                    if (mensagem.startsWith("jogada:")) {
                        String[] partes = mensagem.split(":");
                        int posicao = Integer.parseInt(partes[1]);
                        char jogador = escritores.indexOf(escritor) == 0 ? 'X' : 'O';
                        numJogadas++;

                        if (jogador == 'X' && jogadorAtual == 1 || jogador == 'O' && jogadorAtual == 2) {
                            enviarMensagemParaTodos("Jogada valida");
                            if (validarJogada(posicao)) {
                                atualizaTabuleiro(posicao, jogador);
                                enviarMensagemParaTodos("JOGADA_VALIDA;" + posicao + ";" + jogador);

                                // Criar metodo de verificar se houve ganhador Samuel/Lary
                                // Validar empate - Felipe
                            }
                        } else {
                            encerrarJogo();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void enviarMensagemParaTodos(String mensagem) {
            for (PrintWriter escritor : escritores) {
                escritor.println(mensagem);
                escritor.flush();
            }
        }

        private boolean validarJogada(int posicao) {
            if (posicao < 0 || posicao >= 9 || tabuleiro[posicao] != '-') {
                return false;
            }
            return true;
        }

        private void atualizaTabuleiro(int posicao, char jogador) {
            tabuleiro[posicao] = jogador;
        }

        private void encerrarJogo() {
            for (PrintWriter escritor : escritores) {
                escritor.println("FIM_JOGO");
                escritor.flush();
            }
            escritores.clear();
            tabuleiro = new char[9];
            for (int i = 0; i < 9; i++) {
                tabuleiro[i] = '-';
            }
            jogadorAtual = 1;
        }
    }
}
