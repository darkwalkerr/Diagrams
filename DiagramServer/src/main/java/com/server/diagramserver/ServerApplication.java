package com.server.diagramserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.SocketException;


public class ServerApplication extends Application {

    private int port = 7777; // Номер порта для сервера
    private boolean isrunning;
    private DiagramServer server; // Объявление экземпляра DiagramServer

    public boolean isRunning() {
        return isrunning;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("ServerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 550);
        stage.setTitle("Diagram server");
        stage.setScene(scene);
        stage.show();
        isrunning = false;

        // Создание экземпляра DiagramServer
        server = new DiagramServer();

        // Передача порта и ссылки на этот экземпляр ServerApplication в контроллер
        ServerController controller = fxmlLoader.getController();
        controller.setPort(port);
        controller.setServerApplication(this);
        controller.setServer(server);
    }

    @Override
    public void stop() throws Exception {
        // Остановка сервера при закрытии приложения
        stopServer();
        super.stop();
    }

    // Метод для запуска сервера
    public  void startServer() {
        if (!isRunning()) {
            try {
                isrunning = true;
                server.start(port);
                System.out.println("Сервер запущен на порту: " + port);
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибки запуска сервера
            }
        } else {
            System.out.println("Сервер уже запущен.");
        }
    }

    // Метод для остановки сервера
    public  void stopServer() {
        if (isRunning()) {
            if (server != null) {
                try {
                    isrunning = false;
                    server.stop();
                    System.out.println("Сервер остановлен.");
                } catch (Exception e) {
                    // Подавляем Exception
                }
            }
        } else {
            System.out.println("Сервер уже остановлен.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
