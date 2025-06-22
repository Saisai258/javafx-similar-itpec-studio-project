package application.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppMain extends Application {
    public static Stage primaryStage;
    public static BorderPane root;

    @Override
    public void start(Stage p) {
        try {
            primaryStage = new Stage();

            root = FXMLLoader.load(getClass().getResource("/application/main/MainViews.fxml"));

            Scene scene = new Scene(root, 1366, 700);
            primaryStage.setScene(scene);

            primaryStage.setTitle("Quiz Mania Ultimate Challenge");
            primaryStage.getIcons().add(new Image("downLogo.jpg"));

            primaryStage.setMaximized(true);

            // Optional: set the position manually if needed
            primaryStage.setX(100);
            primaryStage.setY(100);

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
