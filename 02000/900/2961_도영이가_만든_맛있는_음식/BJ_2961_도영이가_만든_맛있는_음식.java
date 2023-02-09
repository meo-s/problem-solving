// https://www.acmicpc.net/problem/2961

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_2961_도영이가_만든_맛있는_음식 {

    public static class DoZero {
        public DoZero(final int[][] ingredients) {
            this.ingredients = ingredients;
        }

        public int makeBestDish(final int sour, final int bitter, final int lb) {
            if (lb == this.ingredients.length) {
                return 0 < sour + bitter ? Math.abs(sour - bitter) : Integer.MAX_VALUE;
            }

            int minTasteDiff = Integer.MAX_VALUE;
            for (int i = lb; i < this.ingredients.length; ++i) {
                final int newSour = Math.max(ingredients[i][0], sour * ingredients[i][0]);
                final int newBitter = bitter + ingredients[i][1];
                minTasteDiff = Math.min(minTasteDiff, makeBestDish(newSour, newBitter, i + 1));
                minTasteDiff = Math.min(minTasteDiff, makeBestDish(sour, bitter, i + 1));
            }

            return minTasteDiff;
        }

        public int makeBestDish() {
            return makeBestDish(0, 0, 0);
        }

        private int[][] ingredients;
    }

    public static void main(final String[] args) throws IOException {
        final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N = Integer.parseInt(stdin.readLine());
        final int[][] ingredients = new int[N][2];
        for (int i = 0; i < N; ++i) {
            final String[] tokens = stdin.readLine().split(" ");
            for (int j = 0; j < 2; ++j) {
                ingredients[i][j] = Integer.parseInt(tokens[j]);
            }
        }

        stdout.write(new DoZero(ingredients).makeBestDish() + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
