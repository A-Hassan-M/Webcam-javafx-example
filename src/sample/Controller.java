package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.List;

public class Controller {

    @FXML
    private ListView<String> currentUsers;

    public Controller(){

        List<User> users = loadUsers();
        for (User user:users){
            System.out.println(user.toString()+"\n");
        }
        currentUsers.getItems().addAll("ahmed","ali","sameh");
        currentUsers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private List<User> loadUsers() {
        return UserModel.selectAll();
    }
}
