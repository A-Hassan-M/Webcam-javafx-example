package sample;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NewUser {
    private ImageView cameraScene;
    private Button capture;
    private Button reset;

    private boolean stopCamera;
    private ObjectProperty<Image> imageProperty;
    private Webcam webcam;
    private BufferedImage grabbedImage;

    private Scene mainScene;
    private VBox layout;

    ScheduledExecutorService th;

    public NewUser(){
        initControls();
        initializeWebcam();
        setupStyle();
    }

    private void setupStyle() {
        layout.setStyle("-fx-background-color: #F8F8F8");
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.setSpacing(20);

        capture.setTextFill(Color.web("#fff"));
        capture.setStyle("-fx-background-color: #870203;");
        capture.setFont(Font.font("Verdana", FontWeight.BOLD,16));
        capture.setPrefWidth(120);

        reset.setTextFill(Color.web("#fff"));
        reset.setStyle("-fx-background-color: #870203;");
        reset.setFont(Font.font("Verdana", FontWeight.BOLD,16));
        reset.setPrefWidth(120);

        cameraScene.setFitWidth(250);
        cameraScene.setFitHeight(250);
    }


    private void initializeWebcam() {
        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();

        startWebCamStream();

    }

    private void startWebCamStream() {
        Runnable task = () -> {

            if (!stopCamera) {
                try {
                    if ((grabbedImage = webcam.getImage()) != null) {

                        Platform.runLater(() -> {
                            Image mainImage = SwingFXUtils.toFXImage(grabbedImage, null);
                            imageProperty.set(mainImage);
                        });

                        grabbedImage.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        th = Executors.newSingleThreadScheduledExecutor();
        th.scheduleAtFixedRate(task,0,33, TimeUnit.MILLISECONDS);
        cameraScene.imageProperty().bind(imageProperty);
    }

    private void initControls() {
        layout = new VBox();

        capture = new Button("CAPTURE");
        reset = new Button("RESET");
        capture.setOnAction(e->{
            takePhoto();
        });

        reset.setOnAction(e->{
            resetWebcam();
        });

        stopCamera = false;
        imageProperty = new SimpleObjectProperty<>();


        cameraScene = new ImageView();

        HBox camControls = new HBox();

        reset.setDisable(true);
        camControls.getChildren().addAll(reset, capture);
        camControls.setSpacing(10);
        camControls.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(cameraScene,camControls);

        layout.setOnMousePressed(event -> {
            Main.xPos = event.getSceneX();
            Main.yPos= event.getSceneY();
        });
        layout.setOnMouseDragged(event -> {
            Main.window.setX(event.getScreenX() - Main.xPos);
            Main.window.setY(event.getScreenY() - Main.yPos);
        });

        mainScene = new Scene(layout, 300, 350);


    }

    private void resetWebcam() {
        stopCamera = false;
        reset.setDisable(true);
        capture.setText("CAPTURE");
        webcam.open();
        startWebCamStream();
    }

    private void takePhoto() {
        stopCamera = true;
        reset.setDisable(false);
        th.shutdown();
        try {
            th.awaitTermination(33, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webcam.close();

        if(capture.getText().equals("CAPTURE")) {
            capture.setText("Next");
        }else {
            System.out.println(capture.getText());
            Image image = cameraScene.getImage();
            Main.sceneStack.push(mainScene);
            Main.window.setScene(new InfoScene(image).getScene());
        }
    }

    public Scene getMainScene() {
        return mainScene;
    }
}