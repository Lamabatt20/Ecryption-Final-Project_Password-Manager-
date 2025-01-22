package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class dashboard {

    public static Pane dashboard(Stage primaryStage) {
        Image backgroundImage = new Image("background1.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());

        TableView<PasswordEntry> tableView = new TableView<>();
        tableView.setStyle(
            "-fx-background-color: black; " +
            "-fx-control-inner-background: black; " +
            "-fx-control-inner-background-alt: black; " +
            "-fx-border-color: white; -fx-font-size: 25px;");

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

        TableColumn<PasswordEntry, Void> viewColumn = new TableColumn<>("View");
        viewColumn.setStyle("-fx-alignment: CENTER;");
        viewColumn.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button();
            {
                ImageView viewIcon = new ImageView(new Image("view.png", 20, 20, true, true));
                viewButton.setGraphic(viewIcon);
                viewButton.setStyle(buttonStyle);
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

        TableColumn<PasswordEntry, Void> editColumn = new TableColumn<>("Edit");
        editColumn.setStyle("-fx-alignment: CENTER;");

        editColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button();
            {
                ImageView editIcon = new ImageView(new Image("edit.png", 20, 20, true, true));
                editButton.setGraphic(editIcon);
                editButton.setStyle(buttonStyle);
                editButton.setOnAction(e -> {
                    PasswordEntry entry = getTableView().getItems().get(getIndex());
                    editEntry(entry);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });

        TableColumn<PasswordEntry, Void> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setStyle("-fx-alignment: CENTER;");
        deleteColumn.setCellFactory(column -> new TableCell<>() {
            private final Button deleteButton = new Button();
            {
                ImageView deleteIcon = new ImageView(new Image("delete.png", 20, 20, true, true));
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle(buttonStyle);
                deleteButton.setOnAction(e -> {
                    PasswordEntry entry = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(entry);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        tableView.getColumns().addAll(accountTypeColumn, usernameColumn, passwordColumn, viewColumn, editColumn, deleteColumn);

        double totalColumnWidth = 150 * 3 + 80 * 3; 
        tableView.setPrefWidth(totalColumnWidth);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        ObservableList<PasswordEntry> data = FXCollections.observableArrayList(
            new PasswordEntry("Email", "user1@example.com", "password123"),
            new PasswordEntry("Bank", "user2", "securepass"),
            new PasswordEntry("Social Media", "user3", "mypassword")
            
        );
        tableView.setItems(data);

        ScrollPane tableScrollPane = new ScrollPane(tableView);
        tableScrollPane.setFitToWidth(true);
        tableScrollPane.setFitToHeight(true);

        Label titleLabel = new Label("Password Manager");
        titleLabel.setStyle("-fx-font-size: 50px; -fx-text-fill: white;");
        titleLabel.setAlignment(Pos.CENTER);

        Button addButton = new Button("Add New Pass");
        addButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-text-fill: black;");
        addButton.setOnAction(e -> addNewPassword(data));

        VBox tableContainer = new VBox();
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.getChildren().add(tableScrollPane);
        tableContainer.setMaxWidth(totalColumnWidth + 50); 

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox containerBox = new VBox(15);
        containerBox.setAlignment(Pos.CENTER);
        containerBox.getChildren().addAll(titleLabel, tableContainer, addButton);

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, containerBox);

        return root;
    }

    private static void editEntry(PasswordEntry entry) {
        TextInputDialog dialog = new TextInputDialog(entry.getPassword());
        dialog.setTitle("Edit Password");
        dialog.setHeaderText("Edit Password for " + entry.getAccountType());
        dialog.setContentText("New Password:");

        dialog.showAndWait().ifPresent(newPassword -> entry.setPassword(newPassword));
    }

    private static void viewPassword(PasswordEntry entry) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("View Password");
        alert.setHeaderText("Password for " + entry.getAccountType());
        alert.setContentText("Password: " + entry.getPassword());
        alert.showAndWait();
    }

    private static void addNewPassword(ObservableList<PasswordEntry> data) {
        Dialog<PasswordEntry> dialog = new Dialog<>();
        dialog.setTitle("Add New Password");
        dialog.setHeaderText("Enter Account Details");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        ComboBox<String> accountTypeComboBox = new ComboBox<>();
        accountTypeComboBox.getItems().addAll(
            "Facebook", 
            "Email", 
            "LinkedIn", 
            "Twitter", 
            "Instagram", 
            "Google", 
            "GitHub", 
            "Netflix", 
            "Amazon", 
            "Bank", 
            "Other"
        );
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
                    return new PasswordEntry(accountType, username, password);
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

}
