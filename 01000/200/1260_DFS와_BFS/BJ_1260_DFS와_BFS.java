// https://www.acmicpc.net/problem/1260

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

public class BJ_1260_DFS와_BFS {
    public static class Graph extends ArrayList<ArrayList<Integer>> {
        public Graph(final int V) {
            super();
            for (int i = 0; i <= V; ++i) {
                add(new ArrayList<>());
            }
        }

        public StringBuilder dfs(final int u, final boolean[] visited, final StringBuilder trace) {
            visited[u] = true;
            trace.append(u);
            trace.append(' ');

            for (final int v : get(u)) {
                if (!visited[v]) {
                    dfs(v, visited, trace);
                }
            }

            return trace;
        }

        public String bfs(final int start) {
            final StringBuilder trace = new StringBuilder();

            final boolean[] visited = new boolean[size()];
            visited[start] = true;
            final Deque<Integer> pendings = new ArrayDeque<>();
            pendings.addLast(start);
            while (0 < pendings.size()) {
                final int u = pendings.pollFirst();
                trace.append(u);
                trace.append(' ');

                for (final int v : get(u)) {
                    if (!visited[v]) {
                        visited[v] = true;
                        pendings.addLast(v);
                    }
                }
            }

            return trace.toString();
        }

        public String dfs(final int u) {
            final boolean[] visited = new boolean[size()];
            return dfs(u, visited, new StringBuilder()).toString();
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int V, E, S;
            {
                final String[] tokens = stdin.readLine().split(" ");
                V = Integer.parseInt(tokens[0]);
                E = Integer.parseInt(tokens[1]);
                S = Integer.parseInt(tokens[2]);
            }

            final Graph g = new Graph(V);
            for (int i = 0; i < E; ++i) {
                final String[] tokens = stdin.readLine().split(" ");
                final int u = Integer.parseInt(tokens[0]);
                final int v = Integer.parseInt(tokens[1]);
                g.get(u).add(v);
                g.get(v).add(u);
            }

            for (int i = 1; i <= V; ++i) {
                Collections.sort(g.get(i));
            }

            stdout.write(g.dfs(S) + "\n");
            stdout.write(g.bfs(S) + "\n");

            stdout.flush();
        }
    }
}
