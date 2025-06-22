package application.main.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class IDGenerateForm {

	public static Stage idForm;
	public Stage iDGenerateForm() throws IOException {
		idForm=new Stage();
		AnchorPane root= (AnchorPane)FXMLLoader.load(getClass().getResource("/application/main/IDGenerate.fxml"));
		Scene sc=new Scene(root,400,255);
		idForm.setScene(sc);
		idForm.initStyle(StageStyle.UNDECORATED);
		idForm.initModality(Modality.APPLICATION_MODAL);
		return idForm;
	} 
}
