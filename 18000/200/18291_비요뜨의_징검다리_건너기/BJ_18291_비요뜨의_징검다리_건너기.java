import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BJ_18291_비요뜨의_징검다리_건너기 {
    public static int M = 1_000_000_007;

    public static long pow(long n, final int k) {
        n %= M;
        switch (k) {
            case 0:
                return 1;
            case 1:
                return n;
            case 2:
                return (n * n) % M;
            default:
                long nxn = pow(pow(n, k / 2), 2);
                return (k % 2 == 1) ? (nxn * n) % M : nxn;
        }
    }

    public static void main(String[] args) throws IOException {
        final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        int T = Integer.parseInt(stdin.readLine());
        while (0 < T--) {
            final int N = Integer.parseInt(stdin.readLine());
            stdout.write(pow(2, Math.max(N - 2, 0)) + "\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
