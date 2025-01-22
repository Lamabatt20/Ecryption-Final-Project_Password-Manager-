package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class signup {

    public static Pane createSignupPane(Stage primaryStage) {
        Image backgroundImage = new Image("background2.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);

        backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());

        VBox containerBox = new VBox(10);
        containerBox.setAlignment(Pos.CENTER);
        containerBox.setPrefSize(400, 350); 
        containerBox.setTranslateY(40);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: white;");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(300); 

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: white;");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(300); 

        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordLabel.setStyle("-fx-text-fill: white;");
        confirmPasswordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.setMaxWidth(300); 
        Button loginButton = new Button("Sign Up");
        loginButton.setFont(Font.font(16));
        loginButton.setStyle("-fx-background-color: #7cfff3; -fx-text-fill: black;");
        loginButton.setMaxWidth(300); 

        loginButton.setOnAction(e -> {
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            
            if (password.equals(confirmPassword)) {
                Pane encodeBorderPane = new BorderPane();
                encodeBorderPane = dashboard.dashboard(primaryStage);
                Scene encodeScene = new Scene(encodeBorderPane);
                primaryStage.setScene(encodeScene);
                primaryStage.setFullScreen(true);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Password Mismatch");
                alert.setHeaderText(null);
                alert.setContentText("The passwords do not match. Please try again.");
                alert.showAndWait();
            }
        });

        Label errorLabel = new Label("Invalid username or password.");
        errorLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);

        containerBox.getChildren().addAll(
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                confirmPasswordLabel, confirmPasswordField, 
                loginButton, errorLabel
        );

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, containerBox);
        return root;
    }
}
