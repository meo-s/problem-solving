// https://www.acmicpc.net/problem/11578

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_11578_CCW {
    public static int cross(final int x1, final int y1, final int x2, final int y2) {
        return x1 * y2 - x2 * y1;
    }

    public static int sign(final int v) {
        return v != 0 ? v / Math.abs(v) : 0;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var pt = new int[3][2];
        for (var i = 0; i < 3; ++i) {
            final var tokens = stdin.readLine().split(" ");
            pt[i][0] = Integer.parseInt(tokens[0]);
            pt[i][1] = Integer.parseInt(tokens[1]);
        }

        final var x1 = pt[1][0] - pt[0][0];
        final var y1 = pt[1][1] - pt[0][1];
        final var x2 = pt[2][0] - pt[1][0];
        final var y2 = pt[2][1] - pt[1][1];
        stdout.write(sign(cross(x1, y1, x2, y2)) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
