package algo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
Determine if a 9x9 Sudoku board is valid. Only the filled cells need to be validated according to the following rules:

Each row must contain the digits 1-9 without repetition.
Each column must contain the digits 1-9 without repetition.
Each of the 9 3x3 sub-boxes of the grid must contain the digits 1-9 without repetition.

A partially filled sudoku which is valid.

The Sudoku board could be partially filled, where empty cells are filled with the character '.'.

Example 1:

Input:
[
  ["5","3",".",".","7",".",".",".","."],
  ["6",".",".","1","9","5",".",".","."],
  [".","9","8",".",".",".",".","6","."],
  ["8",".",".",".","6",".",".",".","3"],
  ["4",".",".","8",".","3",".",".","1"],
  ["7",".",".",".","2",".",".",".","6"],
  [".","6",".",".",".",".","2","8","."],
  [".",".",".","4","1","9",".",".","5"],
  [".",".",".",".","8",".",".","7","9"]
]
Output: true
Example 2:

Input:
[
  ["8","3",".",".","7",".",".",".","."],
  ["6",".",".","1","9","5",".",".","."],
  [".","9","8",".",".",".",".","6","."],
  ["8",".",".",".","6",".",".",".","3"],
  ["4",".",".","8",".","3",".",".","1"],
  ["7",".",".",".","2",".",".",".","6"],
  [".","6",".",".",".",".","2","8","."],
  [".",".",".","4","1","9",".",".","5"],
  [".",".",".",".","8",".",".","7","9"]
]
Output: false
Explanation: Same as Example 1, except with the 5 in the top left corner being
    modified to 8. Since there are two 8's in the top left 3x3 sub-box, it is invalid.
Note:

A Sudoku board (partially filled) could be valid but is not necessarily solvable.
Only the filled cells need to be validated according to the mentioned rules.
The given board contain only digits 1-9 and the character '.'.
The given board size is always 9x9.
 */
public class ValidSudoku {
    public static void main(String [] args){
        final ValidSudoku vs = new ValidSudoku();
        final char[][] board = {  {'.','.','.','.','5','.','.','1','.'},
                            {'.','4','.','3','.','.','.','.','.'},
                            {'.','.','.','.','.','3','.','.','1'},
                            {'8','.','.','.','.','.','.','2','.'},
                            {'.','.','2','.','7','.','.','.','.'},
                            {'.','1','5','.','.','.','.','.','.'},
                            {'.','.','.','.','.','2','.','.','.'},
                            {'.','2','.','9','.','.','.','.','.'},
                            {'.','.','4','.','.','.','.','.','.'}};
        System.out.println(vs.isSudokuValid(board));
    }

    private boolean isSudokuValid(char[][] board) throws NumberFormatException{
        if(!isAllRowsValid(board)){
            return false;
        }
        if(!isAllColumnsValid(board)){
            return false;
        }
        if(!isAllMiniSudokusValid(board)){
            return false;
        }
        return true;
    }

    private boolean isAllRowsValid(char [][] board){
        for(int i = 0; i<9 ; i++){
            final char [] row = getRow(board, i);
            if(!isRowOrColumnValid(row)){
                return false;
            }
        }
        return true;
    }

    private boolean isAllColumnsValid(char [][] board){
        for(int i = 0; i<9 ; i++){
            final char [] column = getColumn(board, i);
            if(!isRowOrColumnValid(column)){
                return false;
            }
        }
        return true;
    }


    private boolean isAllMiniSudokusValid(char[][] board){
        for(int xStart = 0 ; xStart < 9; xStart = xStart+3){
            for(int yStart = 0 ; yStart < 9; yStart = yStart+3){
                final char[][] miniSudoku = getMiniSudoku(board, xStart, yStart);
                if(!isMiniSudokuValid(miniSudoku)){
                    return false;
                }
            }
        }
        return true;
    }

    private char[] getRow(char[][] board, int rowNumber){
        return board[rowNumber];
    }

    private char[] getColumn(char[][] board, int columnNumber){
        final char [] column = new char[9];
        for(int count=0; count<9; count++){
            column[count] = board[count][columnNumber];
        }
        return column;
    }



    private boolean isRowOrColumnValid(char [] elements){
        final Map<Integer, Boolean> checkerMap = getCheckerMap();
        for(int count = 0; count< elements.length; count++){
            final char elem = elements[count];
            if(elem == '.'){
                continue;
            }
            final Integer elemNumValue = Character.getNumericValue(elem);
            if(null==checkerMap.get(elemNumValue) || checkerMap.get(elemNumValue)){
                return false;
            }
            checkerMap.put(elemNumValue, true);
        }
        return true;
    }

    private char[][] getMiniSudoku(char [][] mainSudoku, int xStart, int yStart){
        final char [][] miniSudoku = new char[3][3];
        //For mini Sudoku
        int x = 0, y= 0;
        for(int i = xStart; i<xStart+3; i++){
            for(int j = yStart; j<yStart+3; j++){
                miniSudoku[x][y] = mainSudoku[i][j];
                y++;
            }
            //reset y for mini sudoku
            y=0;
            x++;
        }
        return miniSudoku;
    }

    private boolean isMiniSudokuValid(char [][] miniSudoku){
        final Map<Integer, Boolean> checkerMap = getCheckerMap();
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                final char elem = miniSudoku[i][j];
                if(elem == '.'){
                    continue;
                }
                final Integer elemNumValue = Character.getNumericValue(elem);
                if(checkerMap.get(elemNumValue)){
                    return false;
                }
                checkerMap.put(elemNumValue, true);
            }
        }
        return true;
    }

    private List<Integer> getValidNumbers(){
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    private Map<Integer, Boolean> getCheckerMap(){
        return getValidNumbers().stream()
                .collect(
                        Collectors.toMap(Function.identity(), (t)->false)
                );
    }
}
