// https://www.acmicpc.net/problem/11660

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_11660_구간_합_구하기_5 {

    public static void main(final String[] args) throws IOException {
        final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N, M;
        {
            final String[] tokens = stdin.readLine().split(" ");
            N = Integer.parseInt(tokens[0]);
            M = Integer.parseInt(tokens[1]);
        }

        final int[][] dp = new int[N + 1][N + 1];
        for (int x = 1; x <= N; ++x) {
            final StringTokenizer tokens = new StringTokenizer(stdin.readLine());
            for (int y = 1; y <= N; ++y) {
                dp[y][x] = Integer.parseInt(tokens.nextToken()) + dp[y][x - 1] + dp[y - 1][x] - dp[y - 1][x - 1];
            }
        }

        for (int i = 0; i < M; ++i) {
            final String[] tokens = stdin.readLine().split(" ");
            final int x1 = Integer.parseInt(tokens[0]);
            final int y1 = Integer.parseInt(tokens[1]);
            final int x2 = Integer.parseInt(tokens[2]);
            final int y2 = Integer.parseInt(tokens[3]);
            stdout.write((dp[y2][x2] - dp[y2][x1 - 1] - dp[y1 - 1][x2] + dp[y1 - 1][x1 - 1]) + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
