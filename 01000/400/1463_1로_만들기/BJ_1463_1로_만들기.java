import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.stream.IntStream;

public class BJ_1463_1로_만들기 {
    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var dp = new int[N + 1];

        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[1] = 0;
        for (var i = 1; i <= N; ++i) {
            final var dist = dp[i];
            IntStream.of(i + 1, i * 2, i * 3).forEach(j -> {
                if (j <= N) {
                    dp[j] = Math.min(dp[j], dist + 1);
                }
            });
        }

        stdout.write(dp[N] + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
