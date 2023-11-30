package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class SampleController {

    @FXML
    private Label statusLabel;

    @FXML
    private ArrayList<ArrayList<Circle>> circlesList;

    public void initialize() {
        circlesList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            ArrayList<Circle> row = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                Circle circle = getCircleById("circle" + i + j);
                if (circle == null) {
                    System.err.println("Circle is null for ID: " + "circle" + (i * 7 + j));
                } else {
                    row.add(circle);
                }  
            }
            
            circlesList.add(row);
        }
    }

    @FXML
    private void handleCircleClick(MouseEvent event) {
        Circle clickedCircle = (Circle) event.getSource();
        int rowIndex = GridPane.getRowIndex(clickedCircle);
        int colIndex = GridPane.getColumnIndex(clickedCircle);

        // Change the color of the clicked circle
        Color newColor = Color.RED; // You can set any color you want
        clickedCircle.setFill(newColor);

        // Update the status label
        statusLabel.setText("Circle at (" + rowIndex + ", " + colIndex + ") clicked");
    }

    private Circle getCircleById(String id) {
        for (ArrayList<Circle> row : circlesList) {
            for (Circle circle : row) {
                if (circle.getId().equals(id)) {
                    return circle;
                }
            }
        }
        return null;
    }
}
