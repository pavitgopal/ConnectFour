package application;

import javafx.fxml.FXML;

import java.util.Random;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import java.util.concurrent.TimeUnit;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


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
        
        //gridPane.setStyle("-fx-background-color: #ffff00;");
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
            String colorStyle = "-fx-background-color: #ff0000;";
            String newStyle = colorStyle + "-fx-background-radius: 100;";
            buttonToColor.setStyle(newStyle);
            buttonToColor.setDisable(true);
            //counter++;
            checkWin();
            
            
            randomAI();
            //thoughtfulAI();
        }
    }

    private int getColumnIndex(Button button) {
        // Assuming the button ID is formatted as "buttonXY" where X is the row and Y is the column
        String id = button.getId();
        return Integer.parseInt(id.substring(id.length() - 1));
    }

    protected Button findFirstUncoloredButtonInColumn(int columnIndex) {
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
    	
    	PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            int random = new Random().nextInt(7); // assuming 7 columns, generate a number between 0 and 6
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
    

    @FXML
    protected void checkWin() {
        // Check for vertical (column-wise) win
        for (int col = 0; col < 7; col++) { // assuming 7 columns
            for (int row = 0; row < 5; row++) { // stop at 5 to avoid out of bounds
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row + 1, col))
                        && colorStyle.equals(getButtonColorStyle(row + 2, col))
                        && colorStyle.equals(getButtonColorStyle(row + 3, col))) {
                    declareWinner(colorStyle);
                    return;
                }
            }
        }

        // Check for horizontal (row-wise) win
        for (int row = 0; row < 8; row++) { // assuming 8 rows
            for (int col = 0; col < 4; col++) { // stop at 4 to avoid out of bounds
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row, col + 1))
                        && colorStyle.equals(getButtonColorStyle(row, col + 2))
                        && colorStyle.equals(getButtonColorStyle(row, col + 3))) {
                    declareWinner(colorStyle);
                    return;
                }
            }
        }
        
        // Check for diagonal win (Top-Left to Bottom-Right)
        for (int row = 0; row < 5; row++) { // assuming 8 rows, stop at 5
            for (int col = 0; col < 4; col++) { // assuming 7 columns, stop at 4
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row + 1, col + 1))
                        && colorStyle.equals(getButtonColorStyle(row + 2, col + 2))
                        && colorStyle.equals(getButtonColorStyle(row + 3, col + 3))) {
                    declareWinner(colorStyle);
                    return;
                }
            }
        }

        // Check for diagonal win (Top-Right to Bottom-Left)
        for (int row = 0; row < 5; row++) {
            for (int col = 3; col < 7; col++) { // start from column 3 to avoid out of bounds
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row + 1, col - 1))
                        && colorStyle.equals(getButtonColorStyle(row + 2, col - 2))
                        && colorStyle.equals(getButtonColorStyle(row + 3, col - 3))) {
                    declareWinner(colorStyle);
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
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
            	
            	String buttonId = "button" + i + j;
                Button buttonToDisable = (Button) gridPane.lookup("#" + buttonId);
                
            	buttonToDisable.setDisable(true);
            }
        }
    }
    
    
    
    


    
    /*void thoughtfulAI() {
        // 1. Check for a winning move
        int winningMove = findWinningMove("#000000"); // Black's color
        if (winningMove != -1) {
            playMove(winningMove);
            return;
        }

        // 2. Block player's winning move
        int blockingMove = findWinningMove("#ff0000"); // Red's color
        if (blockingMove != -1) {
            playMove(blockingMove);
            return;
        }

        // 3. Extend AI's own line
        int bestMove = findBestMoveForAI();
        if (bestMove != -1) {
            playMove(bestMove);
            return;
        }

        // 4. If no other options, make a random move
        randomAI();
    }

    private int findWinningMove(String colorStyle) {
        for (int col = 0; col < 7; col++) { // Assuming 7 columns
            int row = getTopEmptyRow(col);
            if (row != -1) {
                // Temporarily play the move
                setButtonStyle(row, col, colorStyle);
                if (checkWinAI()) {
                    resetButtonStyle(row, col); // Reset the button style
                    return col;
                }
                resetButtonStyle(row, col); // Reset the button style
            }
        }
        return -1; // No winning move found
    }
    
    protected boolean checkWinAI() {
        // Check for vertical (column-wise) win
        for (int col = 0; col < 7; col++) { // assuming 7 columns
            for (int row = 0; row < 5; row++) { // stop at 5 to avoid out of bounds
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row + 1, col))
                        && colorStyle.equals(getButtonColorStyle(row + 2, col))
                        && colorStyle.equals(getButtonColorStyle(row + 3, col))) {
                    
                    return true;
                }
            }
        }

        // Check for horizontal (row-wise) win
        for (int row = 0; row < 8; row++) { // assuming 8 rows
            for (int col = 0; col < 4; col++) { // stop at 4 to avoid out of bounds
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row, col + 1))
                        && colorStyle.equals(getButtonColorStyle(row, col + 2))
                        && colorStyle.equals(getButtonColorStyle(row, col + 3))) {
                    
                    return true;
                }
            }
        }
        
        // Check for diagonal win (Top-Left to Bottom-Right)
        for (int row = 0; row < 5; row++) { // assuming 8 rows, stop at 5
            for (int col = 0; col < 4; col++) { // assuming 7 columns, stop at 4
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row + 1, col + 1))
                        && colorStyle.equals(getButtonColorStyle(row + 2, col + 2))
                        && colorStyle.equals(getButtonColorStyle(row + 3, col + 3))) {
                    
                    return true;
                }
            }
        }

        // Check for diagonal win (Top-Right to Bottom-Left)
        for (int row = 0; row < 5; row++) {
            for (int col = 3; col < 7; col++) { // start from column 3 to avoid out of bounds
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row + 1, col - 1))
                        && colorStyle.equals(getButtonColorStyle(row + 2, col - 2))
                        && colorStyle.equals(getButtonColorStyle(row + 3, col - 3))) {
                    
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void resetButtonStyle(int row, int col) {
        String buttonId = "button" + row + col;
        Button button = (Button) gridPane.lookup("#" + buttonId);
        if (button != null) {
            // Assuming the default style is without color or disabled state
            String defaultStyle = "-fx-background-color: #cfcbcb; -fx-background-radius: 100;";
            button.setStyle(defaultStyle);
        }
    }
    
    private int getTopEmptyRow(int col) {
        for (int row = 0; row < 8; row++) { // Assuming 8 rows
            String buttonId = "button" + row + col;
            Button button = (Button) gridPane.lookup("#" + buttonId);
            if (button != null && !button.isDisabled()) {
                return row;
            }
        }
        return -1; // Return -1 if the column is full
    }
    
    
    
    
    private void setButtonStyle(int row, int col, String colorStyle) {
        String buttonId = "button" + row + col;
        Button button = (Button) gridPane.lookup("#" + buttonId);
        if (button != null) {
            button.setStyle(colorStyle + "-fx-background-radius: 100;");
        }
    }

    private int findBestMoveForAI() {
        int bestCol = -1;
        int maxInLine = 0; // Maximum number of AI chips in line found so far

        for (int col = 0; col < 7; col++) {
            int row = getTopEmptyRow(col);
            if (row != -1) {
                int inLine = countMaxInLine(row, col, "#000000"); // Black's color
                if (inLine > maxInLine) {
                    maxInLine = inLine;
                    bestCol = col;
                }
            }
        }
        return bestCol;
    }
    
    private int countMaxInLine(int row, int col, String colorStyle) {
        int maxCount = 0;

        // Check horizontally
        int count = 1; // Start with the current chip
        // Check to the left
        for (int i = col - 1; i >= 0 && getButtonColorStyle(row, i).equals(colorStyle); i--) {
            count++;
        }
        // Check to the right
        for (int i = col + 1; i < 7 && getButtonColorStyle(row, i).equals(colorStyle); i++) {
            count++;
        }
        maxCount = Math.max(maxCount, count);

        // Check vertically (only need to check downwards)
        count = 1; // Start with the current chip
        for (int i = row + 1; i < 8 && getButtonColorStyle(i, col).equals(colorStyle); i++) {
            count++;
        }
        maxCount = Math.max(maxCount, count);

        // Check diagonal (Top-Left to Bottom-Right)
        count = 1; // Start with the current chip
        for (int i = 1; row + i < 8 && col + i < 7 && getButtonColorStyle(row + i, col + i).equals(colorStyle); i++) {
            count++;
        }
        for (int i = 1; row - i >= 0 && col - i >= 0 && getButtonColorStyle(row - i, col - i).equals(colorStyle); i++) {
            count++;
        }
        maxCount = Math.max(maxCount, count);

        // Check diagonal (Top-Right to Bottom-Left)
        count = 1; // Start with the current chip
        for (int i = 1; row + i < 8 && col - i >= 0 && getButtonColorStyle(row + i, col - i).equals(colorStyle); i++) {
            count++;
        }
        for (int i = 1; row - i >= 0 && col + i < 7 && getButtonColorStyle(row - i, col + i).equals(colorStyle); i++) {
            count++;
        }
        maxCount = Math.max(maxCount, count);

        return maxCount;
    }

    

    private void playMove(int columnIndex) {
        Button buttonToPlay = findFirstUncoloredButtonInColumn(columnIndex);
        if (buttonToPlay != null) {
            String colorStyle = "-fx-background-color: #000000;"; // Black's color
            String newStyle = colorStyle + "-fx-background-radius: 100;";
            buttonToPlay.setStyle(newStyle);
            buttonToPlay.setDisable(true);
            checkWin();
        }
    }*/

    protected void Reset()
    {
    	counter = 0;
        winnerColor.setText("Winner: ");
        
        for (int i = 0; i < 8; i++) {
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

    @FXML
    protected void onReset(ActionEvent e) {
    	Reset();
    }
}