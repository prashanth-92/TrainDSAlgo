package algo;

public class QueenAttack {
    public static void main(String[] args) {
        QueenAttack queen = new QueenAttack();
        System.out.println(
                queen.queensAttack(4, 0, 4, 4, new int[][]{})
        );

    }

    // Complete the queensAttack function below.
    private int queensAttack(int n, int k, int r_q, int c_q, int[][] obstacles) {
        return getPrimaryMoves(n, r_q, c_q, obstacles) +
                getSecondaryMoves(n, r_q, c_q, obstacles);
    }

    private int getPrimaryMoves(int n, int r_q, int c_q, int[][] obstacles){
        return getNorthMoves(r_q, n, obstacles) +
                getSouthMoves(r_q, obstacles) +
                getEastMoves(c_q, obstacles) +
                getWestMoves(c_q, n , obstacles);
    }

    private int getSecondaryMoves(int n, int r_q, int c_q, int[][] obstacles){
        return getNorthEastMoves(r_q, c_q, n, obstacles) +
                getSouthEastMoves(r_q, c_q, n, obstacles) +
                getNorthWestMoves(r_q, c_q, n, obstacles) +
                getSouthWestMoves(r_q, c_q, n, obstacles);
    }

    private int getNorthMoves(int queenRowPosition, int totalRows, int[][] obstacles){
        int count = 0;
        while(queenRowPosition!=totalRows){
            queenRowPosition++;
            count++;
        }
        return count;
    }

    private int getWestMoves(int queenColPosition, int totalRows, int[][] obstacles){
        int count = 0;
        while(queenColPosition!=totalRows){
            queenColPosition++;
            count++;
        }
        return count;
    }

    private int getSouthMoves(int queenRowPosition, int[][] obstacles){
        int count = 0;
        while(queenRowPosition>0){
            queenRowPosition--;
            count++;
        }
        return count;
    }

    private int getEastMoves(int queenColPosition, int[][] obstacles){
        int count = 0;
        while(queenColPosition>0){
            queenColPosition--;
            count++;
        }
        return count;
    }

    private int getNorthEastMoves(int queenRowPosition, int queenColPosition, int totalRows, int[][] obstacles){
        int count = 0;
        while(queenRowPosition!=totalRows && queenColPosition!=totalRows){
            queenColPosition++;
            queenRowPosition++;
            count++;
        }
        return count;
    }

    private int getNorthWestMoves(int queenRowPosition, int queenColPosition, int totalRows, int[][] obstacles){
        int count = 0;
        while(queenRowPosition!=totalRows && queenColPosition>0){
            queenColPosition--;
            queenRowPosition++;
            count++;
        }
        return count;
    }

    private int getSouthWestMoves(int queenRowPosition, int queenColPosition, int totalRows, int[][] obstacles){
        int count = 0;
        while(queenRowPosition>0 && queenColPosition>0){
            queenColPosition--;
            queenRowPosition--;
            count++;
        }
        return count;
    }

    private int getSouthEastMoves(int queenRowPosition, int queenColPosition, int totalRows, int[][] obstacles){
        int count = 0;
        while(queenRowPosition>0 && queenColPosition!=totalRows){
            queenColPosition++;
            queenRowPosition--;
            count++;
        }
        return count;
    }


   /* private boolean isObstaclePresent(int[][] obstacles, int position){
        for(int i=0; i< obstacles.length;i++){
            for(int j=0; j<2; j++){
                if()
            }
        }
        return false;
    }
    */
}
