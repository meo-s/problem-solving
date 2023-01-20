import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_11727_2xn_타일링_2 {
    private static final int M = 10007;

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        final var N = Integer.parseInt(stdin.readLine());
        final var dp = new int[N + 1];
        dp[0] = dp[1] = 1;
        for (var i = 2; i <= N; ++i) {
            dp[i] = (dp[i - 1] + 2 * dp[i - 2]) % M;
        }

        stdout.write(dp[N] + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
