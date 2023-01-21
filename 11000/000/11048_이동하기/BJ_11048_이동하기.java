// https://www.acmicpc.net/problem/11048

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_11048_이동하기 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        int H, W;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            H = Integer.parseInt(tokens.nextToken());
            W = Integer.parseInt(tokens.nextToken());
        }

        final var miro = new int[H + 1][W + 1];
        for (var h = 1; h <= H; ++h) {
            final var tokens = new StringTokenizer(stdin.readLine());
            for (var w = 1; w <= W; ++w) {
                miro[h][w] = Integer.parseInt(tokens.nextToken());
                miro[h][w] += Math.max(miro[h - 1][w - 1], Math.max(miro[h - 1][w], miro[h][w - 1]));
            }
        }

        stdout.write(miro[H][W] + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
