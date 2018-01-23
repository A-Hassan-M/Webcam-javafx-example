package sample;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.concurrent.ScheduledExecutorService;


public class Main extends Application{

    private ImageView cameraScene;
    private Button capture;
    private Button reset;

    private boolean stopCamera;
    private ObjectProperty<Image> imageProperty;
    private Webcam webcam;
    private BufferedImage grabbedImage;

    private Scene mainScene;
    private VBox layout;

    static Stage window;
    static Stack<Scene> sceneStack;

    static double xPos;
    static double yPos;

    @Override
    public void start(Stage primaryStage){
        window = primaryStage;
        initControls();
        initializeWebcam();
        setupStyle();

        primaryStage.setTitle("task");
        primaryStage.setScene(mainScene);
        primaryStage.show();
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

        window.initStyle(StageStyle.UNDECORATED);
    }


    private void initializeWebcam() {
        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();

        startWebCamStream();

    }

    private void startWebCamStream() {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call(){

                while (!stopCamera) {
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

                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        cameraScene.imageProperty().bind(imageProperty);
    }

    private void initControls() {
        layout = new VBox();

        capture = new Button("CAPTURE");
        reset = new Button("RESET");
        capture.setOnAction(e->{
            stopCamera = true;
            reset.setDisable(false);
            webcam.close();
            if(capture.getText().equals("CAPTURE")) {
                capture.setText("Next");
            }else {
                Image image = cameraScene.getImage();
                sceneStack.push(mainScene);
                window.setScene(new InfoScene(image).getScene());
            }
        });

        reset.setOnAction(e->{
            stopCamera = false;
            reset.setDisable(true);
            capture.setText("Capture");
            webcam.open();
            startWebCamStream();
        });

        sceneStack = new Stack<>();

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
            xPos = event.getSceneX();
            yPos= event.getSceneY();
        });
        layout.setOnMouseDragged(event -> {
            window.setX(event.getScreenX() - xPos);
            window.setY(event.getScreenY() - yPos);
        });

        mainScene = new Scene(layout, 300, 350);


    }

    public static void main(String[] args) {
        launch(args);
    }
}
