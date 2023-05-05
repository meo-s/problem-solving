// https://www.acmicpc.net/problem/17306

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class BJ_17306_전쟁_중의_삶 {

    private static class Node {
        public Node left = null;
        public Node right = null;
        public boolean mark = false;
    }

    private static Node add(final Node trie, final BigInteger n, final boolean first) {
        if (n.equals(BigInteger.ZERO)) {
            return trie;
        }

        Node node = add(trie, n.divide(BigInteger.TWO), false);
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            if (node.left == null) {
                node.left = new Node();
            }
            node = node.left;
        } else {
            if (node.right == null) {
                node.right = new Node();
            }
            node = node.right;
        }

        node.mark = node.mark || first;
        return node;
    }

    private static void add(final Node trie, final BigInteger n) {
        add(trie, n, true);
    }

    private static long dfs(final Node trie, boolean count) {
        count = count || trie.mark || (trie.left != null && trie.right != null);
        long numVulnerabilities = count ? 1 : 0;
        numVulnerabilities += trie.left != null ? dfs(trie.left, count) : 0;
        numVulnerabilities += trie.right != null ? dfs(trie.right, count) : 0;
        return numVulnerabilities;
    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            stdin.readLine();
            final StringTokenizer tokens = new StringTokenizer(stdin.readLine());

            final Node root = new Node();
            while (tokens.hasMoreTokens()) {
                add(root, new BigInteger(tokens.nextToken()));
            }

            System.out.println(dfs(root, false));
        }
    }

}
