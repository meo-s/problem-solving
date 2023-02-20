// https://www.acmicpc.net/problem/2805

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

public class BJ_2805_나무_자르기 {
    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N, M;
            {
                final String[] tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                M = Integer.parseInt(tokens[1]);
            }

            final Integer[] trees = new Integer[N];
            {
                final String[] tokens = stdin.readLine().split(" ");
                for (int i = 0; i < N; ++i) {
                    trees[i] = Integer.parseInt(tokens[i]);
                }
            }

            Arrays.sort(trees, Collections.reverseOrder());

            int numCuttingTrees = 1;
            int currentHeight = trees[0];
            long cuttedLength = 0;
            while (cuttedLength < M) {
                while (numCuttingTrees < N && trees[numCuttingTrees] == currentHeight) {
                    ++numCuttingTrees;
                }

                --currentHeight;
                cuttedLength += numCuttingTrees;
            }

            System.out.print(currentHeight + "\n");
        }
    }
}