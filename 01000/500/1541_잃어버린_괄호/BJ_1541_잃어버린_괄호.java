// https://www.acmicpc.net/problem/1541

import java.io.IOException;

public class BJ_1541_잃어버린_괄호 {
    public static void main(final String[] args) throws IOException {
        final String exp = new String(System.in.readAllBytes()).trim();
        final String[] tokens = exp.split("[+-]");

        int w = 1;
        int expOffset = tokens[0].length();
        long expValue = Integer.parseInt(tokens[0]);
        for (int i = 1; i < tokens.length; ++i) {
            w = (w < 0 || exp.charAt(expOffset) == '-' ? -1 : 1);
            expValue += w * Integer.parseInt(tokens[i]);
            expOffset += 1 + tokens[i].length();
        }

        System.out.println(expValue);
    }
}
