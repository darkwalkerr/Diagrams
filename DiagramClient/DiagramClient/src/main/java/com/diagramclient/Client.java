package com.diagramclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private Socket clientSocket; // Сокет клиента
    private BufferedReader in; // Входной поток для чтения данных от сервера

    // Метод для начала соединения с сервером
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port); // Подключение к серверу по указанному IP и порту
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Инициализация входного потока
    }

    // Метод для получения данных от сервера
    public String receiveData() throws IOException {
        if (in == null) {
            throw new IOException("Соединение с сервером не установлено"); // Исключение, если соединение не установлено
        }
        StringBuilder data = new StringBuilder(); // Строка для накопления данных
        String line;
        while ((line = in.readLine()) != null) { // Чтение данных построчно
            data.append(line).append("\n"); // Добавление прочитанной строки к накопленным данным
        }
        return data.toString(); // Возвращение накопленных данных в виде строки
    }

    // Метод для остановки соединения
    public void stopConnection() throws IOException {
        try {
            if (in != null) {
                in.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            // Подавляем IOException
        }
    }
}
