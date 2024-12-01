package example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Ana düzen (BorderPane)
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #001f3f; -fx-padding: 20;"); // Arka plan rengi ve padding

        // Add Course butonu
        Button addCourseButton = new Button("Add Course");
        addCourseButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: white;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        addCourseButton.setOnAction(event -> openAddCourseWindow());
        BorderPane.setAlignment(addCourseButton, Pos.TOP_LEFT); // Sol üst köşe hizası
        root.setTop(addCourseButton);

        // Merkez içerik (VBox)
        VBox vbox = new VBox(20); // 20 piksel dikey boşluk
        vbox.setStyle("-fx-alignment: center;");

        // Timetable başlığı
        Label title = new Label("Timetable");
        title.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-padding: 10;"
        );

        // Arama çubuğu
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search for classes");
        searchBar.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-alignment: center;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 50 5 50;"
        );
        searchBar.setPrefColumnCount(10);

        // Submit butonu
        Button submitButton = new Button("Search");
        submitButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: white;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        submitButton.setOnAction(event -> {
            String searchText = searchBar.getText();
            openNewWindow(searchText);
        });

        // VBox'a başlık, arama çubuğu ve submit butonunu ekle
        vbox.getChildren().addAll(title, searchBar, submitButton);

        root.setCenter(vbox); // VBox'ı merkeze ekle

        // Sahne oluştur ve göster
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Timetable");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openNewWindow(String searchText) {
        Stage newWindow = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: top-center; -fx-padding: 20;");
        vbox.setAlignment(Pos.TOP_CENTER); // Üst hizalama

        Label message = new Label("Details Of Lesson " + searchText);
        message.setStyle("-fx-font-size: 16px; -fx-text-fill: #001f3f;");
        vbox.getChildren().add(message);

        Scene newScene = new Scene(vbox, 300, 200);
        newWindow.setTitle("Lesson Details");
        newWindow.setScene(newScene);
        newWindow.show();
    }

    private void openAddCourseWindow() {
        Stage newWindow = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Başlık
        Label title = new Label("Add Course");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");

        // Name input
        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;"); // Stil ayarı
        TextField nameField = new TextField();
        nameField.setPromptText("");

        // Code input
        Label codeLabel = new Label("Code:");
        codeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField codeField = new TextField();
        codeField.setPromptText("");

        // Timing input
        Label timingLabel = new Label("Timing:");
        timingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField timingField = new TextField();
        timingField.setPromptText("");

        // Lecturer input
        Label lecturerLabel = new Label("Lecturer:");
        lecturerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField lecturerField = new TextField();
        lecturerField.setPromptText("");

        // Add button
        Button addButton = new Button("Add");
        addButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: #d3d3d3;" + // Açık gri arka plan
            "-fx-text-fill: #001f3f;" +       // Koyu mavi yazı
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        
        addButton.setOnAction(event -> newWindow.close());

        // VBox'a etiketler ve giriş alanlarını ekle
        vbox.getChildren().addAll(
            title,
            nameLabel, nameField,
            codeLabel, codeField,
            timingLabel, timingField,
            lecturerLabel, lecturerField,
            addButton
        );

        // Yeni sahne oluştur ve pencereye ata
        Scene newScene = new Scene(vbox, 300, 400);
        newWindow.setTitle("Add New Course Tab");
        newWindow.setScene(newScene);
        newWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
