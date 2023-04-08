// https://www.acmicpc.net/problem/2633

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BJ_2633_사회망_서비스_SNS {
    public static class Graph extends ArrayList<List<Integer>> {
        public Graph(final int N) {
            for (int i = 0; i < N; ++i) {
                this.add(new ArrayList<>());
            }
        }
    }

    public static int dfs(final Graph g, final int u, final int parent, final int flag, final int[][] dp) {
        if (dp[u][flag] == -1) {
            dp[u][flag] = flag;
            for (final var v : g.get(u)) {
                if (v != parent) {
                    var cost = dfs(g, v, u, flag ^ 1, dp);
                    if (flag == 1) {
                        cost = Math.min(cost, dfs(g, v, u, 1, dp));
                    }
                    dp[u][flag] += cost;
                }
            }
        }

        return dp[u][flag];
    }

    public static int dfs(final Graph g) {
        final var dp = new int[g.size()][2];
        for (final var row : dp) {
            row[0] = row[1] = -1;
        }

        dfs(g, 0, -1, 0, dp);
        dfs(g, 0, -1, 1, dp);
        return Math.min(dp[0][0], dp[0][1]);
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var V = Integer.parseInt(stdin.readLine());
            final var g = new Graph(V);
            for (var i = 0; i < V - 1; ++i) {
                final var tokens = stdin.readLine().split(" ");
                final var u = Integer.parseInt(tokens[0]);
                final var v = Integer.parseInt(tokens[1]);
                g.get(u - 1).add(v - 1);
                g.get(v - 1).add(u - 1);
            }

            System.out.println(dfs(g));
        }
    }
}
