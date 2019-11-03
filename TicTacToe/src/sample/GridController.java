package sample;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class GridController {

    @FXML
    public TilePane mainPane;
    private GridPane grid;
    private boolean isXTurn;
    private ImageView turnImage;
    private char[][] gameBoard;
    private  DialogPane turnText;
    private boolean isGameEnded;
    private Button rstGame;
    private int numTurns;

    public void initialize() throws FileNotFoundException {
        int numCols = 3;
        int numRows = 3;
        gameBoard = new char[][]{{'e', 'e', 'e'}, {'e', 'e', 'e'}, {'e', 'e', 'e'}};
        isXTurn = true;
        isGameEnded = false;
        numTurns = 0;
        rstGame = new Button();
        rstGame.setText("Reset Game");
        rstGame.setOnMouseClicked(d -> {
            mainPane.getChildren().remove(0, mainPane.getChildren().size());
            try {
                initialize();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        rstGame.setVisible(false);

        mainPane.setTileAlignment(Pos.CENTER_LEFT);
        mainPane.setOrientation(Orientation.HORIZONTAL);
        grid = new GridPane();
        grid.setGridLinesVisible(true);
        turnText = new DialogPane();
        turnText.setMinSize(50, 15);
        turnText.setMaxSize(80, 15);
        turnText.setContentText("Turn: ");
        turnImage = new ImageView();
        mainPane.getChildren().add(turnText);
        mainPane.getChildren().add(turnImage);
        mainPane.getChildren().add(grid);
        mainPane.getChildren().add(rstGame);
        mainPane.setPrefTileWidth(80);
        turnImage.setImage(new Image(new FileInputStream("x.jpg"), 30, 30, true, true));


        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setMinWidth(100);
            grid.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(100);
            grid.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                addPane(i, j);
            }
        }
    }

    private void addPane(int colIndex, int rowIndex) {
        javafx.scene.image.ImageView paneImg = new javafx.scene.image.ImageView();
        Pane pane = new Pane();
        pane.setMinSize(90,90);
        pane.getChildren().add(paneImg);
        pane.setOnMouseClicked(e -> {
            if(gameBoard[colIndex][rowIndex] == 'e' && !isGameEnded) {
                String oImage = "o.jpg";
                String xImage = "x.jpg";
                try {
                    numTurns++;
                    if(isXTurn) {
                        gameBoard[colIndex][rowIndex] = 'x';
                        isXTurn = false;
                        turnImage.setImage(new Image(new FileInputStream(oImage), 30, 30, true, true));
                        paneImg.setImage(new Image(new FileInputStream(xImage), 90, 90, true, true));
                        if (checkRowsAndColumns(colIndex, rowIndex, 'x') || numTurns >= 9) {
                            String endString;
                            if(checkRowsAndColumns(colIndex, rowIndex, 'x') )
                                endString = "Game Ended, X Wins!";
                            else
                                endString = "Draw!";
                            turnText.setContentText(endString);
                            turnImage.setVisible(false);
                            isGameEnded = true;
                            rstGame.setVisible(true);
                        }
                    }
                    else{
                        gameBoard[colIndex][rowIndex] = 'o';
                        isXTurn = true;
                        turnImage.setImage(new Image(new FileInputStream(xImage), 30, 30, true, true));
                        paneImg.setImage(new Image(new FileInputStream(oImage), 90, 90, true, true));
                        if (checkRowsAndColumns(colIndex, rowIndex, 'o') || numTurns >= 9) {
                            String endString;
                            if(checkRowsAndColumns(colIndex, rowIndex, 'o') )
                                endString = "Game Ended, 0 Wins!";
                            else
                                endString = "Draw!";
                            turnText.setContentText(endString);
                            turnImage.setVisible(false);
                            isGameEnded = true;
                            rstGame.setVisible(true);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        grid.add(pane, colIndex, rowIndex);
    }

    private boolean checkRowsAndColumns(int col, int row, char type){
        boolean wonGame = true;
        for(int i=0; i<gameBoard.length; i++){
            if(gameBoard[col][i] != type){
                wonGame =  false;
                break;
            }
        }
        if(wonGame)
            return true;
        wonGame = true;
        for(int i=0; i<gameBoard[col].length; i++){
            if(gameBoard[i][row] != type){
                wonGame = false;
                break;
            }
        }
        if(wonGame)
            return true;
        if(gameBoard[1][1] == type){
            if(gameBoard[2][2] == type && gameBoard[0][0] == type){
                return true;
            }
            if(gameBoard[2][0] == type && gameBoard[0][2] == type){
                return true;
            }
        }
        return false;
    }
}
