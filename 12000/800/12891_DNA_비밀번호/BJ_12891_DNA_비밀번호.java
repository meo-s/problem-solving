// https://www.acmicpc.net/problem/12891

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

public class BJ_12891_DNA_비밀번호 {
    private static HashMap<Character, Integer> ATCG2INT;

    static {
        ATCG2INT = new HashMap<>();
        ATCG2INT.put('A', 0);
        ATCG2INT.put('C', 1);
        ATCG2INT.put('G', 2);
        ATCG2INT.put('T', 3);
    }

    public static class DNAPasswordCounter {
        private static boolean isValid(final int[] ATCGs, final int[] requirements) {
            for (int i = 0; i < ATCGs.length; ++i) {
                if (ATCGs[i] < requirements[i]) {
                    return false;
                }
            }
            return true;
        }

        public static int count(final String DNA, final int P, final int[] requirements) {
            int numAvailables = 0;
            final int[] ATCGs = new int[4];
            for (int i = 0; i < DNA.length(); ++i) {
                ++ATCGs[ATCG2INT.get(DNA.charAt(i))];
                if (P <= i) {
                    --ATCGs[ATCG2INT.get(DNA.charAt(i - P))];
                }
                if (P - 1 <= i) {
                    numAvailables += (isValid(ATCGs, requirements) ? 1 : 0);
                }
            }

            return numAvailables;
        }
    }

    public static void main(final String[] args) throws IOException {
        final BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        final int P;
        {
            final String[] tokens = stdin.readLine().split(" ");
            P = Integer.parseInt(tokens[1]);
        }

        final String DNA = stdin.readLine();
        final int[] requirements = new int[4];
        {
            final StringTokenizer tokens = new StringTokenizer(stdin.readLine());
            for (int i = 0; i < requirements.length; ++i) {
                requirements[i] = Integer.parseInt(tokens.nextToken());
            }
        }

        stdout.write(DNAPasswordCounter.count(DNA, P, requirements) + "\n");

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
