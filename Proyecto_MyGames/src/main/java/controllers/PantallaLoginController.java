package controllers;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilJDBC.ConexionMySQL;

public class PantallaLoginController {

	private Parent root;
	private Stage stage;
	private Scene scene;

	@FXML
	private Button btnCreaCuenta;

	@FXML
	private Button btnLogin;

	@FXML
	private Button btnRestablecePass;

	@FXML
	private PasswordField pwPassword;

	@FXML
	private TextField txtUsuario;

	@FXML
	void btnCreaCuentaPressed(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PantallaRegistro.fxml"));
		root = loader.load();

		// PantallaRegistroController pantRegController = loader.getController();
		// vent2Controller.escribeMensaje(txtUsuario.getText());

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void btnLoginPressed(ActionEvent event) {
		String username = txtUsuario.getText();
		String password = pwPassword.getText();

		if (username.isEmpty() || password.isEmpty()) {
			showAlert(AlertType.WARNING, "Campo vacío", "Por favor, introduce el usuario y la contraseña.");
			return;
		}

		Connection conexion = ConexionMySQL.conectar();
		String query = "SELECT * FROM usuarios WHERE username = ? AND contraseña = ?";

		try (PreparedStatement sentenciaPreparada = conexion.prepareStatement(query)) {

			sentenciaPreparada.setString(1, username);
			sentenciaPreparada.setString(2, password);

			ResultSet rs = sentenciaPreparada.executeQuery();

			if (rs.next()) {
				// Si existe el usuario, navega a la pantalla principal
				showAlert(AlertType.INFORMATION, "Inicio de sesión exitoso", "¡Bienvenido, " + username + "!");
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PantallaPrincipal.fxml"));
				root = loader.load();

				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} else {
				// Si no existe el usuario, mostrar alerta
				showAlert(AlertType.ERROR, "Error de inicio de sesión", "Usuario o contraseña incorrectos.");
			}

			conexion.close();

		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error de conexión", "No se pudo conectar con la base de datos.");
		}

	}

	@FXML
	void btnRestablecePassPressed(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PantallaRestableceContrasenya.fxml"));
		root = loader.load();

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// Método para mostrar Alertas
	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.setHeaderText(null);
		alert.showAndWait();
	}

}