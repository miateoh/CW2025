package com.tetris.game.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations(){

    }

    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        boolean returnValue = true;
        if (targetX >= 0 && targetY < matrix.length && targetX < matrix[targetY].length) {
            returnValue = false;
        }
        return returnValue;
    }

    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    public static void merge(int[][] matrix, int[][] shape, int offsetX, int offsetY) {
        for (int x = 0; x < shape.length; x++) {
            for (int y = 0; y < shape[0].length; y++) {

                if (shape[x][y] != 0) {

                    int boardX = offsetX + x;
                    int boardY = offsetY + y;

                    // Boundary safety (important!)
                    if (boardX >= 0 && boardX < matrix.length &&
                            boardY >= 0 && boardY < matrix[0].length) {

                        matrix[boardX][boardY] = shape[x][y];
                    }
                }
            }
        }
    }

    public static ClearRow checkRemoving(int[][] matrix) {

        int width = matrix.length;
        int height = matrix[0].length;

        int removedCount = 0;

        int[][] newMatrix = new int[width][height];

        // Start from bottom row
        int newY = height - 1;

        for (int y = height - 1; y >= 0; y--) {

            boolean fullRow = true;
            for (int x = 0; x < width; x++) {
                if (matrix[x][y] == 0) {
                    fullRow = false;
                    break;
                }
            }

            if (fullRow) {
                removedCount++;
            } else {
                // copy row down
                for (int x = 0; x < width; x++) {
                    newMatrix[x][newY] = matrix[x][y];
                }
                newY--;
            }
        }

        return new ClearRow(newMatrix, removedCount);
    }

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
