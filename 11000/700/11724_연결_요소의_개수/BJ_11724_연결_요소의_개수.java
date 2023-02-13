// https://www.acmicpc.net/problem/11724

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class BJ_11724_연결_요소의_개수 {
    public static class Graph extends ArrayList<ArrayList<Integer>> {
        public void dfs(final int u, final boolean[] visited) {
            visited[u] = true;
            for (final var v : get(u)) {
                if (!visited[v]) {
                    dfs(v, visited);
                }
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int V, E;
            {
                final var tokens = stdin.readLine().split(" ");
                V = Integer.parseInt(tokens[0]);
                E = Integer.parseInt(tokens[1]);
            }

            final var graph = new Graph();
            for (var i = 0; i < V; ++i) {
                graph.add(new ArrayList<>());
            }

            for (var i = 0; i < E; ++i) {
                final var tokens = stdin.readLine().split(" ");
                final var u = Integer.parseInt(tokens[0]) - 1;
                final var v = Integer.parseInt(tokens[1]) - 1;
                graph.get(u).add(v);
                graph.get(v).add(u);
            }

            var visited = new boolean[graph.size()];
            var numConnectedComponents = 0;
            for (var i = 0; i < V; ++i) {
                if (!visited[i]) {
                    graph.dfs(i, visited);
                    ++numConnectedComponents;
                }
            }

            stdout.write(numConnectedComponents + "\n");
            stdout.flush();
        }
    }
}
