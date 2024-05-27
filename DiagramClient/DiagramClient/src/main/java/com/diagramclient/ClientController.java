package com.diagramclient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.chart.PieChart;
import java.io.IOException;

public class ClientController {
    @FXML
    private Label welcomeText; // Метка для отображения приветственного текста
    @FXML
    private Button getDiagramButton; // Кнопка для получения диаграммы
    @FXML
    private PieChart diagramChart; // Элемент для отображения круговой диаграммы

    private Client client; // Экземпляр класса Client для работы с сервером

    // Метод, вызываемый при нажатии кнопки "Get Diagram"
    @FXML
    protected void onGetDiagramButtonClick() {
        new Thread(() -> {
            try {
                if (client != null) {
                    client.stopConnection(); // Остановка предыдущего соединения, если оно было установлено
                }
                client = new Client(); // Создание нового клиента
                client.startConnection("localhost", 7777); // Установка соединения с сервером
                String data = client.receiveData(); // Получение данных от сервера
                Platform.runLater(() -> {
                    try {
                        updatePieChart(data); // Обновление диаграммы с полученными данными
                    } catch (NumberFormatException e) {
                        showError("Ошибка данных", "Получены некорректные данные: " + e.getMessage());
                    }
                });
                client.stopConnection(); // Остановка соединения после получения данных
            } catch (IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR); // Создание окна с сообщением об ошибке
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Не удалось получить данные с сервера: " + e.getMessage());
                    alert.showAndWait();
                });
            }
        }).start(); // Запуск нового потока для выполнения сетевых операций
    }

    // Метод для обновления круговой диаграммы
    private void updatePieChart(String data) throws NumberFormatException {
        diagramChart.getData().clear(); // Очистить предыдущие данные диаграммы

        String[] values = data.split(","); // Разделение полученных данных на отдельные значения
        for (int i = 0; i < values.length; i++) {
            if (!values[i].isEmpty()) {
                int value = Integer.parseInt(values[i].trim()); // Преобразование строки в целое число
                int percent = (int) ((value * 100) / 360); // Вычисление процента
                PieChart.Data slice = new PieChart.Data("Сектор" + (i + 1) + "-" + value + "(" + percent + "%)", value); // Создание нового сектора диаграммы
                diagramChart.getData().add(slice); // Добавление сектора в диаграмму
            }
        }
    }

    // Метод для отображения ошибки
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Создание окна с сообщением об ошибке
        alert.setTitle(title); // Установка заголовка окна
        alert.setHeaderText(null);
        alert.setContentText(content); // Установка содержимого окна
        alert.showAndWait(); // Отображение окна
    }
}
