import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class BJ_11057_오르막수 {
    private static final int M = 10_007;

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var dp = new int[N][10];
        Arrays.fill(dp[0], 1);
        for (int i = 1; i < N; ++i) {
            for (int j = 0; j < 10; ++j) {
                for (int k = 0; k <= j; ++k) {
                    dp[i][j] = (dp[i][j] + dp[i - 1][k]) % M;
                }
            }
        }

        stdout.write(Arrays.stream(dp[N - 1]).sum() % M + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
