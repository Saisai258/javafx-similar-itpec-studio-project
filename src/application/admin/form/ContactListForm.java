package application.admin.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ContactListForm {
    public AnchorPane contactListForm() throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/application/admin/ContactList.fxml"));
//        root.getStylesheets().add(getClass().getResource("D:\\Eclipse\\JavaWorkspace\\ITPECOldQuestionProject\\src\\style.css").toExternalForm());

        return root;
    }
}
