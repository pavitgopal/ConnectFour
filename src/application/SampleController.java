package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javafx.scene.control.TextField;

public class SampleController {
    @FXML
    private GridPane gridPane;
    
    @FXML
	private ChoiceBox<String> difficultyChoiceBox;

    @FXML
    private Button saveButton;

    @FXML
    private ChoiceBox<String> loadGameChoiceBox;

    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<String> gameStateLog = new ArrayList<>();

    @FXML
    private Label winnerColor, yellowWins, blueWins;
    
    @FXML
    private TextField fileNameTextField;

    private int numYellowWins = 0;
    private int numBlueWins = 0;
    
    private String currentDifficulty = "Easy"; // Default difficulty
    
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
                    populateLoadGameChoiceBox();
                }
            }
        }
        difficultyChoiceBox.setItems(FXCollections.observableArrayList("Easy", "Hard"));
        difficultyChoiceBox.setValue("Easy"); // Set default value
        difficultyChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> onDifficultyChanged(newValue));
    }
    
    private void onDifficultyChanged(String newValue) {
    	currentDifficulty = newValue;
    }


    
    private void populateLoadGameChoiceBox() {
        File folder = new File("savedGames");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                loadGameChoiceBox.getItems().add(file.getName());
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
            logGameState("You", columnIndex);
            checkWin();
            
            if (currentDifficulty.equals("Easy")) {
                randomAI();
            } else if (currentDifficulty.equals("Hard")) {
                thoughtfulAI();
            }

            
        }
    }
    
    private void logGameState(String player, int columnIndex) {
        gameStateLog.add(player + " dropped a chip in column " + columnIndex);
        System.out.println(player + " dropped a chip in column " + columnIndex);
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
                
                logGameState("Computer", random);
                
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
                	//System.out.println("winning spot found");
                	logGameState("Computer", col);
                	return;
                }
                    
                else
            	{
	            	potentialWinButton.setStyle(originalStyle);
	            	potentialWinButton.setDisable(false);
            	}
               
                
            }
    		
    		
            
    	}
    	
    	for (int col = 0; col < 7; col++) 
    	{
    		Button potentialWinButton = findFirstUncoloredButtonInColumn(col);
    		
    		if (potentialWinButton != null) {
                // Temporarily color the button to check for a win
                String originalStyle = potentialWinButton.getStyle();
                
                String colorStyle = "-fx-background-color: #ff0000;";
                String newStyle = colorStyle + "-fx-background-radius: 100;";
                
                potentialWinButton.setStyle(newStyle);
                potentialWinButton.setDisable(true);
                
                checkWinOpp();
                
                if(found)
                {
                	
                	colorStyle = "-fx-background-color: #000000;";
                    newStyle = colorStyle + "-fx-background-radius: 100;";
                    
                    potentialWinButton.setStyle(newStyle);
                    potentialWinButton.setDisable(true);
                    //System.out.println("blocking spot found");
                    logGameState("Player", col);
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
    	//System.out.println("random spot found");
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
    
    
    @FXML
    protected void checkWinOpp() {
        found = false;

        // Check for vertical (column-wise) win
        for (int col = 0; col < 7; col++) { // 7 columns
            for (int row = 0; row < 3; row++) { // only need to check up to row 3 for vertical wins
                String colorStyle = getButtonColorStyle(row, col);
                if (!colorStyle.isEmpty() && colorStyle.equals(getButtonColorStyle(row + 1, col))
                        && colorStyle.equals(getButtonColorStyle(row + 2, col))
                        && colorStyle.equals(getButtonColorStyle(row + 3, col))) {
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
    
    private void saveGame() {
        try (PrintWriter out = new PrintWriter("gameState.txt")) {
            for (String logEntry : gameStateLog) {
                out.println(logEntry);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    private void loadGame() {
        try (Scanner scanner = new Scanner(new File("gameState.txt"))) {
            gameStateLog.clear();
            while (scanner.hasNextLine()) {
                String logEntry = scanner.nextLine();
                gameStateLog.add(logEntry);
                System.out.println(logEntry);
                // You need to implement how each log entry affects the game state
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }


    @FXML
    protected void onReset(ActionEvent e) {
        // Clear existing game state
        clearGameState();

        // Create a new file for logging
        String newFileName = createNewLogFile();

        // Update the UI
        winnerColor.setText("Winner: ");
        resetButtons();

        System.out.println("Game reset.");
    }

    private void clearGameState() {
        // Clear the game state log
        gameStateLog.clear();

        // Reset any other game state variables here (e.g., score counters)
        numYellowWins = 0;
        numBlueWins = 0;
    }

    private String createNewLogFile() {
        String fileName = "savedGames/" + System.currentTimeMillis() + ".txt";
        try (PrintWriter out = new PrintWriter(fileName)) {
            // File is created here. It will be empty initially.
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error creating new game log file: " + e.getMessage());
        }
        return fileName;
    }

    private void resetButtons() {
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

    @FXML
    protected void onLoadGame(ActionEvent e) {
        String fileNameInput = fileNameTextField.getText();
        if (fileNameInput == null || fileNameInput.trim().isEmpty()) {
            System.err.println("Please enter a file name.");
            return;
        }

        String fileName = "savedGames/" + fileNameInput + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            loadGame(fileName);
        } else {
            System.err.println("File not found: " + fileName);
        }
    }
    
    @FXML
    protected void onSaveGame(ActionEvent e) {
        String fileNameInput = fileNameTextField.getText();
        if (fileNameInput == null || fileNameInput.trim().isEmpty()) {
            System.err.println("Please enter a file name.");
            return;
        }

        String fileName = "savedGames/" + fileNameInput + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
            loadGameChoiceBox.getItems().add(fileNameInput); // Add new file name to choice box
        }
        saveGame(fileName); // Save the game when requested
    }

    private void saveGame(String fileName) {
        try (PrintWriter out = new PrintWriter(fileName)) {
            for (String logEntry : gameStateLog) {
                out.println(logEntry);
            }
            System.out.println("Game saved to: " + fileName);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }


    private void loadGame(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            gameStateLog.clear();
            while (scanner.hasNextLine()) {
                String logEntry = scanner.nextLine();
                gameStateLog.add(logEntry);
                applyLogEntry(logEntry);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }


    private void applyLogEntry(String logEntry) {
        // Example log entry format: "Player dropped a chip in column 3"
        String[] parts = logEntry.split(" ");
        int columnIndex;
        try {
            columnIndex = Integer.parseInt(parts[parts.length - 1]); // Get the column index from the last part of the log entry
        } catch (NumberFormatException e) {
            System.err.println("Error parsing log entry: " + logEntry);
            return;
        }

        // Find the first uncolored button in this column to simulate dropping a chip
        Button buttonToColor = findFirstUncoloredButtonInColumn(columnIndex);
        if (buttonToColor != null) {
            String player = parts[0]; // Assuming the player's name is the first word in the log entry
            String colorStyle;
            if (player.equals("You")) {
                colorStyle = "-fx-background-color: #ff0000;"; // Red for 'You'
            } else {
                colorStyle = "-fx-background-color: #000000;"; // Black for 'Computer'
            }
            String newStyle = colorStyle + "-fx-background-radius: 100;";
            buttonToColor.setStyle(newStyle);
            buttonToColor.setDisable(true);
        }
    }


    private void resetGame() {
        winnerColor.setText("Winner: ");
        for (Button button : buttons) {
            String colorStyle = "-fx-background-color: #cfcbcb;";
            String newStyle = colorStyle + "-fx-background-radius: 100;";
            button.setStyle(newStyle);
            button.setDisable(false);
        }
    }

}