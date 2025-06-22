package application.admin.form;
  
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class AdminMainForm extends Application {
  @Override
  public void start(Stage primaryStage) {
    try {
    	AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/application/admin/Sample.fxml"));
      Scene scene = new Scene(root,400,400);
      scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
      primaryStage.setResizable(false);
      primaryStage.getIcons().add(new Image("downLogo.jpg"));
      primaryStage.setTitle("Quiz Mania Ultimate Challenge");
      primaryStage.setScene(scene);
      primaryStage.getIcons().add(new Image("file:/C:/Java Programming/JavaWorkspace/MYProject/images/ppp.jpg")); 
   // Avoid profile form being closed during opening
      primaryStage.iconifiedProperty().addListener((obs, wasMinimized, isNowMinimized) -> {
          if (isNowMinimized && ProfileForm.canBeSafelyClosed()) {
              ProfileForm.closeForm();
              AdminUpdateProfileForm.closeForm();
          }
      });

      primaryStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
          if (!isNowFocused && ProfileForm.canBeSafelyClosed()) {
              ProfileForm.closeForm();
              AdminUpdateProfileForm.closeForm();
          }
      });
      primaryStage.show();
    } catch(Exception e) {
      e.printStackTrace();   
    }
  }
}