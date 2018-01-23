package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class UserScene{

    private ImageView userImage;
    private Label title;
    private Label name;
    private Label address;
    private Label phoneNumber;
    private User user;
    private BorderPane layout;
    private HBox infoBox;
    private VBox rightLayout;
    Scene scene;

    public UserScene(User user) {
        this.user = user;
        getImage();
        initControls();
        setupStyle();
    }

    private void getImage() {
        userImage = new ImageView();
        userImage.setImage(new Image("file:"+user.getPhotoPath()));
    }

    private void initControls() {
        layout = new BorderPane();
        rightLayout = new VBox();
        infoBox = new HBox();

        title = new Label("USER PROFILE");

        name = new Label("Name: \t"+user.getName());
        address = new Label("Address: \t"+user.getAddress());
        phoneNumber = new Label("Phone: \t"+user.getPhoneNumber());

        rightLayout.getChildren().addAll(name,address,phoneNumber);
        infoBox.getChildren().addAll(userImage,rightLayout);

        layout.setOnMousePressed(event -> {
            Main.xPos = event.getSceneX();
            Main.yPos= event.getSceneY();
        });

        layout.setOnMouseDragged(event -> {
            Main.window.setX(event.getScreenX() - Main.xPos);
            Main.window.setY(event.getScreenY() - Main.yPos);
        });

        scene = new Scene(layout,800,350);
    }

    private void setupStyle() {
        title.setMaxWidth(Main.window.getMaxWidth());
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(20));
        title.setTextFill(Color.web("#fff"));
        title.setFont(Font.font("Verdana", FontWeight.BOLD,16));
        title.setStyle("-fx-background-color: #870203;");

        name.setTextFill(Color.web("#870203"));
        address.setTextFill(Color.web("#870203"));
        phoneNumber.setTextFill(Color.web("#870203"));

        userImage.setFitWidth(300);
        userImage.setPreserveRatio(true);

        rightLayout.setSpacing(80);
        rightLayout.setAlignment(Pos.CENTER_LEFT);

        infoBox.setSpacing(100);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(0,0,0,80));

        layout.setStyle("-fx-background-color: #F8F8F8");
        layout.setTop(title);
        layout.setCenter(infoBox);

        BorderPane.setAlignment(infoBox,Pos.CENTER_LEFT);
    }

    public Scene getScene() {
        return scene;
    }
}
