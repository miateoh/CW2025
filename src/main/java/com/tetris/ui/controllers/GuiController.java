/**
 * Handles all JavaFX UI updates for the game board, falling brick,
 * score display, and notifications.
 *
 * Delegates all game logic to GameController and acts purely as the view layer.
 */

package com.tetris.ui.controllers;

import com.tetris.ui.views.ViewData;
import com.tetris.game.bricks.DownData;
import com.tetris.game.events.EventSource;
import com.tetris.game.events.EventType;
import com.tetris.game.events.MoveEvent;
import com.tetris.game.logic.InputEventListener;
import com.tetris.ui.views.GameOverPanel;
import com.tetris.ui.views.NotificationPanel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final int PREVIEW_SIZE = 15;

    @FXML private GridPane gamePanel;
    @FXML private GridPane brickPanel;

    @FXML private Pane nextPiecePane;
    @FXML private Pane holdPiecePane;
    @FXML private Group groupNotification;
    @FXML private GameOverPanel gameOverPanel;

    @FXML private GridPane nextPieceGrid;
    @FXML private GridPane holdPieceGrid;

    @FXML private Label scoreLabel;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;

    private Timeline timeLine;
    private InputEventListener eventListener;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);

        gameOverPanel.setVisible(false);
    }

    private void handleKeyPress(KeyEvent keyEvent) {
        if (!isPause.get() && !isGameOver.get()) {

            switch (keyEvent.getCode()) {
                case LEFT, A -> refreshBrick(eventListener.onLeftEvent(
                        new MoveEvent(EventType.LEFT, EventSource.USER)));

                case RIGHT, D -> refreshBrick(eventListener.onRightEvent(
                        new MoveEvent(EventType.RIGHT, EventSource.USER)));

                case UP, W -> refreshBrick(eventListener.onRotateEvent(
                        new MoveEvent(EventType.ROTATE, EventSource.USER)));

                case DOWN, S -> moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));

                case SPACE -> {
                    DownData data = eventListener.onHardDropEvent(
                            new MoveEvent(EventType.DOWN, EventSource.USER)
                    );

                    if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
                        NotificationPanel n =
                                new NotificationPanel("+" + data.getClearRow().getScoreBonus());
                        groupNotification.getChildren().add(n);
                        n.showScore(groupNotification.getChildren());
                    }

                    refreshBrick(data.getViewData());
                }

                case C -> refreshBrick(
                        eventListener.onHoldEvent(new MoveEvent(EventType.ROTATE, EventSource.USER))
                );
            }
        }

        if (keyEvent.getCode() == KeyCode.N) newGame(null);
        keyEvent.consume();
    }

    // ---------------------------------------------------------
    // INITIAL SETUP
    // ---------------------------------------------------------

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        // Background grid (skip first 2 rows)
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                r.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = r;
                gamePanel.add(r, j, i - 2);
            }
        }

        // Falling brick
        int[][] data = brick.getBrickData();
        rectangles = new Rectangle[data.length][data[0].length];

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {
                Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                r.setFill(getFillColor(data[y][x]));
                rectangles[y][x] = r;
                brickPanel.add(r, x, y);
            }
        }

        updateBrickPanelPosition(brick);

        initializeNextPieceDisplay(brick.getNextBrickData());
        initializeHoldPieceDisplay();

        // TIMER
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                t -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    // ---------------------------------------------------------
    // PREVIEW PANELS
    // ---------------------------------------------------------

    private void initializeNextPieceDisplay(int[][] next) {
        updatePreviewGrid(nextPieceGrid, next);
    }

    private void initializeHoldPieceDisplay() {
        updatePreviewGrid(holdPieceGrid, null);
    }

    private void updatePreviewGrid(GridPane grid, int[][] data) {
        grid.getChildren().clear();

        if (data == null) return;

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {
                Rectangle r = new Rectangle(PREVIEW_SIZE, PREVIEW_SIZE);
                r.setFill(getFillColor(data[y][x]));
                r.setArcHeight(5);
                r.setArcWidth(5);
                grid.add(r, x, y);
            }
        }
    }

    // ---------------------------------------------------------
    // BRICK + GHOST REFRESH
    // ---------------------------------------------------------

    private void refreshBrick(ViewData brick) {
        if (isPause.get() || isGameOver.get()) return;

        updateBrickPanelPosition(brick);

        int[][] shape = brick.getBrickData();
        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                setRectangleData(shape[y][x], rectangles[y][x]);
            }
        }

        drawGhost(brick);

        updatePreviewGrid(nextPieceGrid, brick.getNextBrickData());
        updatePreviewGrid(holdPieceGrid, brick.getHoldBrickData());
    }

    private void updateBrickPanelPosition(ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(gamePanel.getLayoutY() - 42 + brick.getyPosition() * BRICK_SIZE);
    }

    // ---------------------------------------------------------
    // GHOST PIECE
    // ---------------------------------------------------------

    private void drawGhost(ViewData brick) {
        gamePanel.getChildren().removeIf(
                n -> n instanceof Rectangle && ((Rectangle) n).getOpacity() == 0.3
        );

        if (isGameOver.get()) return;

        int[][] shape = brick.getBrickData();
        int ghostY = brick.getGhostLandingY();

        for (int sy = 0; sy < shape.length; sy++) {
            for (int sx = 0; sx < shape[sy].length; sx++) {
                if (shape[sy][sx] != 0) {
                    int boardX = brick.getxPosition() + sx;
                    int boardY = ghostY + sy - 2;

                    if (boardY >= 0 && boardX >= 0) {
                        Rectangle g = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                        g.setFill(Color.GRAY);
                        g.setOpacity(0.3);
                        g.setArcWidth(9);
                        g.setArcHeight(9);
                        gamePanel.add(g, boardX, boardY);
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------
    // BACKGROUND + SCORE
    // ---------------------------------------------------------

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int val, Rectangle r) {
        r.setFill(getFillColor(val));
        r.setArcWidth(9);
        r.setArcHeight(9);
    }

    private Paint getFillColor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }

    // ---------------------------------------------------------
    // GAME SAFETY + HOOKS
    // ---------------------------------------------------------

    private void moveDown(MoveEvent e) {
        DownData data = eventListener.onDownEvent(e);

        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel n = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(n);
            n.showScore(groupNotification.getChildren());
        }

        refreshBrick(data.getViewData());
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener listener) {
        this.eventListener = listener;
    }

    public void bindScore(IntegerProperty score) {
        score.addListener((o, oldVal, newVal) ->
                scoreLabel.setText("SCORE: " + newVal)
        );
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.set(true);
    }

    public void newGame(ActionEvent e) {
        timeLine.stop();
        gameOverPanel.setVisible(false);

        eventListener.createNewGame();
        refreshGameBackground(new int[0][0]);

        gamePanel.requestFocus();
        timeLine.play();

        isPause.set(false);
        isGameOver.set(false);
    }
}


