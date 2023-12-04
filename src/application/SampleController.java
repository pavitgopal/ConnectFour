package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class SampleController {
    @FXML
    private GridPane gridPane;

    private ArrayList<Button> buttons = new ArrayList<>();

    @FXML
    private Label winnerColor, yellowWins, blueWins;

    private int numYellowWins = 0;
    private int numBlueWins = 0;
    
    boolean found = false;

    @FXML
    public void initialize() {
        for (int i = 0; i < 6; i++) { // 6 rows
            for (int j = 0; j < 7; j++) { // 7 columns
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
        int columnIndex = getColumnIndex(clickedButton);
        Button buttonToColor = findFirstUncoloredButtonInColumn(columnIndex);
        if (buttonToColor != null) {
            String colorStyle = "-fx-background-color: #ff0000;";
            String newStyle = colorStyle + "-fx-background-radius: 100;";
            buttonToColor.setStyle(newStyle);
            buttonToColor.setDisable(true);
            checkWin();
            thoughtfulAI();
            //randomAI();
        }
    }

    private int getColumnIndex(Button button) {
        String id = button.getId();
        return Integer.parseInt(id.substring(id.length() - 1));
    }

    protected Button findFirstUncoloredButtonInColumn(int columnIndex) {
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
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            int random = new Random().nextInt(7);
            Button randButton = findFirstUncoloredButtonInColumn(random);
            if (randButton != null) {
                String colorStyle = "-fx-background-color: #000000;";
                String newStyle = colorStyle + "-fx-background-radius: 100;";
                randButton.setStyle(newStyle);
                randButton.setDisable(true);
                checkWin();
            }
        });
        pause.play();
    }
    
    private void thoughtfulAI()
    {
    	for (int col = 0; col < 7; col++) 
    	{
    		Button potentialWinButton = findFirstUncoloredButtonInColumn(col);
    		
    		if (potentialWinButton != null) {
                // Temporarily color the button to check for a win
                String originalStyle = potentialWinButton.getStyle();
                
                String colorStyle = "-fx-background-color: #000000;";
                String newStyle = colorStyle + "-fx-background-radius: 100;";
                
                potentialWinButton.setStyle(newStyle);
                potentialWinButton.setDisable(true);
                
                checkWin();
                
                if(found)
                {
                	return;
                }
                    
                else
            	{
	            	potentialWinButton.setStyle(originalStyle);
	            	potentialWinButton.setDisable(false);
            	}
               
                
            }
            
    	}
    	
    	randomAI();
    }

    @FXML
    protected void checkWin() {
        found = false;

        // Check for vertical (column-wise) win
        for (int col = 0; col < 7; col++) { // 7 columns
            for (int row = 0; row < 3; row++) { // only need to check up to row 3 for vertical wins
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row + 1, col))
                        && colorStyle.equals(getButtonColorStyle(row + 2, col))
                        && colorStyle.equals(getButtonColorStyle(row + 3, col))) {
                    declareWinner(colorStyle);
                    found = true;
                    return;
                }
            }
        }

        // Check for horizontal (row-wise) win
        for (int row = 0; row < 6; row++) { // 6 rows
            for (int col = 0; col < 4; col++) { // only need to check up to column 4 for horizontal wins
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row, col + 1))
                        && colorStyle.equals(getButtonColorStyle(row, col + 2))
                        && colorStyle.equals(getButtonColorStyle(row, col + 3))) {
                    declareWinner(colorStyle);
                    found = true;
                    return;
                }
            }
        }

        // Check for diagonal win (Top-Left to Bottom-Right)
        for (int row = 0; row < 3; row++) { // up to row 3
            for (int col = 0; col < 4; col++) { // up to column 4
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row + 1, col + 1))
                        && colorStyle.equals(getButtonColorStyle(row + 2, col + 2))
                        && colorStyle.equals(getButtonColorStyle(row + 3, col + 3))) {
                    declareWinner(colorStyle);
                    found = true;
                    return;
                }
            }
        }

        // Check for diagonal win (Top-Right to Bottom-Left)
        for (int row = 0; row < 3; row++) {
            for (int col = 3; col < 7; col++) { // start from column 3
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row + 1, col - 1))
                        && colorStyle.equals(getButtonColorStyle(row + 2, col - 2))
                        && colorStyle.equals(getButtonColorStyle(row + 3, col - 3))) {
                    declareWinner(colorStyle);
                    found = true;
                    return;
                }
            }
        }
    }


    private String getButtonColorStyle(int row, int col) {
        String buttonId = "button" + row + col;
        Button button = (Button) gridPane.lookup("#" + buttonId);
        if (button != null && button.isDisabled()) { // check only colored (disabled) buttons
            return button.getStyle();
        }
        return "";
    }

    private void declareWinner(String colorStyle) {
        // Declare the winner based on the color style
        if (colorStyle.contains("#000000")) { // blue color
            numBlueWins++;
            winnerColor.setText("Black wins!");
        } else if (colorStyle.contains("#ff0000")) { // yellow color
            numYellowWins++;
            winnerColor.setText("Red wins!");
        }
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
            	
            	String buttonId = "button" + i + j;
                Button buttonToDisable = (Button) gridPane.lookup("#" + buttonId);
                
            	buttonToDisable.setDisable(true);
            }
        }
    }


    @FXML
    protected void onReset(ActionEvent e) {
    	
    	//counter = 0;
        winnerColor.setText("Winner: ");
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
            	String buttonId = "button" + i + j;
                Button buttonToReset = (Button) gridPane.lookup("#" + buttonId);
                
                String colorStyle = "-fx-background-color: #cfcbcb;";
                String newStyle = colorStyle + "-fx-background-radius: 100;";
                buttonToReset.setStyle(newStyle);
                buttonToReset.setDisable(false);
                
             
            } 
        }
    }
}