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

public class login {

    public static Pane createLoginPane(Stage primaryStage) {
        Image backgroundImage = new Image("background.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);


        backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());

        VBox containerBox = new VBox(10);
        containerBox.setAlignment(Pos.CENTER);
        containerBox.setPrefSize(400, 300);
        containerBox.setTranslateY(25); 


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

        Button signupButton = new Button("Log In");
        signupButton.setFont(Font.font(16));
        signupButton.setStyle("-fx-background-color: #7cfff3; -fx-text-fill: black;");
        signupButton.setMaxWidth(300);
        signupButton.setOnAction(e -> {            	
            Pane encodeBorderPane = new BorderPane();
            encodeBorderPane = dashboard.dashboard(primaryStage);
            Scene encodeScene = new Scene(encodeBorderPane);
            primaryStage.setScene(encodeScene);
            primaryStage.setFullScreen(true);
         });

        Label errorLabel = new Label("Invalid username or password.");
        errorLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);

     //  loginButton.setOnAction(e -> {
       //     String username = usernameField.getText();
         //   String password = passwordField.getText();

           // if ("admin".equals(username) && "password".equals(password)) {
             //   Alert alert = new Alert(Alert.AlertType.INFORMATION);
               // alert.setTitle("Login Successful");
                //alert.setHeaderText(null);
                //alert.setContentText("Welcome, " + username + "!");
                //alert.showAndWait();

         //   } else {
         //       errorLabel.setVisible(true);
         //   }
     //   });

        containerBox.getChildren().addAll(
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                signupButton, errorLabel
        );

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, containerBox);


        return root;
    }
}
