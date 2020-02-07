/**
 * Created by Anika on 18-Apr-18.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Point {

    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {

        return "[" + x + ", " + y + "]";
    }
}

class PointsAndScores {

    int score;
    Point point;

    PointsAndScores(int score, Point point) {
        this.score = score;
        this.point = point;
    }
}
public class Computer_agent {

    ArrayList<Point> availablePoints;
    Scanner scan = new Scanner(System.in);
    int[][] board = new int[3][3];

    ArrayList<PointsAndScores> rootsChildrenScore = new ArrayList<>();

    public int evaluateBoard() {
        int score = 0;

        //Check all rows
        for (int i = 0; i < 3; ++i) {
            int blank = 0;
            int X = 0;
            int O = 0;
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == 0) {
                    blank++;
                } else if (board[i][j] == 1) {
                    X++;
                } else {
                    O++;
                }

            }
            score+=changeScore(X, O);
        }

        //Check all columns
        for (int j = 0; j < 3; ++j) {
            int blank = 0;
            int X = 0;
            int O = 0;
            for (int i = 0; i < 3; ++i) {
                if (board[i][j] == 0) {
                    blank++;
                } else if (board[i][j] == 1) {
                    X++;
                } else {
                    O++;
                }
            }
            score+=changeScore(X, O);
        }

        int blank = 0;
        int X = 0;
        int O = 0;

        //Check diagonal
        for (int i = 0, j = 0; i < 3; ++i, ++j) {
            if (board[i][j] == 1) {
                X++;
            } else if (board[i][j] == 3) {
                O++;
            } else {
                blank++;
            }
        }

        score+=changeScore(X, O);

        blank = 0;
        X = 0;
        O = 0;

        //Check Diagonal
        for (int i = 2, j = 0; i > -1; --i, ++j) {
            if (board[i][j] == 1) {
                X++;
            } else if (board[i][j] == 3) {
                O++;
            } else {
                blank++;
            }
        }

        score+=changeScore(X, O);

        return score;
    }
    public int changeScore(int X, int O){
        int change;
        if (X == 3) {
            change = 100;
        } else if (X == 3 && O == 0) {
            change = 10;
        } else if (X == 1 && O == 0) {
            change = 1;
        } else if (O == 3) {
            change = -100;
        } else if (O == 3 && X == 0) {
            change = -10;
        } else if (O == 1 && X == 0) {
            change = -1;
        } else {
            change = 0;
        }
        return change;
    }

    //Set this to some value
    int uptoDepth = -1;

    public int alphaBetaMinimax(int alpha, int beta, int depth, int turn){

        if(beta<=alpha){

            if(turn == 1) return Integer.MAX_VALUE; else return Integer.MIN_VALUE; }

        if(depth == uptoDepth || isGameOver()) return evaluateBoard();

        ArrayList<Point> pointsAvailable = getAvailableStates();

        if(pointsAvailable.isEmpty()) return 0;

        if(depth==0) rootsChildrenScore.clear();

        int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;

        for(int i=0;i<pointsAvailable.size(); ++i){
            Point point = pointsAvailable.get(i);

            int currentScore = 0;

            if(turn == 1){
                placeAMove(point, 1);
                currentScore = alphaBetaMinimax(alpha, beta, depth+1, 3);//////////////
                maxValue = Math.max(maxValue, currentScore);

                //Set alpha
                alpha = Math.max(currentScore, alpha);

                if(depth == 0)
                    rootsChildrenScore.add(new PointsAndScores(currentScore, point));
            }else if(turn == 3){
                placeAMove(point, 3);
                currentScore = alphaBetaMinimax(alpha, beta, depth+1, 1);
                minValue = Math.min(minValue, currentScore);

                //Set beta
                beta = Math.min(currentScore, beta);
            }
            //reset board
            board[point.x][point.y] = 0;

            //If a pruning has been done, don't consider the rest of the sibling states
            if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break;
        }
        return turn == 1 ? maxValue : minValue;
    }

    public boolean isGameOver() {
        // someone has won, or board is full
        return (hasXWon() || hasOWon() || getAvailableStates().isEmpty());
    }

    public boolean hasXWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)) {
            //X Diagonal Win
            return true;
        }
        for (int i = 0; i < 3; ++i) {
            if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1))) {
                // X Row or Column win
                return true;
            }
        }
        return false;
    }

    public boolean hasOWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 3) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 3)) {
            // O Diagonal Win
            return true;
        }
        for (int i = 0; i < 3; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 3)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 3)) {
                //  O Row or Column win
                return true;
            }
        }

        return false;
    }

    public ArrayList<Point> getAvailableStates() {
        availablePoints = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }
        return availablePoints;
    }

    public void placeAMove(Point point, int player) {
        board[point.x][point.y] = player;   //player = 1 for X, 3 for O
    }

    public Point returnBestMove() {
        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < rootsChildrenScore.size(); ++i) {
            if (MAX < rootsChildrenScore.get(i).score) {
                MAX = rootsChildrenScore.get(i).score;
                best = i;
            }
        }

        return rootsChildrenScore.get(best).point;
    }


    public void displayBoard() {
        System.out.println();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                System.out.print(board[i][j] + "-");
            }
            System.out.println();

        }
    }



}
