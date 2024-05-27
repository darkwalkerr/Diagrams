package com.diagramclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Загружаем FXML файл для интерфейса клиента
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("ClientView.fxml"));
        // Создаем сцену с загруженным интерфейсом
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        // Устанавливаем заголовок окна
        stage.setTitle("Diagram Client");
        // Устанавливаем сцену на окно
        stage.setScene(scene);
        // Отображаем окно
        stage.show();
    }

    public static void main(String[] args) {
        // Запуск JavaFX приложения
        launch(args);
    }
}
