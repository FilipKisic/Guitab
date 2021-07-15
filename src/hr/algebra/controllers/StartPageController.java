package hr.algebra.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartPageController {
    public Button btnPlay;

    public void btnPlayPressed(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("hr/algebra/res/views/menu_page.fxml")));
            Parent root = loader.load();
            Stage stage = setupStage(root);
            closeStartWindow(actionEvent, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage setupStage(Parent root) {
        Stage stage = new Stage();
        Scene scene = new Scene(root, 1600, 900);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        return stage;
    }

    private void closeStartWindow(ActionEvent actionEvent, Stage stage) {
        stage.show();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
