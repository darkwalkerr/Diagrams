package com.server.diagramserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.SocketException;

public class DiagramServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private String senddata;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Сервер запущен на порту: " + port);

        while (true) {
            clientSocket = serverSocket.accept();
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = now.format(formatter);
            // Выводим сообщение о подключении клиента с текущим временем
            System.out.println("Подключился клиент: " + clientSocket.getInetAddress().toString() + " в " + formattedTime);
            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            clientThread.start();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                if (senddata != null && !senddata.isEmpty()) {
                    sendData(senddata);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendData(String data) {
        try {
            if (out != null) {
                out.println(data);
                out.flush(); // Flush output stream to ensure immediate sending
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (out != null) {
                out.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (SocketException e) {
            // Подавляем SocketException
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void setDataToSend(String data) {
        this.senddata = data;
    }

    public String getSendData() {
        return senddata;
    }
}
