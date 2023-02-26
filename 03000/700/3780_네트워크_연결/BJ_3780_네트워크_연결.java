// https://www.acmicpc.net/problem/3780

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_3780_네트워크_연결 {
    public static class Network {
        public static class EndPoint {
            private int center;
            private int cachedCenter;
            private int distToCachedCenter;

            public EndPoint(final int index) {
                this.center = index;
                this.cachedCenter = index;
                this.distToCachedCenter = 0;
            }
        }

        private EndPoint[] endpoints;

        public Network(final int N) {
            endpoints = new EndPoint[N + 1];
            for (var i = 0; i <= N; ++i) {
                endpoints[i] = new EndPoint(i);
            }
        }

        public int find(final int u) {
            if (endpoints[u].center != u) {
                endpoints[u].center = find(endpoints[u].center);
            }
            return endpoints[u].center;
        }

        public void union(final int v, final int u) {
            final int vp = find(v);
            final int up = find(u);
            if (vp != up) {
                endpoints[v].distToCachedCenter += Math.abs(u - v) % 1000;
                endpoints[v].cachedCenter = u;
                endpoints[vp].center = up;
            }
        }

        public int distanceToCenterFrom(final int u) {
            final var currentCenter = find(u);
            if (endpoints[u].cachedCenter != currentCenter) {
                final var oldCenter = endpoints[u].cachedCenter;
                endpoints[u].cachedCenter = currentCenter;
                endpoints[u].distToCachedCenter += distanceToCenterFrom(oldCenter);
            }
            return endpoints[u].distToCachedCenter;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            var T = Integer.parseInt(stdin.readLine());
            while (0 <= --T) {
                final var N = Integer.parseInt(stdin.readLine());
                final var network = new Network(N);

                TESTCASE_LOOP: for (;;) {
                    final var tokens = stdin.readLine().split(" ");
                    switch (tokens[0].charAt(0)) {
                        case 'I': {
                            final var v = Integer.parseInt(tokens[1]);
                            final var u = Integer.parseInt(tokens[2]);
                            network.union(v, u);
                            break;
                        }
                        case 'E': {
                            final var u = Integer.parseInt(tokens[1]);
                            stdout.write(network.distanceToCenterFrom(u) + "\n");
                            break;
                        }
                        case 'O':
                            break TESTCASE_LOOP;
                    }
                }
            }

            stdout.flush();
        }
    }
}
