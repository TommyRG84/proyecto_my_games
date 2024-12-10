package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class mainApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Font.loadFont(getClass().getResource("/PressStart2P.ttf").toExternalForm(), 10);

			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(mainApp.class.getResource("/views/PantallaLogin.fxml"));

			Pane ventana = (Pane) loader.load();

			Scene scene = new Scene(ventana);
			scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
