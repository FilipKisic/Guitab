package hr.algebra.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuPageController {

    public AnchorPane ancrPnMenuBackground;
    public Button btnParadise;
    public Button btnCliffs;
    public Button btnFields;
    public Button btnLayla;

    public void stopApplication() {
        Platform.exit();
    }

    public void btnPlaySongPressed(ActionEvent actionEvent) {
        String buttonId = ((Button) actionEvent.getSource()).getId();
        String songName = buttonId.replace("btn", "");
        openSongLecture(songName);
    }

    private void openSongLecture(String songName) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("hr/algebra/res/views/main_page.fxml")));
            Parent root = loader.load();
            MainPageController controller = loader.getController();
            controller.loadSong(songName);
            Stage stage = setupStage(root);
            stage.show();
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
}
