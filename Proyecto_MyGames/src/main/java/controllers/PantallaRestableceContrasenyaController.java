package controllers;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import app.mainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilJDBC.ConexionMySQL;

public class PantallaRestableceContrasenyaController {

	private Parent root;
	private Stage stage;
	private Scene scene;

	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnResetPass;

	@FXML
	private TextField txtEmail;

	@FXML
	void btnCancelarPressed(ActionEvent event) throws IOException {
		root = FXMLLoader.load(mainApp.class.getResource("/views/PantallaLogin.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void btnResetPassPressed(ActionEvent event) {
		String email = txtEmail.getText();

		if (email.isEmpty()) {
			showAlert(AlertType.WARNING, "Campo vacío", "Por favor, introduce tu correo electrónico.");
			return;
		}

		Connection conexion = ConexionMySQL.conectar();

		// Verificar si el correo electrónico existe en la base de datos
		String query = "SELECT * FROM usuarios WHERE correo = ?";
		try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				// El correo existe, generar una nueva contraseña
				String nuevaContrasena = generateRandomPassword();

				// Actualizar la base de datos con la nueva contraseña
				String updateQuery = "UPDATE usuarios SET contraseña = ? WHERE correo = ?";
				try (PreparedStatement pstmtUpdate = conexion.prepareStatement(updateQuery)) {
					pstmtUpdate.setString(1, nuevaContrasena);
					pstmtUpdate.setString(2, email);
					pstmtUpdate.executeUpdate();

					// Enviar el correo con la nueva contraseña
					sendEmail(email, nuevaContrasena);

					showAlert(AlertType.INFORMATION, "Éxito", "La contraseña ha sido restablecida y enviada a tu correo.");

				} catch (SQLException e) {
					e.printStackTrace();
					showAlert(AlertType.ERROR, "Error de base de datos", "No se pudo actualizar la contraseña.");
				}
			} else {
				showAlert(AlertType.ERROR, "Correo no encontrado", "No existe un usuario con ese correo.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error de base de datos", "No se pudo verificar el correo.");
		}

	}

//Método para generar una nueva contraseña aleatoria
	private String generateRandomPassword() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < 8; i++) {
			int index = random.nextInt(characters.length());
			password.append(characters.charAt(index));
		}

		return password.toString();
	}

	// Método para enviar el correo electrónico con la nueva contraseña
	private void sendEmail(String recipientEmail, String newPassword) {
		String from = "aaacande@gmail.com"; // Tu correo
		String host = "smtp.gmail.com"; // Host del servidor SMTP (usando Gmail como ejemplo)
		String user = "aaacande@gmail.com"; // Tu correo
		String password = "nner rwhj pfqi hcvu"; // Contraseña de tu correo (recomendado usar una App Password si usas Gmail)

		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
			message.setSubject("Restablecimiento de contraseña");
			message.setText(
					"Hola, \n\nTu nueva contraseña es: " + newPassword + "\n\nPor favor, inicia sesión con esta contraseña.");

			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error al enviar correo", "No se pudo enviar el correo.");
		}
	}

	// Método para mostrar alertas
	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}