// https://www.acmicpc.net/problem/15831

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_15831_준표와_조약돌 {
    @SuppressWarnings("unused")
    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N, B, W;
        {
            final var tokens = stdin.readLine().split(" ");
            N = Integer.parseInt(tokens[0]);
            B = Integer.parseInt(tokens[1]);
            W = Integer.parseInt(tokens[2]);
        }

        final var road = stdin.readLine();
        var maxCourseLength = 0;
        var stones = new int[2]; // 0: 검은 돌, 1: 하얀 돌
        var lb = 0;
        for (var i = 0; i < road.length(); ++i) {
            ++stones[road.charAt(i) == 'B' ? 0 : 1];
            if (B < stones[0]) {
                while (B < stones[0]) {
                    --stones[road.charAt(lb++) == 'B' ? 0 : 1];
                }
            }
            if (W <= stones[1]) {
                maxCourseLength = Math.max(maxCourseLength, stones[0] + stones[1]);
            }
        }

        stdout.write(maxCourseLength + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
