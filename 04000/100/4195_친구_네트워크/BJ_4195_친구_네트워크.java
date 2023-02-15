// https://www.acmicpc.net/problem/4195

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BJ_4195_친구_네트워크 {
    public static class UnionFind {
        private List<Integer> parents = new ArrayList<>();
        private List<Integer> setSizes = new ArrayList<>();

        public int find(final int u) {
            while (parents.size() <= u) {
                parents.add(parents.size());
                setSizes.add(1);
            }
            if (parents.get(u) != u) {
                parents.set(u, find(parents.get(u)));
            }
            return parents.get(u);
        }

        public void union(final int u, final int v) {
            final int up = find(u);
            final int vp = find(v);
            if (up != vp) {
                parents.set(up, vp);
                setSizes.set(vp, setSizes.get(up) + setSizes.get(vp));
            }
        }

        public int sizeOf(final int u) {
            return setSizes.get(find(u));
        }
    }

    public static class Diary {
        private Map<String, Integer> people = new HashMap<>();

        int get(final String person) {
            people.putIfAbsent(person, people.size());
            return people.get(person);
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {

            var t = Integer.parseInt(stdin.readLine());
            while (0 < t--) {
                final var uf = new UnionFind();
                final var diary = new Diary();

                var numRelations = Integer.parseInt(stdin.readLine());
                while (0 < numRelations--) {
                    final int u, v;
                    {
                        final var tokens = stdin.readLine().split(" ");
                        u = diary.get(tokens[0]);
                        v = diary.get(tokens[1]);
                    }

                    uf.union(u, v);
                    stdout.write(uf.sizeOf(u) + "\n");
                }
            }

            stdout.flush();
        }
    }
}
