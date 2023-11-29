package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;

public class SampleController {

    @FXML
    private Label statusLabel;

    // Use a List to store the circles
    private List<Circle> connect4Grid = new ArrayList<>();

    @FXML
    public void initialize() {
        Platform.runLater(this::initializeConnect4Grid);
    }

    private void initializeConnect4Grid() {
        int rows = 6; // Number of rows
        int columns = 7; // Number of columns

        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                Circle circle = createCircle("circle" + col + row);
                circle.setStroke(Color.BLACK);
                // Pass MouseEvent as the parameter to handleCircleClick
                circle.setOnMouseClicked(event -> handleCircleClick(event));
                connect4Grid.add(circle);
            }
        }
    }

    private Circle createCircle(String id) {
        // Use JavaFX utility method to look up a node by ID
        return (Circle) statusLabel.getScene().lookup("#" + id);
    }

    @FXML
    private void handleCircleClick(MouseEvent event) {
        // Your event handling logic here
    }

    private void checkForWin(int col, int row) {
        // Add your Connect Four win-checking logic here
        // You might want to check horizontally, vertically, and diagonally
        // Example: Check horizontally
        if (checkLine(col, row, 1, 0) || checkLine(col, row, -1, 0)) {
            statusLabel.setText("Player wins!");
        }
    }

    private boolean checkLine(int col, int row, int colDirection, int rowDirection) {
        // Check for four consecutive circles in a line
        int count = 1; // Count the clicked circle
        Color targetColor = Color.RED; // Assuming 'RED' represents the current player

        for (int i = 1; i < 4; i++) {
            int nextCol = col + i * colDirection;
            int nextRow = row + i * rowDirection;

            if (isValidPosition(nextCol, nextRow) &&
                    connect4Grid.get(nextCol).get(nextRow).getFill() == targetColor) {
                count++;
            } else {
                break; // Stop if the next circle is not of the same color
            }
        }

        return count >= 4;
    }

    private boolean isValidPosition(int col, int row) {
        return col >= 0 && col < connect4Grid.size() && row >= 0 && row < connect4Grid.get(col).size();
    }
}
