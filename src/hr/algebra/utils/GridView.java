package hr.algebra.utils;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Ellipse;

import java.util.ArrayList;
import java.util.List;

public class GridView {
    private static List<Ellipse> playerFingers;

    public static void initGridView(AnchorPane mainContainer) {
        playerFingers = new ArrayList<>();
        for(Node node : mainContainer.getChildren()) {
            if(node instanceof Ellipse) {
                playerFingers.add((Ellipse) node);
                node.setVisible(false);
            }
        }
    }

    public static void showFingerOnFret(String id) {
        playerFingers.stream().filter((e) -> e.getId().equals(id)).forEach(e -> e.setVisible(true));
    }

    public static void cleanFingers() {
        playerFingers.forEach(e -> e.setVisible(false));
    }
}
