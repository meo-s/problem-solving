// https://www.acmicpc.net/problem/18185

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;

public class BJ_18185_라면_사기_Small {
    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var N = Integer.parseInt(stdin.readLine());
            final var noddles = new int[N];
            {
                final var tokens = stdin.readLine().split(" ");
                for (var i = 0; i < N; ++i) {
                    noddles[i] = Integer.parseInt(tokens[i]);
                }
            }

            final var bundles = new ArrayDeque<Integer>();
            var numBundleRemains = 0;
            var cost = 0;
            for (var i = 0; i < N; ++i) {
                final var numBundleOrders = Math.min(numBundleRemains, noddles[i]);
                final var numNewOrders = Math.max(noddles[i] - numBundleOrders, 0);
                cost += numNewOrders * 3 + numBundleOrders * 2;

                final var numNewBundleOrders = Math.min((i < N - 1 ? noddles[i + 1] : 0), numNewOrders);
                bundles.addLast(numNewBundleOrders);

                numBundleRemains += numNewBundleOrders - (2 <= i ? bundles.pollFirst() : 0);
            }

            System.out.print(cost + "\n");
        }
    }
}
