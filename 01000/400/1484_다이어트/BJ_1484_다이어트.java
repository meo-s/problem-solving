// https://www.acmicpc.net/problem/1484

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_1484_다이어트 {
    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var G = Integer.parseInt(stdin.readLine());
        var numFindings = 0;
        var x = 2L;
        var y = 1L;
        while (y < x) {
            if (x * x - y * y == G) {
                stdout.write(x++ + "\n");
                ++numFindings;
            } else if (x * x - y * y < G) {
                ++x;
            } else {
                ++y;
            }
        }

        if (numFindings == 0) {
            stdout.write("-1\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
