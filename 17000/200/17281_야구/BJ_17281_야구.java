// https://www.acmicpc.net/problem/17281

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BJ_17281_야구 {
    private static final int NUM_ATHLETES = 9;
    private static final int[] ORDERS;

    static {
        ORDERS = new int[NUM_ATHLETES - 1];
        for (int i = 0; i < ORDERS.length; ++i) {
            ORDERS[i] = i + (2 < i ? 1 : 0);
        }
    }

    public static <T> T swap(final T lhs, final T rhs) {
        return lhs;
    }

    public static class NextPermutation {
        private boolean hasNext;
        private final int[] perm;

        public NextPermutation(final int N) {
            hasNext = true;
            perm = new int[N];
            for (int i = 0; i < N; ++i) {
                perm[i] = i;
            }
        }

        public boolean hasNext() {
            return hasNext;
        }

        public boolean next() {
            int pivot = perm.length - 2;
            while (0 <= pivot && perm[pivot] > perm[pivot + 1]) {
                --pivot;
            }

            if (0 <= pivot) {
                int i = perm.length - 1;
                while (pivot < i && perm[i] < perm[pivot]) {
                    --i;
                }

                perm[i] = swap(perm[pivot], perm[pivot] = perm[i]);
                for (int j = 1; pivot + j < perm.length - j; ++j) {
                    perm[perm.length - j] = swap(perm[pivot + j], perm[pivot + j] = perm[perm.length - j]);
                }
            }

            return hasNext = 0 <= pivot;
        }

        public int[] get() {
            return perm;
        }
    }

    public static class BaseballSimulator {
        public static class SimulationState {
            public int score;
            public int[] bases;

            public SimulationState() {
                score = 0;
                bases = new int[3];
            }
        }

        private int numInnings;
        private int[][] results;

        private BaseballSimulator(final int numInnings) {
            this.numInnings = numInnings;
            this.results = new int[numInnings][NUM_ATHLETES];
        }

        public int getNumInnings() {
            return numInnings;
        }

        public int simulate(final int[] orders) {
            int score = 0;
            int orderIndex = 0;
            final boolean[] bases = new boolean[3];
            for (int inning = 0; inning < numInnings; ++inning) {
                Arrays.fill(bases, false);

                int numOut = 0;
                while (numOut < 3) {
                    final int athleteIndex = orders[orderIndex];
                    orderIndex = (orderIndex + 1) % orders.length;

                    if (results[inning][athleteIndex] == 0) {
                        ++numOut;
                        continue;
                    }

                    if (results[inning][athleteIndex] == 4) {
                        ++score;
                        for (int i = 0; i < bases.length; ++i) {
                            if (bases[i]) {
                                bases[i] = false;
                                ++score;
                            }
                        }
                    } else {
                        for (int i = 3; 1 <= i; --i) {
                            if (bases[i - 1]) {
                                bases[i - 1] = false;
                                if (4 <= i + results[inning][athleteIndex]) {
                                    ++score;
                                } else {
                                    bases[(i - 1) + results[inning][athleteIndex]] = true;
                                }
                            }
                        }

                        bases[results[inning][athleteIndex] - 1] = true;
                    }

                }
            }

            return score;
        }

        public static BaseballSimulator from(final BufferedReader br) throws IOException {
            final int numInnings = Integer.parseInt(br.readLine());

            final BaseballSimulator simulator = new BaseballSimulator(numInnings);

            for (int i = 0; i < numInnings; ++i) {
                final String[] tokens = br.readLine().split(" ");
                for (int j = 0; j < NUM_ATHLETES; ++j) {
                    simulator.results[i][j] = Integer.parseInt(tokens[j]);
                }
            }

            return simulator;
        }

    }

    public static void main(final String[] args) throws IOException {
        try (final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            int maxScore = 0;
            final BaseballSimulator simulator = BaseballSimulator.from(stdin);
            final NextPermutation perm = new NextPermutation(NUM_ATHLETES - 1);
            final int[] orders = new int[NUM_ATHLETES];
            orders[3] = 0;

            while (perm.hasNext()) {
                for (int i = 1; i < NUM_ATHLETES; ++i) {
                    orders[ORDERS[perm.get()[i - 1]]] = i;
                }

                maxScore = Math.max(maxScore, simulator.simulate(orders));
                perm.next();
            }

            System.out.print(maxScore + "\n");
        }
    }
}
