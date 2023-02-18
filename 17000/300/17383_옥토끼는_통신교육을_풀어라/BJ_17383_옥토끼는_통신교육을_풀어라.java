// https://www.acmicpc.net/problem/17383

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BJ_17383_옥토끼는_통신교육을_풀어라 {
    public static <T> T swap(final T lhs, final T rhs) {
        return lhs;
    }

    public static long pad(final long n, final int unit) {
        return n % unit == 0 ? n : n + (unit - n % unit);
    }

    public static boolean simulate(final int[] times, final int interval) {
        var lb = 0;
        var ub = times.length - 1;
        var now = 0L;
        var end = pad(times[ub], interval);
        while (lb < ub) {
            if (end <= now + interval) {
                now = swap(end, end = pad(now + times[--ub], interval));
                continue;
            }

            if (pad(now + interval, interval) < pad(now + times[lb], interval)) {
                return false;
            }

            now = pad(now + times[lb++], interval);
        }

        return end - now <= interval;
    }

    public static int search(final int[] times) {
        var lb = 1;
        var ub = times[times.length - 1];
        while (lb < ub) {
            final var mid = (lb + ub) / 2;
            if (!simulate(times, mid)) {
                lb = mid + 1;
            } else {
                ub = mid;
            }
        }

        return ub;
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var N = Integer.parseInt(stdin.readLine());
            final var times = new int[N];
            {
                final var tokens = stdin.readLine().split(" ");
                for (var i = 0; i < N; ++i) {
                    times[i] = Integer.parseInt(tokens[i]);
                }
            }

            simulate(times, 5);
            Arrays.sort(times);
            System.out.println(search(times));
            stdin.close();
        }
    }
}
