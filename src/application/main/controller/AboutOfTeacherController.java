package application.main.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.main.DatabaseConnection;
import application.teacher.form.TeacherLoginForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class AboutOfTeacherController implements Initializable {

    @FXML
    private Label txtdescription;

    @FXML
    private Canvas canvas;

    private int currentId = -1;
    private List<Integer> list_id;

    @FXML
    void btnNext(ActionEvent event) {
        int currentIndex = list_id.indexOf(currentId);
        if (currentIndex != -1 && currentIndex < list_id.size() - 1) {
            currentId = list_id.get(currentIndex + 1);
            extractData(currentId);
        } else {
            System.out.println("No next record available.");
        }
    }

    @FXML
    void btnBack(ActionEvent event) {
        int currentIndex = list_id.indexOf(currentId);
        if (currentIndex > 0) {
            currentId = list_id.get(currentIndex - 1);
            extractData(currentId);
        } else {
            System.out.println("No previous record available.");
        }
    }

    @FXML
    void btnTeacher(ActionEvent event) throws IOException {
        new TeacherLoginForm().toggleTeacherLoginForm();;
    }

    public void extractData(int id) {
        DatabaseConnection db = new DatabaseConnection();
        try (Connection con = db.getConnetion();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT image, description FROM teacherinformation WHERE id = ? AND action = 1")) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String text = rs.getString("description");
                txtdescription.setText(text != null ? text : "No description available.");

                byte[] imageData = rs.getBytes("image");
                if (imageData != null && imageData.length > 0) {
                    Image image = new Image(new ByteArrayInputStream(imageData));
                    drawCircularImage(image);
                } else {
                    System.out.println("No image found.");
                }
            } else {
                System.out.println("No data found for ID: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void drawCircularImage(Image image) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        gc.save();
        gc.beginPath();
        gc.arc(width / 2, height / 2, width / 2, height / 2, 0, 360);
        gc.closePath();
        gc.clip();

        gc.drawImage(image, 0, 0, width, height);
        gc.restore();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TeacherAbout info = new TeacherAbout();
        list_id = info.getAllIds();

        if (!list_id.isEmpty()) {
            currentId = list_id.get(0);
            extractData(currentId);
        } else {
            txtdescription.setText("No teacher information available.");
            System.out.println("No teachers with action = 1 found.");
        }
    }
}

class TeacherAbout {

    public List<Integer> getAllIds() {
        List<Integer> idList = new ArrayList<>();
        DatabaseConnection db = new DatabaseConnection();

        try (Connection con = db.getConnetion();
             PreparedStatement pst = con.prepareStatement("SELECT id FROM teacherinformation WHERE action = 1");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                idList.add(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idList;
    }
}
