package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Stack;


public class Main extends Application{

    static Stage window;
    static Stack<Scene> sceneStack;

    static double xPos;
    static double yPos;

    @Override
    public void start(Stage primaryStage){
        window = primaryStage;
        window.initStyle(StageStyle.UNDECORATED);
        sceneStack = new Stack<>();

        primaryStage.setScene(new NewUser().getMainScene());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
