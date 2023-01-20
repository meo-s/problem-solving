import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

// https://www.acmicpc.net/problem/11437

public class BJ_11437 {
    public static class Graph extends HashMap<Integer, ArrayList<Integer>> {
    };

    public static class Tree {
        public static class Node {
            public Node(Node parent_, int index_, int depth_) {
                parent = parent_;
                index = index_;
                depth = depth_;
            }

            public Node parent;
            public int index;
            public int depth;
        }

        private Tree(final Graph graph, final int root) {
            nodes = new Node[graph.size() + 1];
            nodes[root] = new Node(null, root, 0);
            
            final var visited = new boolean[graph.size() + 1];
            visited[root]= true;
            final var pendings = new ArrayDeque<Node>();
            pendings.add(nodes[root]);
            while (0 < pendings.size()) {
                final var node = pendings.pollFirst();
                for (final var childIndex : graph.get(node.index)) {
                    if (!visited[childIndex]) {
                        visited[childIndex] = true;

                        final var child = new Node(node, childIndex, node.depth + 1);
                        pendings.addLast(child);
                        nodes[childIndex] = child;
                    }
                }
            }
        }

        private Node[] nodes;
    }

    public static void main(String args[]) throws IOException {
        final var stdin = new BufferedReader(new InputStreamReader(System.in));
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));

        final var N = Integer.parseInt(stdin.readLine());
        final var graph = new Graph();
        for (var i = 1; i < N; ++i) {
            final var tokens = new StringTokenizer(stdin.readLine());
            final var u = Integer.parseInt(tokens.nextToken());
            final var v = Integer.parseInt(tokens.nextToken());

            IntStream.of(u, v).forEach(key -> {
                if (!graph.containsKey(key)) {
                    graph.put(key, new ArrayList<>());
                }
            });

            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        final var tree = new Tree(graph, 1);

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
