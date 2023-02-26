// https://www.acmicpc.net/problem/10775

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.IntStream;

public class BJ_10775_공항 {
    public static class GateManager {
        private int[] gates;

        public GateManager(final int N) {
            gates = IntStream.range(0, N + 1).toArray();
        }

        public int findEmptyGate(final int g) {
            if (gates[g] != g) {
                gates[g] = findEmptyGate(gates[g]);
            }
            return gates[g];
        }

        public boolean tryToDock(final int g) {
            if (findEmptyGate(g) == 0) {
                return false;
            }

            --gates[findEmptyGate(g)];
            return true;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var G = Integer.parseInt(stdin.readLine());
            final var P = Integer.parseInt(stdin.readLine());

            var numDockings = 0;
            final var gateManager = new GateManager(G);
            for (int i = 0; i < P; ++i) {
                final var g = Integer.parseInt(stdin.readLine());
                if (!gateManager.tryToDock(g)) {
                    break;
                }

                ++numDockings;
            }

            System.out.print(numDockings + "\n");
        }
    }
}
