package application.teacher.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import application.teacher.form.ProfileForm;
import application.main.DatabaseConnection;

public class ColorPickerController {

    @FXML
    private GridPane colorGrid; // Ensure it's initialized in FXML

    @FXML
    private GridPane avatarGrid; // Ensure it's initialized in FXML

    @FXML
    private ImageView sImage; // Ensure it's linked in FXML
    double radius = 55; // Adjust as needed

    private Stage colorPickerStage;
  
    private Consumer<Color> colorCallback; // Callback for color selection
    private Consumer<String> avatarCallback; // Callback for avatar selection
    
    public void setStage(Stage stage) {
        this.colorPickerStage = stage;
        colorPickerStage.setY(110);
        colorPickerStage.setX(500);
        colorPickerStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                colorPickerStage.close();
            }
        });
    }

    // Method to set color callback
    public void setColorCallback(Consumer<Color> callback) {
        this.colorCallback = callback;
    }

    // Method to set avatar callback
    public void setAvatarCallback(Consumer<String> callback) {
        this.avatarCallback = callback;
    }
    @FXML
    public void initialize() {
        loadColors();
        loadAvatars();
    }

    private void loadColors() {
        String[] colors = {"#dc143c", "#ff5733", "#33ff57", "#3357ff", "#ffd700", 
        		"#800080","#00ffff","#ff4500", "#ff1493", "#32cd32", "#8a2be2", "#7fffd4", 
                "#1e90ff", "#ff6347", "#6a5acd", "#ff8c00", "#ff00ff", "#2e8b57"};

        int col = 0, row = 0;
        for (String color : colors) {
            Circle circle = new Circle(25, Color.web(color)); // Create circle with radius 25

            // Add click event to select color
            circle.setOnMouseClicked(event -> {
                if (colorCallback != null) {
                    colorCallback.accept(Color.web(color)); // Send selected color back
                }
                keepProfileFormOpen(); // Keep profile form open
            });

            colorGrid.add(circle, col, row); // Add circle to grid
            col++;
            if (col ==7) { // 3 circles per row
                col = 0;
                row++;
            }
        }
    }

    private void loadAvatars() {
        // List of image paths (Absolute local paths)
        List<String> imagePaths = Arrays.asList(
             "D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av1.jpg","D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av3.jpg",
             "D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av7.jpg","D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av22.jpg",
            "D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av4.jpg","D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av8.jpg",
            "D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av5.jpg","D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av9.jpg",
            "D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av10.jpg","D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av11.jpg",
            "D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av12.jpg","D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av17.jpg",
            "D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av14.jpg","D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av15.jpg",
            "D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av18.jpg","D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av19.png",
            "D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av20.png","D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av21.jpg",
            "D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\application\\images/av24.webp"
        		);

        int col = 0, row = 0;
        for (String avatarPath : imagePaths) {
            // Use File to access local images
            File file = new File(avatarPath);
            if (!file.exists()) {
                continue; // Skip if the image doesn't exist
            }

            // Load the image from the file
            Image avatarImage = new Image(file.toURI().toString());
            ImageView avatarView = new ImageView(avatarImage);
            avatarView.setFitWidth(50);
            avatarView.setFitHeight(50);
            avatarView.setPreserveRatio(true);  // Maintain aspect ratio
            avatarView.setVisible(true);        // Ensure it’s visible
            
            // Create a circular clip
            double radius = avatarView.getFitWidth() / 2;
            Circle clip = new Circle(radius, radius, radius);
            avatarView.setClip(clip);
            
            // Set click handler for selecting the avatar
            avatarView.setOnMouseClicked(e -> {
				try {
					selectAvatar(avatarPath);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				  keepProfileFormOpen(); // Keep profile form open
			});

            // Add ImageView to the GridPane
            avatarGrid.add(avatarView, col, row);

            col++;
            if (col == 7) { // 4 avatars per row
                col = 0;
                row++;
            }
        }
    }


    private void selectAvatar(String avatarPath) throws IOException {
    	
    	// Update the sImage with the selected avatar
        if (sImage != null) {
            Image image = new Image("file:" + avatarPath); // Use file path if it's on the file system
            sImage.setImage(image);
            
        }

        // Save the avatar path in the database
        saveAvatarToDatabase(avatarPath);
        
        if (avatarCallback != null) {
            avatarCallback.accept(avatarPath); // Send selected avatar path back
            
        }
//        closeWindow();
    }
    
    private void saveAvatarToDatabase(String avatarPath) throws IOException {
        try {
            // Create a connection to the database
            DatabaseConnection db = new DatabaseConnection();
            Connection con = db.getConnetion();
            
            // Create a File object from the avatar path (assuming it's a valid path string)
            File avatarFile = new File(avatarPath);
            
            // Get the size of the image (in bytes)
            long avatarSize = avatarFile.length(); // Get size in bytes
            
            // Prepare SQL statement to update the avatar photo and size for the current user
            
            String sql = "UPDATE teacher SET photo=?, size=? WHERE teacher_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            // Read the image file as a byte stream
            FileInputStream fis = new FileInputStream(avatarFile);
            
            // Set the parameters for the SQL statement
            pst.setBinaryStream(1, fis, (int) avatarSize); // Save the binary image data
            pst.setLong(2, avatarSize); // Save the image size (as long)
            pst.setString(3, TeacherLoginController.login_teacher.getTeacher_id()); // Use the logged-in user's ID
            
            // Execute the update
            pst.executeUpdate();
            
            // Close the FileInputStream and connection
            fis.close();
            pst.close();
            con.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the error appropriately (e.g., show a message to the user)
        }
    }

    private void keepProfileFormOpen() {
        if (ProfileForm.profileStage != null && !ProfileForm.profileStage.isShowing()) {
            ProfileForm.profileStage.show();
        }
    }
    @FXML
    private void handleClose(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) colorGrid.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

   

    

}
