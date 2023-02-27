// https://www.acmicpc.net/problem/14865

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class BJ_14865_곡선_자르기 {
    public static class Point {
        public long y;
        public long x;

        public Point(final long y, final long x) {
            this.y = y;
            this.x = x;
        }
    }

    public static class Range implements Comparable<Range> {
        public long beg;
        public long end;

        public Range(final long beg, final long end) {
            this.beg = beg;
            this.end = end;
        }

        @Override
        public int compareTo(final Range other) {
            if (beg == other.beg) {
                return Long.compare(end, other.end);
            }
            return Long.compare(beg, other.beg);
        }

        @Override
        public String toString() {
            return String.format("[%d,%d]", beg, end);
        }
    }

    public static void main(String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N = Integer.parseInt(stdin.readLine());
            final Point[] pts = new Point[N];
            for (int i = 0; i < N; ++i) {
                final String[] tokens = stdin.readLine().split(" ");
                pts[i] = new Point(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0]));
            }

            int pivot = -1;
            for (int i = 0; i < N; ++i) {
                if (pts[i].y < 0) {
                    if ((pivot < 0) || (pts[i].x == pts[pivot].x && pts[i].y < pts[pivot].y)
                            || (pts[i].x < pts[pivot].x)) {
                        pivot = i;
                    }
                }
            }

            final List<Range> ranges = new ArrayList<>();
            final Deque<Long> log = new ArrayDeque<>();
            for (int i = 1; i < N; ++i) {
                final int cp = (pivot + i) % N;
                final int pp = (pivot + (i - 1)) % N;

                if (pts[cp].y * pts[pp].y < 0) {
                    if (log.size() == 0) {
                        log.addLast(pts[cp].x);
                    } else {
                        final long x1 = pts[cp].x;
                        final long x2 = log.pollFirst();
                        ranges.add(new Range(Math.min(x1, x2), Math.max(x1, x2)));
                    }
                }
            }

            Collections.sort(ranges);

            int numRoots = 0;
            int numLeafs = 0;

            final Deque<Range> st = new ArrayDeque<>();
            for (final Range now : ranges) {
                if (0 < st.size() && st.peekLast().end < now.beg) {
                    ++numLeafs;

                    while (0 < st.size() && st.peekLast().end < now.beg) {
                        st.pollLast();
                    }
                }

                st.addLast(now);
                numRoots += (st.size() == 1 ? 1 : 0);
            }

            if (0 < st.size()) {
                ++numLeafs;
            }

            System.out.printf("%d %d\n", numRoots, numLeafs);
        }
    }
}
