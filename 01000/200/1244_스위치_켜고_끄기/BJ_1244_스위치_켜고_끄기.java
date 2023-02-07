// https://www.acmicpc.net/problem/1244

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class BJ_1244_스위치_켜고_끄기 {
    public static void main(final String[] args) throws IOException {
        final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        final int N = Integer.parseInt(stdin.readLine());
        final byte[] lights = new byte[N];
        {
            final StringTokenizer tokens = new StringTokenizer(stdin.readLine());
            for (int i = 0; i < N; ++i) {
                lights[i] = Byte.parseByte(tokens.nextToken());
            }
        }

        int numQueries = Integer.parseInt(stdin.readLine());
        while (0 < numQueries--) {
            final int studentType, switchValue;
            {
                final String[] tokens = stdin.readLine().split(" ");
                studentType = Integer.parseInt(tokens[0]);
                switchValue = Integer.parseInt(tokens[1]);
            }

            switch (studentType) {
                case 1:
                    for (int i = 1; switchValue * i <= N; ++i) {
                        lights[switchValue * i - 1] ^= 1;
                    }
                    break;
                case 2:
                    lights[switchValue - 1] ^= 1;
                    for (int i = 1; 0 < switchValue - i && switchValue + i <= N; ++i) {
                        if (lights[switchValue - i - 1] != lights[switchValue + i - 1]) {
                            break;
                        }

                        lights[switchValue - i - 1] ^= 1;
                        lights[switchValue + i - 1] ^= 1;
                    }

                    break;
            }
        }

        for (int i = 0; i < N; ++i) {
            if (0 < i && i % 20 == 0) {
                stdout.write("\n");
            }
            stdout.write(lights[i] + " ");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
