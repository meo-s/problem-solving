// https://www.acmicpc.net/problem/2563

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class Baekjoon_2563 {

    public static void main(String[] args) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));

        int N = Integer.parseInt(stdin.readLine());
        int[] attached = new int[10000];
        for (int i = 0; i < N; ++i) {
            String[] tokens = stdin.readLine().split(" ");
            final int x = Integer.parseInt(tokens[0]);
            final int y = Integer.parseInt(tokens[1]);
            for (int dy = 0; dy < 10; ++dy) {
                for (int dx = 0; dx < 10; ++dx) {
                    if (y + dy < 100 && x + dx < 100)
                        attached[(y + dy) * 100 + (x + dx)] = 1;
                }
            }
        }

        stdout.write(Arrays.stream(attached).sum() + "");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
