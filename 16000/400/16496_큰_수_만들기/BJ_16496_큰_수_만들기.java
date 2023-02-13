// https://www.acmicpc.net/problem/16496

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;

public class BJ_16496_큰_수_만들기 {

    public static void main(final String args[]) throws IOException {
        try (final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
                final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            stdin.readLine();
            final var nums = stdin.readLine().split(" ");

            Arrays.sort(nums, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    final var a = Long.parseLong(lhs);
                    final var b = Long.parseLong(rhs);
                    final var ab = a * (long) Math.pow(10, (int) Math.max(0, Math.log10(b)) + 1) + b;
                    final var ba = b * (long) Math.pow(10, (int) Math.max(0, Math.log10(a)) + 1) + a;

                    return ab - ba != 0 ? (int) ((ab - ba) / Math.abs(ab - ba)) : 0;
                }
            }.reversed());

            if (nums[0].charAt(0) == '0') {
                stdout.write("0\n");
            } else {
                for (final var num : nums) {
                    stdout.write(num);
                }
                stdout.write("\n");
            }

            stdout.flush();
        }
    }
}
