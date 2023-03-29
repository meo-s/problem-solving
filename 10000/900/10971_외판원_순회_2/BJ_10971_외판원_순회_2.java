// https://www.acmicpc.net/problem/10971

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BJ_10971_외판원_순회_2 {
    public static void dfs(int[][] edges, int[][] costs, int u, int cost, int visitmask) {
        final int N = edges.length;
        for (int v = 0; v < N; ++v) {
            if (v == 0 && visitmask != (1 << N) - 2) {
                continue;
            }

            if (edges[u][v] != 0 && (visitmask & (1 << v)) == 0) {
                if (cost + edges[u][v] < costs[v][visitmask]) {
                    costs[v][visitmask] = cost + edges[u][v];
                    dfs(edges, costs, v, costs[v][visitmask], visitmask | (1 << v));
                }
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N = Integer.parseInt(stdin.readLine());
            final int[][] edges = new int[N][N];
            for (int u = 0; u < N; ++u) {
                final String[] tokens = stdin.readLine().split(" ");
                for (int v = 0; v < N; ++v) {
                    edges[u][v] = Integer.parseInt(tokens[v]);
                }
            }

            final int[][] costs = new int[N][1 << N];
            for (final int[] row : costs) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }

            costs[0][0] = 0;
            dfs(edges, costs, 0, 0, 0);

            System.out.println(costs[0][(1 << N) - 2]);
        }
    }
}
