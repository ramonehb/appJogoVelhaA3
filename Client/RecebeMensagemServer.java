package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class RecebeMensagemServer implements Runnable{

    private Socket socket;
    private char[] tabuleiro;
    
    public RecebeMensagemServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner leitor = new Scanner(socket.getInputStream());
            while (leitor.hasNextLine()){
                var mensagem = leitor.nextLine();

                if (mensagem.equals("JOGADOR_1")) {
                    System.out.println("Você é o jogador 1 (X)");
                } else if (mensagem.equals("JOGADOR_2")) {
                    System.out.println("Você é o jogador 2 (O)");
                } else if(mensagem.equals("INICIAR_JOGO")){
                    System.out.println("Jogo da Velha iniciado");
                    tabuleiro = new char[9];
                    exibirTabuleiro(tabuleiro);
                } else if (mensagem.startsWith("JOGADA_VALIDA")) {
                    System.out.println("Tabuleiro atual:");

                    var partes = mensagem.split(";");
                    var posicao = Integer.parseInt(partes[1]);
                    var jogador = partes[2].charAt(0);

                    limpaTerminal();
                    atualizarTabuleiro(posicao, jogador);
                    exibirTabuleiro(tabuleiro);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private void exibirTabuleiro(char[] tabuleiro) {
        System.out.println("  0 | 1 | 2 ");
        System.out.println("-----------");
        System.out.println("  3 | 4 | 5 ");
        System.out.println("-----------");
        System.out.println("  6 | 7 | 8 ");
        System.out.println();

        System.out.println("  " + tabuleiro[0] + " | " + tabuleiro[1] + " | " + tabuleiro[2]);
        System.out.println("-----------");
        System.out.println("  " + tabuleiro[3] + " | " + tabuleiro[4] + " | " + tabuleiro[5]);
        System.out.println("-----------");
        System.out.println("  " + tabuleiro[6] + " | " + tabuleiro[7] + " | " + tabuleiro[8]);
        System.out.println();
    }

    private void atualizarTabuleiro(int posicao, char jogador) {
        tabuleiro[posicao] = jogador;
    }
    
    private void limpaTerminal(){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "cls");

            Process process = processBuilder.inheritIO().start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
