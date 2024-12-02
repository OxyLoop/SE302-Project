 
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #001f3f; -fx-padding: 20;");

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
        BorderPane.setAlignment(addCourseButton, Pos.TOP_LEFT);
        root.setTop(addCourseButton);

        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-alignment: center;");

        Label title = new Label("Timetable");
        title.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-padding: 10;"
        );

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

        vbox.getChildren().addAll(title, searchBar, submitButton);

        root.setCenter(vbox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Timetable");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openNewWindow(String searchText) {
        Stage newWindow = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: top-center; -fx-padding: 20;");
        vbox.setAlignment(Pos.TOP_CENTER);

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

        Label title = new Label("Add Course");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #001f3f;");

        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField nameField = new TextField();
        nameField.setPromptText("");

        Label codeLabel = new Label("Code:");
        codeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField codeField = new TextField();
        codeField.setPromptText("");

        Label timingLabel = new Label("Timing:");
        timingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField timingField = new TextField();
        timingField.setPromptText("");

        Label lecturerLabel = new Label("Lecturer:");
        lecturerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #001f3f;");
        TextField lecturerField = new TextField();
        lecturerField.setPromptText("");

        Button addButton = new Button("Add");
        addButton.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-background-color: #d3d3d3;" +
            "-fx-text-fill: #001f3f;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 5 20;"
        );
        addButton.setOnAction(event -> newWindow.close());

        vbox.getChildren().addAll(
            title,
            nameLabel, nameField,
            codeLabel, codeField,
            timingLabel, timingField,
            lecturerLabel, lecturerField,
            addButton
        );

        Scene newScene = new Scene(vbox, 300, 400);
        newWindow.setTitle("Add New Course Tab");
        newWindow.setScene(newScene);
        newWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
