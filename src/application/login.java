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
import java.sql.*;

public class login {
    static Button back = new Button(""); 

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

        // Create HBox for Username label and TextField next to each other
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: white;");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(300);
        HBox usernameHBox = new HBox(10, usernameLabel, usernameField);  // HBox for alignment
        usernameHBox.setAlignment(Pos.CENTER);

        // Create HBox for Password label and PasswordField next to each other
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: white;");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(300);
        HBox passwordHBox = new HBox(10, passwordLabel, passwordField);  // HBox for alignment
        passwordHBox.setAlignment(Pos.CENTER);

        Button LogIn = new Button("LogIn");
        LogIn.setFont(Font.font(16));
        LogIn.setStyle("-fx-background-color: #7cfff3; -fx-text-fill: black;-fx-font-size: 18px; ");
        LogIn.setMaxWidth(300);

        Label errorLabel = new Label("Invalid username or password.");
        errorLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);

        containerBox.getChildren().addAll(
                usernameHBox, passwordHBox,
                LogIn, errorLabel
        );
        
        // Handling login action
        LogIn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            int userID = isValidUser(username, password);

            if (userID != -1) {
                // If credentials are correct, go to dashboard
                Pane encodeBorderPane = new BorderPane();
                encodeBorderPane = dashboard.dashboard(primaryStage ,userID);
                Scene encodeScene = new Scene(encodeBorderPane);
                primaryStage.setScene(encodeScene);
                primaryStage.setFullScreen(true);
                
            } else {
                // If invalid, show error message
                errorLabel.setVisible(true);
            }
        });

        // Setting up the back button
        Image backIcon = new Image("back.png");
        ImageView backIconView = new ImageView(backIcon);
        backIconView.setFitHeight(60); 
        backIconView.setFitWidth(60); 
        back.setGraphic(backIconView);
        back.setStyle("-fx-background-color: transparent;");

        // Create StackPane and add background and container
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, containerBox);

        // Align the back button to the top-left corner of the StackPane
        StackPane.setAlignment(back, Pos.TOP_LEFT);
        root.getChildren().add(back);  // Add the back button to the StackPane

        // Return the complete pane
        return root;
    }

    private static int isValidUser(String username, String password) {
        int userID = -1;
        String query = "SELECT * FROM users WHERE UserName = ?";

        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String encryptedPasswordFromDB = rs.getString("UserPassword");
               
                // Encrypt the input password using the same logic
                String encryptedInputPassword = HillCipher.encrypt(password);

                // Compare the encrypted input password with the one in the database
                if (encryptedInputPassword.equals(encryptedPasswordFromDB)) {
                    userID = rs.getInt("UserID");
                    System.out.println("User email: " + rs.getString("UserEmail"));
                }
            }

        } catch (SQLException ex) {
            System.out.println("Database connection failed!");
            ex.printStackTrace();
        }
        return userID;
    }


    // Method to create and return a connection to the database
    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Driver  found!");
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
