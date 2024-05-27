package com.server.diagramserver;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Random;

public class ServerController {

    private ServerApplication serverApplication; // Ссылка на экземпляр ServerApplication
    private DiagramServer server; // Ссылка на экземпляр DiagramServer

    @FXML
    private Label welcomeText; // Метка для отображения информации
    @FXML
    private TextField sectorCount; // Текстовое поле для ввода количества секторов
    @FXML
    private PieChart diagramChart; // Графический элемент для отображения диаграммы
    private int port; // Номер порта

    // Метод для установки номера порта
    public void setPort(int port) {
        this.port = port;
        welcomeText.setText("Порт сервера: " + port); // Обновление текста метки с номером порта
    }

    // Метод для установки ссылки на ServerApplication
    public void setServerApplication(ServerApplication serverApplication) {
        this.serverApplication = serverApplication;
    }

    // Метод для установки ссылки на DiagramServer
    public void setServer(DiagramServer server) {
        this.server = server;
    }

    // Метод, вызываемый при нажатии кнопки "Сгенерировать диаграмму"
    @FXML
    protected void onGenerateDiagramButtonClick() {
        diagramChart.getData().clear(); // Очистка данных диаграммы

        try {
            int count = Integer.parseInt(sectorCount.getText()); // Получение количества секторов из текстового поля
            Random random = new Random();
            int totalValue = 360;
            String sendString = ""; // Инициализация строки для отправки

            for (int i = 0; i < count - 1; i++) {
                int value = random.nextInt(totalValue); // Генерация случайного значения сектора
                totalValue -= value;
                int percent = (int) ((value * 100) / 360); // Расчет процента
                PieChart.Data slice = new PieChart.Data("Сектор" + (i + 1) + "-" + value + "(" + percent + "%)", value);
                sendString += value + ", "; // Добавление значения в строку для отправки
                diagramChart.getData().add(slice); // Добавление сектора в диаграмму
            }
            int percent = (int) ((totalValue * 100) / 360);
            PieChart.Data lastSlice = new PieChart.Data("Сектор" + count + "-" + totalValue + "(" + percent + "%)", totalValue);
            diagramChart.getData().add(lastSlice); // Добавление последнего сектора в диаграмму
            sendString += totalValue; // Добавление последнего значения в строку для отправки

            // Установка строки для отправки в DiagramServer
            server.setDataToSend(sendString);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR); // Создание предупреждения об ошибке
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, введите число секторов");
            alert.showAndWait(); // Отображение предупреждения
        }
    }

    // Метод, вызываемый при нажатии кнопки "Старт"
    @FXML
    protected void onStartButtonClick() {
        if (server.getSendData() == null || server.getSendData().isEmpty()) { // Проверка наличия данных для отправки
            Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
            informationAlert.setTitle("Information Dialog");
            informationAlert.setHeaderText(null);
            informationAlert.setContentText("Нужно создать диаграмму!");
            informationAlert.showAndWait();
            return;
        }

        if (serverApplication != null) {
            new Thread(() -> {
                if (!serverApplication.isRunning()) { // Проверка, не запущен ли сервер
                    Platform.runLater(() -> welcomeText.setText("Сервер запущен!")); // Обновление текста
                    serverApplication.startServer(); // Запуск сервера
                } else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Сервер уже запущен.");
                        alert.showAndWait();
                    });
                }
            }).start();
        }
    }

    // Метод, вызываемый при нажатии кнопки "Стоп"
    @FXML
    protected void onStopButtonClick() {
        new Thread(() -> {
            if (serverApplication != null && serverApplication.isRunning()) { // Проверка, запущен ли сервер
                serverApplication.stopServer(); // Остановка сервера
                Platform.runLater(() -> welcomeText.setText("Сервер остановлен!")); // Обновление текста
            } else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Сервер уже остановлен.");
                    alert.showAndWait();
                });
            }
        }).start();
    }
}
