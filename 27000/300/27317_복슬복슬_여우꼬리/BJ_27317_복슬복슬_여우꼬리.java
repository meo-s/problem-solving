// https://www.acmicpc.net/problem/27317

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_27317_복슬복슬_여우꼬리 {
    private static int T;

    public static class Range {
        public int beg;
        public int end;
        public int usedMagicCount;
        public boolean isBushyTime;

        public Range(final int beg, final int end, final boolean isBushyTime) {
            this.beg = beg;
            this.end = end;
            this.usedMagicCount = 0;
            this.isBushyTime = isBushyTime;
        }

        public int getRequiredMagics() {
            return isBushyTime ? 0 : (end - beg) / T + (0 < (end - beg) % T ? 1 : 0);
        }

        public int getMinBushyBeginTime() {
            return isBushyTime ? beg : Math.max(beg, end - usedMagicCount * T);
        }

        public int getMaxBushyEndTime() {
            return isBushyTime ? end : Math.min(end, beg + usedMagicCount * T);
        }
    }

    public static int calcBushyTimeDuration(final Range[] ranges, final int lb, final int ub) {
        return ranges[ub - 1].getMaxBushyEndTime() - ranges[lb].getMinBushyBeginTime();
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N, M, K;
            {
                final String[] tokens = stdin.readLine().split(" ");
                N = Integer.parseInt(tokens[0]);
                M = Integer.parseInt(tokens[1]);
                K = Integer.parseInt(tokens[2]);
                T = Integer.parseInt(tokens[3]);
            }

            var latestRangEnd = 0;
            final var ranges = new Range[2 * N + 1];
            for (var i = 0; i < N; ++i) {
                final var tokens = stdin.readLine().split(" ");
                ranges[2 * i] = new Range(latestRangEnd, Integer.parseInt(tokens[0]), false);
                ranges[2 * i + 1] = new Range(ranges[2 * i].end, Integer.parseInt(tokens[1]), true);
                latestRangEnd = ranges[2 * i + 1].end;
            }
            ranges[2 * N] = new Range(latestRangEnd, M, false);

            var lb = 0;
            var ub = 1;
            var magicRemains = 0;
            var maxBushyTimeDuration = 0;
            while (ub <= 2 * N + 1) {
                var k = Math.min(ranges[ub - 1].getRequiredMagics(), K);
                for (;;) {
                    maxBushyTimeDuration = Math.max(maxBushyTimeDuration, calcBushyTimeDuration(ranges, lb, ub));
                    if (--k < 0) {
                        break;
                    }

                    ++magicRemains;
                    ++ranges[ub - 1].usedMagicCount;

                    if (K < magicRemains) {
                        while (ranges[lb].usedMagicCount == 0) {
                            ++lb;
                        }

                        --magicRemains;
                        --ranges[lb].usedMagicCount;
                        if (ranges[lb].usedMagicCount == 0) {
                            ++lb;
                        }
                    }

                }

                // 푸석푸석한 시간대를 복슬복슬 시간으로 바꾸기 위해 필요한 마법의 횟수를 KR이라고 하면,
                // K < KR인 경우, [lb, ub) 구간에 푸석푸석한 시간이 생기기 때문에 lb를 증가시켜야 한다.
                while (K < ranges[ub - 1].getRequiredMagics() && ranges[lb].usedMagicCount == 0) {
                    ++lb;
                }

                ++ub;
            }

            System.out.print(maxBushyTimeDuration + "\n");
        }
    }
}
