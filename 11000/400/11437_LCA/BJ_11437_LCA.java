// https://www.acmicpc.net/problem/11437

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class BJ_11437_LCA {
    public static class Graph extends ArrayList<ArrayList<Integer>> {
        public Graph(final int V) {
            for (var i = 0; i <= V; ++i) {
                add(new ArrayList<>());
            }
        }

        public static Graph from(final BufferedReader br) throws IOException {
            final var V = Integer.parseInt(br.readLine());
            final var g = new Graph(V);
            for (var i = 1; i < V; ++i) {
                final var tokens = br.readLine().split(" ");
                final var u = Integer.parseInt(tokens[0]);
                final var v = Integer.parseInt(tokens[1]);
                g.get(u).add(v);
                g.get(v).add(u);
            }

            return g;
        }
    }

    public static class Tree {
        public static class Node {
            public final int parent;
            public final int depth;

            public Node(final int parent, final int depth) {
                this.parent = parent;
                this.depth = depth;
            }
        }

        public static int lowestCommonAncestorOf(final Node[] tree, int nodeA, int nodeB) {
            while (tree[nodeA].depth < tree[nodeB].depth) {
                nodeB = tree[nodeB].parent;
            }
            while (tree[nodeB].depth < tree[nodeA].depth) {
                nodeA = tree[nodeA].parent;
            }
            while (nodeA != nodeB) {
                nodeA = tree[nodeA].parent;
                nodeB = tree[nodeB].parent;
            }
            return nodeA;
        }

        public static Node[] from(final Graph g, final int root) {
            final var tree = new Node[g.size()];
            tree[root] = new Node(0, 0);

            final var visited = new boolean[g.size()];
            visited[root] = true;

            final var pendings = new ArrayDeque<Integer>();
            pendings.addLast(root);

            var depth = 0;
            while (0 < pendings.size()) {
                depth += 1;
                var remains = pendings.size();
                while (0 <= --remains) {
                    final var u = pendings.pollFirst();
                    for (final var v : g.get(u)) {
                        if (!visited[v]) {
                            visited[v] = true;
                            tree[v] = new Node(u, depth);
                            pendings.add(v);
                        }
                    }
                }
            }

            return tree;
        }
    }

    public static void main(String args[]) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var tree = Tree.from(Graph.from(stdin), 1);

            var Q = Integer.parseInt(stdin.readLine());
            while (0 <= --Q) {
                final var tokens = stdin.readLine().split(" ");
                final var nodeA = Integer.parseInt(tokens[0]);
                final var nodeB = Integer.parseInt(tokens[1]);
                stdout.write(Tree.lowestCommonAncestorOf(tree, nodeA, nodeB) + "\n");
            }

            stdout.flush();
        }
    }
}
