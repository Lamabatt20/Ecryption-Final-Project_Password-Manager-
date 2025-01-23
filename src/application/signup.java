package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    public static int userID;
    static Button backk = new Button("");

    public static Pane createSignupPane(Stage primaryStage) {
        Image backgroundImage = new Image("background2.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);

        backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: white;");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(300);

        Label emailLabel = new Label("Useremail:");
        emailLabel.setStyle("-fx-text-fill: white;");
        emailLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your Email");
        emailField.setMaxWidth(300);

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

        Button SignUp = new Button("Sign Up");
        SignUp.setFont(Font.font(16));
        SignUp.setStyle("-fx-background-color: #7cfff3; -fx-text-fill: black;-fx-font-size: 18px;");
        SignUp.setMaxWidth(300);

        Label errorLabel = new Label("Invalid username or password.");
        errorLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);

        VBox containerBox = new VBox(10);
        containerBox.setAlignment(Pos.CENTER);
        containerBox.setPrefSize(400, 350);
        containerBox.setTranslateY(40);

        HBox usernameHBox = new HBox(10);
        usernameHBox.setAlignment(Pos.CENTER);
        usernameHBox.getChildren().addAll(usernameLabel, usernameField);

        HBox emailHBox = new HBox(10);
        emailHBox.setAlignment(Pos.CENTER);
        emailHBox.getChildren().addAll(emailLabel, emailField);

        HBox passwordHBox = new HBox(10);
        passwordHBox.setAlignment(Pos.CENTER);
        passwordHBox.getChildren().addAll(passwordLabel, passwordField);

        HBox confirmPasswordHBox = new HBox(10);
        confirmPasswordHBox.setAlignment(Pos.CENTER);
        confirmPasswordHBox.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);

        // Back Button
        Image backIcon = new Image("back.png");
        ImageView backIconView = new ImageView(backIcon);
        backIconView.setFitHeight(60);
        backIconView.setFitWidth(60);
        backk.setGraphic(backIconView);
        backk.setStyle("-fx-background-color: transparent;");

        containerBox.getChildren().addAll(
                usernameHBox, emailHBox, passwordHBox, confirmPasswordHBox,
                SignUp, errorLabel
        );

        SignUp.setOnAction(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (password.equals(confirmPassword)) {
                Pane encodeBorderPane = new BorderPane();
                encodeBorderPane = dashboard.dashboard(primaryStage, addUserToDatabase(username, email, password));
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

        // StackPane for background and content
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, containerBox);

        // Align the back button to the top-left corner
        StackPane.setAlignment(backk, Pos.TOP_LEFT);
        root.getChildren().add(backk);  // Add back button to root

        return root;
    }

    private static int addUserToDatabase(String username, String email, String password) {
        String insertQuery = "INSERT INTO users (UserName, UserEmail, UserPassword) VALUES (?, ?, ?)";
        String selectQuery = "SELECT LAST_INSERT_ID()";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            // Encrypt the password using HillCipher
            String encryptedPassword = HillCipher.encrypt(password);

            // Setting values for the insert query
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, encryptedPassword);

            // Execute the insert query
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery(selectQuery);
                    if (rs.next()) {
                        userID = rs.getInt(1);
                        System.out.println("User added successfully! UserID: " + userID);
                        return userID;
                    }
                }
            }
            return -1;

        } catch (SQLException e) {
            System.out.println("Database error while adding new user.");
            e.printStackTrace();
            return -1;
        }
    }

    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Driver found!");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
            e.printStackTrace();
            throw new SQLException("Unable to load JDBC driver");
        }

        String dbUrl = "jdbc:mysql://localhost:3306/passwordmanager";
        String dbUsername = "root";
        String dbPassword = "";

        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
}
