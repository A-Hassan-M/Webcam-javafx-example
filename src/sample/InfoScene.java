package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InfoScene{
    private Scene scene;
    private BorderPane layout;
    private GridPane formLayout;

    private Label title;

    private Label name_label;
    private TextField name;

    private Label address_label;
    private TextField address;

    private Label phone_label;
    private TextField phone;

    private Label password_label;
    private PasswordField password;

    private Button save;
    private Button back;
    private Image userImage;

    private User user;


    public InfoScene(Image userImage){
        this.userImage = userImage;
        initControls();
        organizeLayout();
        setupStyle();

    }

    private void setupStyle() {
        title.setAlignment(Pos.CENTER);
        title.setTextFill(Color.WHITE);
        title.setMaxWidth(Main.window.getMaxWidth());
        title.setPadding(new Insets(20));
        title.setBackground(new Background(new BackgroundFill(Color.web("#870203"),CornerRadii.EMPTY, Insets.EMPTY)));

        save.setTextFill(Color.web("#fff"));
        save.setFont(Font.font("Verdana", FontWeight.BOLD,16));
        save.setPrefWidth(100);
        save.setBackground(new Background(new BackgroundFill(Color.web("#870203"),CornerRadii.EMPTY, Insets.EMPTY)));

        back.setTextFill(Color.web("#fff"));
        back.setFont(Font.font("Verdana", FontWeight.BOLD,16));
        back.setPrefWidth(100);
        back.setBackground(new Background(new BackgroundFill(Color.web("#870203"),CornerRadii.EMPTY, Insets.EMPTY)));


        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(5);
        rowConstraints.setPrefHeight(5);
        rowConstraints.setVgrow(Priority.SOMETIMES);

        formLayout.setBackground(new Background(new BackgroundFill(Color.web("#F8F8F8"),CornerRadii.EMPTY, Insets.EMPTY)));
        formLayout.setPadding(new Insets(20,50,20,50));
        formLayout.setAlignment(Pos.CENTER);
        formLayout.getRowConstraints().addAll(rowConstraints, rowConstraints, rowConstraints, rowConstraints);

        layout.setPadding(new Insets(0,0,20,0));



    }

    private void initControls() {
        layout = new BorderPane();
        formLayout = new GridPane();

        title = new Label("USER INFO");

        name_label = new Label("Name: ");
        name = new TextField();

        address_label = new Label("Address: ");
        address = new TextField();

        phone_label = new Label("Phone: ");
        phone = new TextField();

        password_label = new Label("password: ");
        password = new PasswordField();

        save = new Button("SAVE");
        back = new Button("BACK");

        save.setOnAction(e-> {
            saveInfo();
            Main.window.setScene(new UserScene(user).getScene());
        });

        back.setOnAction(e-> {
            goBack();
        });
    }

    private void goBack() {
        Scene scene = Main.sceneStack.pop();
        Main.window.setScene(scene);
    }

    private void saveInfo() {

        String photoPath = UserModel.saveImageToDisk(userImage);
        user = new User(name.getText(), address.getText(), password.getText(), phone.getText(), photoPath);
        UserModel.insert(user);


    }



    public void organizeLayout(){

        layout.setTop(title);


        formLayout.add(name_label,0,0);
        formLayout.add(name,1,0);
        formLayout.add(address_label,0,1);
        formLayout.add(address,1,1);
        formLayout.add(phone_label,0,2);
        formLayout.add(phone,1,2);
        formLayout.add(password_label,0,3);
        formLayout.add(password,1,3);



        layout.setOnMousePressed(event -> {
            Main.xPos = event.getSceneX();
            Main.yPos= event.getSceneY();
        });
        layout.setOnMouseDragged(event -> {
            Main.window.setX(event.getScreenX() - Main.xPos);
            Main.window.setY(event.getScreenY() - Main.yPos);
        });

        layout.setCenter(formLayout);
        HBox buttons = new HBox();
        buttons.getChildren().addAll(back, save);
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);
        layout.setBottom(buttons);

        scene = new Scene(layout, 800,400);
    }

    public Scene getScene() {
        return scene;
    }
}
