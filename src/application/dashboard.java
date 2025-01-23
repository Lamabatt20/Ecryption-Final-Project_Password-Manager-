package application;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;  
import java.sql.*;

public class dashboard {
	public static StackPane root;
	public static Pane dashboard(Stage primaryStage, int userID) {
		Image backgroundImage = new Image("background1.png");
		ImageView backgroundView = new ImageView(backgroundImage);
		backgroundView.setPreserveRatio(false);
		backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
		backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());

		TableView<PasswordEntry> tableView = new TableView<>();
		tableView.setStyle("-fx-background-color: black; " + "-fx-control-inner-background: black; "
				+ "-fx-control-inner-background-alt: black; " + "-fx-border-color: white; -fx-font-size: 25px;");

		tableView.setRowFactory(tv -> {
			TableRow<PasswordEntry> row = new TableRow<>();
			row.setAlignment(Pos.CENTER);
			return row;
		});

		String buttonStyle = "-fx-background-color: transparent; -fx-padding: 0;";

		TableColumn<PasswordEntry, String> accountTypeColumn = new TableColumn<>("Account Type");
		accountTypeColumn.setCellValueFactory(cellData -> cellData.getValue().accountTypeProperty());
		accountTypeColumn.setStyle("-fx-alignment: CENTER;");

		TableColumn<PasswordEntry, String> usernameColumn = new TableColumn<>("Username");
		usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
		usernameColumn.setStyle("-fx-alignment: CENTER;");

		TableColumn<PasswordEntry, String> passwordColumn = new TableColumn<>("Password");
		passwordColumn.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
		passwordColumn.setCellFactory(column -> new TableCell<>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? null : "******");
				setStyle("-fx-alignment: CENTER;");
			}
		});

		TableColumn<PasswordEntry, Void> viewColumn = createViewColumn();
		TableColumn<PasswordEntry, Void> editColumn = createEditColumn(userID);
		TableColumn<PasswordEntry, Void> deleteColumn = createDeleteColumn(userID);

		tableView.getColumns().addAll(accountTypeColumn, usernameColumn, passwordColumn, viewColumn, editColumn,
				deleteColumn);

		double totalColumnWidth = 150 * 3 + 80 * 3;
		tableView.setPrefWidth(totalColumnWidth);
		tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

		// Fetch data for this specific user (UserID)
		ObservableList<PasswordEntry> data = getPasswordData(userID);

		tableView.setItems(data);

		ScrollPane tableScrollPane = new ScrollPane(tableView);
		tableScrollPane.setFitToWidth(true);
		tableScrollPane.setFitToHeight(true);

		Label titleLabel = new Label("Password Manager");
		titleLabel.setStyle("-fx-font-size: 50px; -fx-text-fill: white;");
		titleLabel.setAlignment(Pos.CENTER);

		Button addButton = new Button("Add New Pass");
		addButton.setStyle("-fx-font-size: 18px; -fx-background-color: #7cfff3; -fx-text-fill: black;");
		addButton.setOnAction(e -> addNewPassword(data,userID));
		
		Button logoutButton = new Button("Logout");
		logoutButton.setStyle("-fx-font-size: 18px; -fx-background-color: #7cfff3; -fx-text-fill: black;");
		logoutButton.setOnAction(e -> {
			Pane encodeBorderPane = new BorderPane();
            encodeBorderPane = login.createLoginPane(primaryStage);
            Scene encodeScene = new Scene(encodeBorderPane);
            primaryStage.setScene(encodeScene);
            primaryStage.setFullScreen(true);
		});
		VBox tableContainer = new VBox();
		tableContainer.setAlignment(Pos.CENTER);
		tableContainer.getChildren().add(tableScrollPane);
		tableContainer.setMaxWidth(totalColumnWidth + 50);

		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		VBox containerBox = new VBox(15);
		containerBox.setAlignment(Pos.CENTER);
		containerBox.getChildren().addAll(titleLabel, tableContainer, addButton, logoutButton);

		 root = new StackPane();
		root.getChildren().addAll(backgroundView, containerBox);

		return root;
	}

	private static ObservableList<PasswordEntry> getPasswordData(int userID) {
		ObservableList<PasswordEntry> data = FXCollections.observableArrayList();

		// Database query to fetch passwords for the given UserID
		String query = "SELECT * FROM useraccounts WHERE UserID = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, userID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String accountType = rs.getString("AccountName");
				String username = rs.getString("AccountUserName");
				String password = rs.getString("AccountPassword");

				data.add(new PasswordEntry(accountType, username, password));
			}

		} catch (SQLException ex) {
			System.out.println("Database connection failed!");
			ex.printStackTrace();
		}

		return data;
	}

	// Add the methods for creating the view, edit, and delete columns
	private static TableColumn<PasswordEntry, Void> createViewColumn() {
		TableColumn<PasswordEntry, Void> viewColumn = new TableColumn<>("View");
		viewColumn.setStyle("-fx-alignment: CENTER;");
		viewColumn.setCellFactory(column -> new TableCell<>() {
			private final Button viewButton = new Button();
			{
				ImageView viewIcon = new ImageView(new Image("view.png", 20, 20, true, true));
				viewButton.setGraphic(viewIcon);
				viewButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
				viewButton.setOnAction(e -> {
					PasswordEntry entry = getTableView().getItems().get(getIndex());
					viewPassword(entry);
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : viewButton);
			}
		});

		return viewColumn;
	}

	private static TableColumn<PasswordEntry, Void> createEditColumn(int userID) {
		TableColumn<PasswordEntry, Void> editColumn = new TableColumn<>("Edit");
		editColumn.setStyle("-fx-alignment: CENTER;");
		editColumn.setCellFactory(column -> new TableCell<>() {
			private final Button editButton = new Button();
			{
				ImageView editIcon = new ImageView(new Image("edit.png", 20, 20, true, true));
				editButton.setGraphic(editIcon);
				editButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
				editButton.setOnAction(e -> {
					PasswordEntry entry = getTableView().getItems().get(getIndex());
					editEntry(entry,userID);
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : editButton);
			}
		});

		return editColumn;
	}

	private static TableColumn<PasswordEntry, Void> createDeleteColumn(int userID) {
		TableColumn<PasswordEntry, Void> deleteColumn = new TableColumn<>("Delete");
		deleteColumn.setStyle("-fx-alignment: CENTER;");
		deleteColumn.setCellFactory(column -> new TableCell<>() {
			private final Button deleteButton = new Button();
			{
				ImageView deleteIcon = new ImageView(new Image("delete.png", 20, 20, true, true));
				deleteButton.setGraphic(deleteIcon);
				deleteButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
				deleteButton.setOnAction(e -> {
					PasswordEntry entry = getTableView().getItems().get(getIndex());
					deletePasswordFromDatabase(entry,userID);
					getTableView().getItems().remove(entry);
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : deleteButton);
			}
		});

		return deleteColumn;
	}

	private static void editEntry(PasswordEntry entry,int userID) {
	    String decryptedPassword = HillCipher.decrypt(entry.getPassword().toUpperCase());
		TextInputDialog dialog = new TextInputDialog(decryptedPassword);
		dialog.setTitle("Edit Password");
		dialog.setHeaderText("Edit Password for " + entry.getAccountType());
		dialog.setContentText("New Password:");

		dialog.showAndWait().ifPresent(newPassword -> {
			entry.setPassword(newPassword);
			updatePasswordInDatabase(entry, userID);
		});
	}

	private static void viewPassword(PasswordEntry entry) {
	    String decryptedPassword = HillCipher.decrypt(entry.getPassword().toUpperCase());

	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("View Password");
	    alert.setHeaderText("Password for " + entry.getAccountType());
	    alert.setContentText("Password: " + decryptedPassword);
	    alert.showAndWait();
	}

	private static void addNewPassword(ObservableList<PasswordEntry> data, int userID) {
		Dialog<PasswordEntry> dialog = new Dialog<>();
		dialog.setTitle("Add New Password");
		dialog.setHeaderText("Enter Account Details");

		GridPane grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setAlignment(Pos.CENTER);

		ComboBox<String> accountTypeComboBox = new ComboBox<>();
		accountTypeComboBox.getItems().addAll("Facebook", "Email", "LinkedIn", "Twitter", "Instagram", "Google",
				"GitHub", "Netflix", "Amazon", "Bank", "Other");
		accountTypeComboBox.setPromptText("Select Account Type");
		grid.add(new Label("Account Type:"), 0, 0);
		grid.add(accountTypeComboBox, 1, 0);

		TextField usernameField = new TextField();
		usernameField.setPromptText("Enter Username");
		grid.add(new Label("Username:"), 0, 1);
		grid.add(usernameField, 1, 1);

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter Password");
		grid.add(new Label("Password:"), 0, 2);
		grid.add(passwordField, 1, 2);

		dialog.getDialogPane().setContent(grid);

		ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);

		dialog.setResultConverter(button -> {
			if (button == saveButton) {
				String accountType = accountTypeComboBox.getValue();
				String username = usernameField.getText();
				String password = passwordField.getText();

				if (accountType != null && !username.isEmpty() && !password.isEmpty()) {
					PasswordEntry newEntry = new PasswordEntry(accountType, username, password);
					insertNewPasswordIntoDatabase(newEntry,userID);
					return newEntry;
				} else {
					return null;
				}
			}
			return null;
		});

		dialog.showAndWait().ifPresent(entry -> {
			data.add(entry);
		});
	}

	private static void insertNewPasswordIntoDatabase(PasswordEntry entry, int userID) {
	    String encryptedPassword = HillCipher.encrypt(entry.getPassword());

	    String query = "INSERT INTO useraccounts (UserID, AccountName, AccountUserName, AccountPassword) VALUES (?, ?, ?, ?)";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setInt(1, userID);
	        pstmt.setString(2, entry.getAccountType());
	        pstmt.setString(3, entry.getUsername());
	        pstmt.setString(4, encryptedPassword); 

	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("New password entry added to database successfully!");
	            showDeleteSuccessMessage(root , "New password entry added to database successfully!");
	        } else {
	            System.out.println("Failed to add new password entry to database.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Database error while inserting new password.");
	        e.printStackTrace();
	    }
	}


	private static void updatePasswordInDatabase(PasswordEntry entry, int userID) {
	    String encryptedPassword = HillCipher.encrypt(entry.getPassword());

	    String query = "UPDATE useraccounts SET AccountPassword = ? WHERE UserID = ? AND AccountName = ? AND AccountUserName = ?";

	    try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setString(1, encryptedPassword); 
	        pstmt.setInt(2, userID);
	        pstmt.setString(3, entry.getAccountType());
	        pstmt.setString(4, entry.getUsername());

	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Password updated successfully in the database.");
	            showDeleteSuccessMessage(root , "Password updated successfully in the database!");
	        } else {
	            System.out.println("Failed to update password in the database.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Database error while updating password.");
	        e.printStackTrace();
	    }
	}

	private static void deletePasswordFromDatabase(PasswordEntry entry, int userID) {
		String query = "DELETE FROM useraccounts WHERE UserID = ? AND AccountName = ? AND AccountUserName = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, userID); 
			pstmt.setString(2, entry.getAccountType()); 
			pstmt.setString(3, entry.getUsername()); 

			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Password deleted successfully from the database.");
	            showDeleteSuccessMessage(root , "Password deleted successfully!");

			} else {
				System.out.println("Failed to delete password from the database.");
			}
		} catch (SQLException e) {
			System.out.println("Database error while deleting password.");
			e.printStackTrace();
		}
	}
	

private static void showDeleteSuccessMessage(StackPane root , String Message) {
    Label successMessage =	new Label(Message);
    successMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-background-color: green; -fx-padding: 10px; -fx-background-radius: 5px;");
    
    StackPane.setAlignment(successMessage, Pos.BOTTOM_CENTER);
    root.getChildren().add(successMessage);
    
    PauseTransition pause = new PauseTransition(Duration.seconds(4));
    
    pause.setOnFinished(event -> root.getChildren().remove(successMessage)); 
    pause.play();
}


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
