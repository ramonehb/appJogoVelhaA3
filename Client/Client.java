package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        var leitor = new Scanner(System.in);
        try {
            var socket = new Socket("localhost", 12345);
            System.out.println("Conectado ao servidor.");

        } catch (IOException e) {
            e.printStackTrace();
            leitor.close();
        }
    }
}