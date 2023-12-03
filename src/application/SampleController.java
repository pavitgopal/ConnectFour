package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class SampleController {
    @FXML
    private GridPane gridPane; // Assuming the buttons are inside this GridPane

    private ArrayList<Button> buttons = new ArrayList<>();
    private int counter = 0;

    @FXML
    private Label winnerColor, yellowWins, blueWins;

    private int numYellowWins = 0;
    private int numBlueWins = 0;

    @FXML
    public void initialize() {
        // Assuming you have a fixed grid size, for example, 7x7
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                String buttonId = "button" + i + j;
                Button button = (Button) gridPane.lookup("#" + buttonId);
                if (button != null) {
                    buttons.add(button);
                    button.setOnAction(this::onButtonClick);
                }
            }
        }
    }

    @FXML
    protected void onButtonClick(ActionEvent e) {
        Button clickedButton = (Button) e.getSource();
        // Extract the column index from the button ID
        int columnIndex = getColumnIndex(clickedButton);

        // Find the first uncolored button in the column
        Button buttonToColor = findFirstUncoloredButtonInColumn(columnIndex);
        if (buttonToColor != null) {
            // Apply the color and disable the button
            String colorStyle = counter % 2 == 0 ? "-fx-background-color: #000DFF;" : "-fx-background-color: #d7d300;";
            String newStyle = colorStyle + "-fx-background-radius: 100;";
            buttonToColor.setStyle(newStyle);
            buttonToColor.setDisable(true);
            counter++;
            checkWin();
        }
    }

    private int getColumnIndex(Button button) {
        // Assuming the button ID is formatted as "buttonXY" where X is the row and Y is the column
        String id = button.getId();
        return Integer.parseInt(id.substring(id.length() - 1));
    }

    private Button findFirstUncoloredButtonInColumn(int columnIndex) {
        // Start from the bottom of the column and move upwards
        for (int i = buttons.size() - 1; i >= 0; i--) {
            Button button = buttons.get(i);
            int buttonColumnIndex = getColumnIndex(button);
            if (buttonColumnIndex == columnIndex && !button.isDisabled()) {
                return button;
            }
        }
        return null;
    }
    
    private void randomAI() {
    	
    }



    @FXML
    protected void checkWin() {
        
    }

    @FXML
    protected void onReset(ActionEvent e) {
        // Implementation of reset logic
    }
}
