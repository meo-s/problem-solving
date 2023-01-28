// https://www.acmicpc.net/problem/4495

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class BJ_4495_녹색_옷_입은_애가_젤다지 {
    static final int[] dy = { 1, -1, 0, 0 };
    static final int[] dx = { 0, 0, 1, -1 };

    public static class VisitState implements Comparable<VisitState> {
        public VisitState(final int dist, final int y, final int x) {
            this.dist = dist;
            this.y = y;
            this.x = x;
        }

        @Override
        public int compareTo(final VisitState rhs) {
            return dist <= rhs.dist ? -1 : 1;
        }

        public final int dist;
        public final int y;
        public final int x;
    }

    public static void main(final String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        var t = 0;
        for (int N; 0 < (N = Integer.parseInt(stdin.readLine()));) {
            final var costs = new int[N][N];
            for (var y = 0; y < N; ++y) {
                final var tokens = new StringTokenizer(stdin.readLine());
                for (var x = 0; x < N; ++x) {
                    costs[y][x] = Integer.parseInt(tokens.nextToken());
                }
            }

            final var dists = new int[N][N];
            for (final var distRow : dists) {
                Arrays.fill(distRow, Integer.MAX_VALUE);
            }

            dists[0][0] = costs[0][0];
            final var pendings = new PriorityQueue<VisitState>();
            pendings.add(new VisitState(dists[0][0], 0, 0));

            while (0 < pendings.size()) {
                final var visitState = pendings.poll();
                final var y = visitState.y;
                final var x = visitState.x;
                if (dists[y][x] < visitState.dist) {
                    continue;
                }

                for (var i = 0; i < dy.length; ++i) {
                    if (y + dy[i] < 0 || N <= y + dy[i] || x + dx[i] < 0 || N <= x + dx[i]) {
                        continue;
                    }

                    if (visitState.dist + costs[y + dy[i]][x + dx[i]] < dists[y + dy[i]][x + dx[i]]) {
                        dists[y + dy[i]][x + dx[i]] = visitState.dist + costs[y + dy[i]][x + dx[i]];
                        pendings.add(new VisitState(dists[y + dy[i]][x + dx[i]], y + dy[i], x + dx[i]));
                    }
                }
            }

            stdout.write(String.format("Problem %d: %d\n", ++t, dists[N - 1][N - 1]));
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
