package application.admin.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageCropperApp extends Application {

    private ImageView imageView = new ImageView();
    private Canvas canvas;
    private double startX, startY, endX, endY;
    private double centerX = 200, centerY = 200, radius = 100; // Circle center and radius for cropping
    private Image selectedImage;
    private Circle cropOverlay;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();

        // FileChooser button
        Button btnSelect = new Button("Select Image");
        btnSelect.setOnAction(e -> selectImage(primaryStage));

        // Crop button
        Button btnCrop = new Button("Crop Image");
        btnCrop.setOnAction(e -> cropImage());

        StackPane imageContainer = new StackPane();
        canvas = new Canvas(400, 400);

        cropOverlay = new Circle(centerX, centerY, radius);
        cropOverlay.setStroke(Color.RED);
        cropOverlay.setStrokeWidth(2);
        cropOverlay.setFill(Color.TRANSPARENT);

        imageContainer.getChildren().addAll(imageView, canvas, cropOverlay);

        root.getChildren().addAll(btnSelect, imageContainer, btnCrop);
        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.setTitle("Image Cropper");
        primaryStage.show();
    }

    private void selectImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedImage = new Image(file.toURI().toString());
            imageView.setImage(selectedImage);
            imageView.setFitWidth(400);
            imageView.setPreserveRatio(true);
        }
    }

    private void cropImage() {
        if (selectedImage == null) {
            System.out.println("No image selected!");
            return;
        }

        // Coordinates and dimensions for the circular crop
        int cropX = (int) (centerX - radius);
        int cropY = (int) (centerY - radius);
        int cropWidth = (int) (2 * radius);
        int cropHeight = (int) (2 * radius);

        // Ensure the crop dimensions are within the image bounds
        int imageWidth = (int) selectedImage.getWidth();
        int imageHeight = (int) selectedImage.getHeight();
        
        // Adjust crop dimensions if the circle goes out of bounds
        cropX = Math.max(0, Math.min(cropX, imageWidth - cropWidth));
        cropY = Math.max(0, Math.min(cropY, imageHeight - cropHeight));

        if (cropWidth <= 0 || cropHeight <= 0) {
            System.out.println("Invalid crop dimensions!");
            return;
        }

        WritableImage croppedImage = new WritableImage(
            selectedImage.getPixelReader(), cropX, cropY, cropWidth, cropHeight
        );

        imageView.setImage(croppedImage);
        saveImage(croppedImage);
    }

    private void saveImage(WritableImage croppedImage) {
        File outputFile = new File("cropped_image.png");
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(croppedImage, null);
        try {
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Image saved: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Mouse event to draw a circular cropping area
    @SuppressWarnings("unused")
	private void onMousePressed(MouseEvent event) {
        startX = event.getX();
        startY = event.getY();
    }

    @SuppressWarnings("unused")
	private void onMouseDragged(MouseEvent event) {
        endX = event.getX();
        endY = event.getY();
        double dx = endX - startX;
        double dy = endY - startY;
        radius = Math.sqrt(dx * dx + dy * dy);
        cropOverlay.setRadius(radius);
    }

    @SuppressWarnings("unused")
	private void onMouseReleased(MouseEvent event) {
        // Finalize the crop overlay size
        endX = event.getX();
        endY = event.getY();
        double dx = endX - startX;
        double dy = endY - startY;
        radius = Math.sqrt(dx * dx + dy * dy);
        cropOverlay.setRadius(radius);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
