// https://www.acmicpc.net/problem/2240

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_2240_자두나무 {

    public static class PlumInfo {
        public PlumInfo(final int at, final int amount) {
            this.at = at;
            this.amount = amount;
        }

        public int at;
        public int amount;
    }

    public static class PlumCounter {
        private PlumCounter(final int N, final int W) {
            dp = new int[2][N][W + 1];
        }

        private int count(final List<PlumInfo> plums, final int offset, final int tree, final int W) {
            if (W < 0 || plums.size() <= offset) {
                return 0;
            }

            if (dp[tree][offset][W] == 0) {
                dp[tree][offset][W] = 1 + (plums.get(offset).at == tree ? plums.get(offset).amount : 0)
                        + Math.max(count(plums, offset + 1, tree, W), count(plums, offset + 1, tree ^ 1, W - 1));
            }

            return dp[tree][offset][W] - 1;
        }

        public static int count(final List<PlumInfo> plums, final int W) {
            final var counter = new PlumCounter(plums.size(), W);
            return Math.max(counter.count(plums, 0, 0, W), counter.count(plums, 0, 1, W - 1));
        }

        private final int[][][] dp;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N, W;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            N = Integer.parseInt(tokens.nextToken());
            W = Integer.parseInt(tokens.nextToken());
        }

        final var trees = new int[N];
        for (var i = 0; i < N; ++i) {
            trees[i] = Integer.parseInt(stdin.readLine()) - 1;
        }

        final var plums = new ArrayList<PlumInfo>();
        var i = 0;
        while (i < N) {
            var amount = 1;
            while (i + amount < N && trees[i + amount] == trees[i])
                ++amount;

            plums.add(new PlumInfo(trees[i], amount));
            i += amount;
        }

        stdout.write(PlumCounter.count(plums, W) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
