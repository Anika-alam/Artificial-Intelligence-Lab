import java.util.Random;
import java.util.Scanner;

/**
 * Created by Anika on 18-Apr-18.
 */
public class Human_agent {
    public static void main(String[] args) {
        Computer_agent b = new Computer_agent();



        b.displayBoard();
        Scanner player= new Scanner(System.in);
        Random rand= new Random();
        int i=rand.nextInt(2);
        System.out.println("First move: 1=computer,0=human\n"+i);


        if (i == 1) {
            Point p = new Point(rand.nextInt(3), rand.nextInt(3));
            b.placeAMove(p, 1);
            b.displayBoard();
        }

        while (!b.isGameOver()) {
            System.out.println("Your move: first enter row, then column");

            Point userMove = new Point(b.scan.nextInt(), b.scan.nextInt());

            b.placeAMove(userMove,3); //3 for O and O is the user
            b.displayBoard();
            if (b.isGameOver()) break;

            b.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
            for (PointsAndScores pas : b.rootsChildrenScore)


                b.placeAMove(b.returnBestMove(), 1);
            b.displayBoard();
        }
        if (b.hasXWon()) {
            System.out.println("Unfortunately, you lost!");
        } else if (b.hasOWon()) {
            System.out.println("You win!");
        } else {
            System.out.println("It's a draw!");
        }
    }

}
