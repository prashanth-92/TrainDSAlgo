package algo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ValidSudoku {
    public static void main(String [] args){
        ValidSudoku vs = new ValidSudoku();
        /*char[][] board = new char [9][9];
        board[0] = new char[]{'5','3','.','.','7','.','.','.','.'};
        board[1] = new char[]{'6','.','.','1','9','5','.','.','.'};
        board[2] = new char[]{'.','9','8','.','.','.','.','6','.'};
        board[3] = new char[]{'8','.','.','.','6','.','.','.','3'};
        board[4] = new char[]{'4','.','.','8','.','3','.','.','1'};
        board[5] = new char[]{'7','.','.','.','2','.','.','.','6'};
        board[6] = new char[]{'.','6','.','.','.','.','2','8','.'};
        board[7] = new char[]{'.','.','.','4','1','9','.','.','5'};
        board[8] = new char[]{'.','.','.','.','8','.','.','7','9'};
        System.out.println(vs.isSudokuValid(board));*/
        /*
        [[".",".",".",".","5",".",".","1","."],[".","4",".","3",".",".",".",".","."],
        [".",".",".",".",".","3",".",".","1"],["8",".",".",".",".",".",".","2","."],
        [".",".","2",".","7",".",".",".","."],[".","1","5",".",".",".",".",".","."],
        [".",".",".",".",".","2",".",".","."],[".","2",".","9",".",".",".",".","."],
        [".",".","4",".",".",".",".",".","."]]

         */

        char[][] board = new char [9][9];
        board[0] = new char[]{'.','.','4','.','.','.','6','3','.'};
        board[1] = new char[]{'.','.','.','.','.','.','.','.','.'};
        board[2] = new char[]{'.','.','.','.','.','.','.','9','.'};
        board[3] = new char[]{'.','.','.','5','6','.','.','.','.'};
        board[4] = new char[]{'4','3','.','.','.','.','.','.','1'};
        board[5] = new char[]{'.','.','.','7','.','.','.','.','.'};
        board[6] = new char[]{'.','.','.','5','.','.','.','.','.'};
        board[7] = new char[]{'.','.','.','.','.','.','.','.','.'};
        board[8] = new char[]{'.','.','.','.','.','.','.','.','.'};
        System.out.println(vs.isSudokuValid(board));
    }

    public boolean isSudokuValid(char[][] board) throws NumberFormatException{
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
            char [] row = getRow(board, i);
            if(!isRowOrColumnValid(row)){
                return false;
            }
        }
        return true;
    }

    private boolean isAllColumnsValid(char [][] board){
        for(int i = 0; i<9 ; i++){
            char [] column = getColumn(board, i);
            if(!isRowOrColumnValid(column)){
                return false;
            }
        }
        return true;
    }


    private boolean isAllMiniSudokusValid(char[][] board){
        for(int xStart = 0 ; xStart < 3; xStart = xStart+3){
            for(int yStart = 0 ; yStart < 3; yStart = yStart+3){
                char[][] miniSudoku = getMiniSudoku(board, xStart, yStart);
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
        char [] column = new char[9];
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
        char [][] miniSudoku = new char[3][3];
        //For mini Sudoku
        int x = 0, y= 0;
        for(int i = xStart; i<3; i++){
            for(int j = yStart; j<3; j++){
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
