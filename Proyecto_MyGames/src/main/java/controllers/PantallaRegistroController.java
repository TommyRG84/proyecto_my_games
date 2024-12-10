package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import app.mainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilJDBC.ConexionMySQL;

public class PantallaRegistroController {

	private Parent root;
	private Stage stage;
	private Scene scene;
	private String avatarSeleccionado;

	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnCreaCuenta;

	@FXML
	private ComboBox<String> cbGeneroFav;

	@FXML
	private DatePicker dtFechaNac;

	@FXML
	private ImageView imgAvatar1;

	@FXML
	private ImageView imgAvatar2;

	@FXML
	private PasswordField pwPassword;

	@FXML
	private PasswordField pwPassword2;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtUsername;

	@FXML
	public void initialize() {
		// Carga las imágenes y las asigna a los ImageView
		Image avatar1 = new Image(getClass().getResourceAsStream("/avatar1.png"));
		Image avatar2 = new Image(getClass().getResourceAsStream("/avatar2.png"));

		imgAvatar1.setImage(avatar1);
		imgAvatar2.setImage(avatar2);

		// Añadir opciones al ComboBox
		ObservableList<String> generos = FXCollections.observableArrayList("Acción", "Indie", "Aventuras", "RPG",
				"Estrategia", "Shooter", "Simulación", "Puzle", "Arcade", "Plataformas", "Carreras", "Deportes", "Lucha",
				"Educativos");

		cbGeneroFav.setItems(generos);

	}

	@FXML
	void btnCancelarPressed(ActionEvent event) throws IOException {
		root = FXMLLoader.load(mainApp.class.getResource("/views/PantallaLogin.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void btnCreaCuentaPressed(ActionEvent event) {
		String username = txtUsername.getText();
		String nombre = txtNombre.getText();
		String email = txtEmail.getText();
		String password = pwPassword.getText();
		String confirmPassword = pwPassword2.getText();
		String generoFavorito = cbGeneroFav.getValue();
		LocalDate fechaNacimiento = dtFechaNac.getValue();

		// Validaciones
		if (username.isEmpty() || nombre.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
				|| avatarSeleccionado == null) {
			showAlert(AlertType.WARNING, "Campos incompletos", "Por favor, rellena todos los campos.");
			return;
		}

		if (!password.equals(confirmPassword)) {
			showAlert(AlertType.ERROR, "Contraseña no coincide", "Las contraseñas no coinciden.");
			return;
		}

		if (fechaNacimiento != null && fechaNacimiento.isAfter(LocalDate.now())) {
			showAlert(AlertType.ERROR, "Fecha inválida", "La fecha de nacimiento debe ser anterior a hoy.");
			return;
		}

		Connection conexion = ConexionMySQL.conectar();

		String queryComprobacion = "SELECT * FROM usuarios WHERE username = ? OR correo = ?";
		String queryInsercion = """
				INSERT INTO usuarios (username, nombre, correo, fecha_nacimiento, genero_preferido, contraseña, fecha_registro)
				VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
				""";

		try (PreparedStatement pstmtComprobacion = conexion.prepareStatement(queryComprobacion);
				PreparedStatement pstmtInsercion = conexion.prepareStatement(queryInsercion)) {

			// Verifica si el usuario o correo ya existen
			pstmtComprobacion.setString(1, username);
			pstmtComprobacion.setString(2, email);

			ResultSet rs = pstmtComprobacion.executeQuery();
			if (rs.next()) {
				showAlert(AlertType.ERROR, "Usuario o correo en uso", "El usuario o correo ya está registrado.");
				return;
			}

			// Inserta nuevo usuario
			pstmtInsercion.setString(1, username);
			pstmtInsercion.setString(2, nombre);
			pstmtInsercion.setString(3, email);
			// Manejo de posibles valores nulos para fecha de nacimiento y género favorito
			if (fechaNacimiento != null) {
				pstmtInsercion.setDate(4, java.sql.Date.valueOf(fechaNacimiento));
			} else {
				pstmtInsercion.setNull(4, java.sql.Types.DATE);
			}

			if (generoFavorito != null) {
				pstmtInsercion.setString(5, generoFavorito);
			} else {
				pstmtInsercion.setNull(5, java.sql.Types.VARCHAR);
			}
			pstmtInsercion.setString(6, password);

			pstmtInsercion.executeUpdate();
			showAlert(AlertType.INFORMATION, "Registro exitoso", "¡El usuario se ha registrado correctamente!");

			// Cierra la conexión y vuelve a la pantalla de login
			conexion.close();
			btnCancelarPressed(event);

		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error de base de datos", "No se pudo completar el registro.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void imgAvatar1Clicked(MouseEvent event) {
		avatarSeleccionado = "avatar1.png";
		resaltaAvatar(imgAvatar1, imgAvatar2);
	}

	@FXML
	void imgAvatar2Clicked(MouseEvent event) {
		avatarSeleccionado = "avatar2.png";
		resaltaAvatar(imgAvatar2, imgAvatar1);
	}

	// Método para resaltar el avatar seleccionado
	private void resaltaAvatar(ImageView seleccionado, ImageView noSeleccionado) {
		seleccionado
				.setStyle("-fx-effect: dropshadow(gaussian, blue, 20, 0, 0, 0); -fx-border-color: blue; -fx-border-width: 3;");
		noSeleccionado.setStyle("-fx-effect: none; -fx-border-color: transparent;");
	}

	// Método auxiliar para mostrar Alertas
	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.setHeaderText(null);
		alert.showAndWait();
	}

}