// https://www.acmicpc.net/problem/20056

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BJ_20056_마법사_상어와_파이어볼 {

    private static final int dy[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
    private static final int dx[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

    public static class Fireball {
        public int y;
        public int x;
        public int m;
        public int d;
        public int s;

        public Fireball(final int y, final int x, final int m, final int s, final int d) {
            this.y = y;
            this.x = x;
            this.m = m;
            this.s = s;
            this.d = d;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            final int N, M, K;
            {
                final var tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                M = Integer.parseInt(tokens[1]);
                K = Integer.parseInt(tokens[2]);
            }

            final List<List<Set<Fireball>>> map = new ArrayList<>();
            for (var y = 0; y < N; ++y) {
                map.add(new ArrayList<>());
                for (var x = 0; x < N; ++x) {
                    map.get(y).add(new HashSet<>());
                }
            }

            final var globalFireballs = new HashSet<Fireball>();
            for (var i = 0; i < M; ++i) {
                final var tokens = stdin.readLine().split(" ");
                final var y = Integer.parseInt(tokens[0]) - 1;
                final var x = Integer.parseInt(tokens[1]) - 1;
                final var m = Integer.parseInt(tokens[2]);
                final var s = Integer.parseInt(tokens[3]);
                final var d = Integer.parseInt(tokens[4]);
                final var fireball = new Fireball(y, x, m, s, d);
                map.get(y).get(x).add(fireball);
                globalFireballs.add(fireball);
            }

            for (int k = 0; k < K; ++k) {

                // 파이어볼 이동
                for (final var fireball : globalFireballs) {
                    final var ny = (N + fireball.y + (fireball.s % N) * dy[fireball.d]) % N;
                    final var nx = (N + fireball.x + (fireball.s % N) * dx[fireball.d]) % N;
                    map.get(fireball.y).get(fireball.x).remove(fireball);
                    map.get(ny).get(nx).add(fireball);
                    fireball.y = ny;
                    fireball.x = nx;
                }

                // 파이어볼 병합
                for (var y = 0; y < N; ++y) {
                    for (var x = 0; x < N; ++x) {
                        final var localFireballs = map.get(y).get(x);

                        final var numLocalFireballs = localFireballs.size();
                        if (1 < numLocalFireballs) {
                            final var remains = localFireballs.stream().mapToInt(e -> e.d % 2).sum();
                            final var newMass = localFireballs.stream().mapToInt(e -> e.m).sum() / 5;
                            final var newSpeed = localFireballs.stream().mapToInt(e -> e.s).sum() / numLocalFireballs;

                            for (final var fireball : localFireballs) {
                                globalFireballs.remove(fireball);
                            }
                            localFireballs.clear();

                            if (0 < newMass) {
                                final var dd = (remains == 0 || remains == numLocalFireballs ? 0 : 1);
                                for (var newDir = 0; newDir < 8; newDir += 2) {
                                    final var newFireball = new Fireball(y, x, newMass, newSpeed, newDir + dd);
                                    localFireballs.add(newFireball);
                                    globalFireballs.add(newFireball);
                                }
                            }
                        }
                    }
                }
            }

            stdout.write(globalFireballs.stream().mapToLong(ball -> ball.m).sum() + "\n");
            stdout.flush();
        }
    }
}
