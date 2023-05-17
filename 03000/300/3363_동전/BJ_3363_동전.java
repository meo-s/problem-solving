// https://www.acmicpc.net/problem/3363

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class BJ_3363_동전 {

    public static class ItemGetter {

        private final int index;

        public ItemGetter(final int index) {
            this.index = index;
        }

        public int get(final int[] a) {
            return a[index];
        }

    }

    public static class Expression {

        private final ItemGetter[][] getters = new ItemGetter[2][4];
        private char relation;

        public boolean evaluate(final int[] a) {
            final int lhs = Arrays.stream(getters[0]).map(ig -> ig.get(a)).reduce((x, y) -> x + y).get();
            final int rhs = Arrays.stream(getters[1]).map(ig -> ig.get(a)).reduce((x, y) -> x + y).get();
            if (relation == '<') {
                return lhs < rhs;
            } else if (relation == '>') {
                return rhs < lhs;
            } else {
                return lhs == rhs;
            }
        }

        public static Expression from(final BufferedReader br) throws IOException {
            final Expression ret = new Expression();
            for (int i = 0; i < 9; ++i) {
                if (i != 4) {
                    ret.getters[i / 5][i - 5 * (i / 5)] = new ItemGetter(readInt(br) - 1);
                } else {
                    ret.relation = readChar(br);
                }
            }

            return ret;
        }

    }

    final static char[] values = { '-', '+' };
    final static Expression[] expressions = new Expression[3];
    final static Deque<String> candidates = new ArrayDeque<>();

    private static char readChar(final BufferedReader br) throws IOException {
        for (;;) {
            final int b = br.read();
            if (b != ' ' && b != '\n') {
                return (char) b;
            }
        }
    }

    private static int readInt(final BufferedReader br) throws IOException {
        int n = 0;
        boolean beforeClear = true;
        for (;;) {
            final int b = br.read();
            if (b == ' ' || b == '\n' || b == -1) {
                if (!beforeClear) {
                    return n;
                }
                continue;
            }

            beforeClear = false;
            n = n * 10 + (b - '0');
        }
    }

    private static void initExpressions() throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            for (int i = 0; i < 3; ++i) {
                expressions[i] = Expression.from(stdin);
            }
        }
    }

    private static void findPossibleCases() {
        final int[] a = new int[12];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < 2; ++j) {
                a[i] = (values[j] == '-' ? -1 : 1);
                if (Arrays.stream(expressions).map(exp -> exp.evaluate(a)).allMatch(isTrue -> isTrue)) {
                    candidates.add(String.format("%d%c", i + 1, values[j]));
                }
            }
            a[i] = 0;
        }
    }

    public static void main(final String[] args) throws IOException {
        initExpressions();
        findPossibleCases();
        if (candidates.size() == 1) {
            System.out.println(candidates.peekFirst());
        } else {
            System.out.println(1 < candidates.size() ? "indefinite" : "impossible");
        }
    }

}
