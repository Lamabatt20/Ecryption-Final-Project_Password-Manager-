package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

import java.io.File;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            VBox buttonBox = new VBox(20);
            buttonBox.setStyle("-fx-alignment: center; -fx-padding: 20;");
            buttonBox.setTranslateY(-250); 

            Button loginButton = new Button("Login");
            loginButton.setFont(Font.font(30));
            loginButton.setStyle("-fx-background-color: #7cfff3; -fx-text-fill: BLACK;"); 

            loginButton.setOnAction(e -> {            	
               Pane encodeBorderPane = new BorderPane();
               encodeBorderPane = login.createLoginPane(primaryStage);
               Scene encodeScene = new Scene(encodeBorderPane);
               primaryStage.setScene(encodeScene);
               primaryStage.setFullScreen(true);
            });

            Button signUpButton = new Button("Sign Up");
            signUpButton.setFont(Font.font(30));
            signUpButton.setStyle("-fx-background-color: #7cfff3; -fx-text-fill: BLACK;"); 

            signUpButton.setOnAction(e ->  
            {
                Pane encodeBorderPane = new BorderPane();
                encodeBorderPane = signup.createSignupPane(primaryStage);
                Scene encodeScene = new Scene(encodeBorderPane);
                primaryStage.setScene(encodeScene);
                primaryStage.setFullScreen(true);
             });

            buttonBox.getChildren().addAll(loginButton, signUpButton);

            BorderPane root = new BorderPane();

            Media media = new Media(new File("background_video.mp4").toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();

            MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());
            mediaView.setPreserveRatio(false);

            Pane videoPane = new Pane(mediaView);

            root.setCenter(videoPane);
            root.setBottom(buttonBox);

            Scene mainScene = new Scene(root, 1920, 1080);
            primaryStage.setScene(mainScene);
            primaryStage.setFullScreen(true);
            primaryStage.show();
            
           login.back.setOnAction(e -> {
                primaryStage.setScene(mainScene);
                primaryStage.setFullScreen(true);
            });
            signup.backk.setOnAction(e -> {
            	primaryStage.setScene(mainScene);
                primaryStage.setFullScreen(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showSignUpScene(Stage stage) {
        StackPane signUpPane = new StackPane();
        Label signUpLabel = new Label("Sign-Up Page");
        signUpLabel.setFont(Font.font(24));
        signUpPane.getChildren().add(signUpLabel);

        Scene signUpScene = new Scene(signUpPane, 800, 600);
        stage.setScene(signUpScene);
    }

    public static void main(String[] args) {
  
        launch(args);
        
    }
}
