// https://www.acmicpc.net/problem/4347

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class BJ_4347_Tic_Tac_Toe {

    private static final int[] WINMASKS = {
            0b111000000,
            0b000111000,
            0b000000111,
            0b100100100,
            0b010010010,
            0b001001001,
            0b100010001,
            0b001010100,
    };

    private static char[] readBoard(final BufferedReader br) throws IOException {
        final char[] board = new char[9];
        for (int y = 0; y < 3; ++y) {
            final String line = br.readLine().trim();
            if (line.length() == 0) {
                --y;
                continue;
            }
            for (int x = 0; x < 3; ++x) {
                board[y * 3 + x] = line.charAt(x);
            }
        }
        return board;
    }

    private static boolean validate(final char[] board) {
        int ocount = 0;
        int obitmask = 0;
        int xcount = 0;
        int xbitmask = 0;
        for (int i = 0; i < 9; ++i) {
            switch (board[i]) {
                case 'X':
                    xbitmask |= 1 << i;
                    ++xcount;
                    break;
                case 'O':
                    obitmask |= 1 << i;
                    ++ocount;
                    break;
            }
        }

        if (xcount - ocount < 0 || 1 < xcount - ocount) {
            return false;
        }

        boolean isPlayer1Win = false;
        boolean isPlayer2Win = false;
        for (final int winmask : WINMASKS) {
            isPlayer1Win = isPlayer1Win || ((xbitmask & winmask) == winmask);
            isPlayer2Win = isPlayer2Win || ((obitmask & winmask) == winmask);
        }

        if (isPlayer1Win && isPlayer2Win) {
            return false;
        } else if (xcount - ocount == 0 && isPlayer1Win) {
            return false;
        } else if (xcount - ocount == 1 && isPlayer2Win) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(final String[] args) throws IOException {
        final StringBuilder ans = new StringBuilder();
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final int N = Integer.parseInt(stdin.readLine());
            for (int i = 0; i < N; ++i) {
                ans.append(validate(readBoard(stdin)) ? "yes\n" : "no\n");
            }
        }

        System.out.println(ans.toString());
    }

}
