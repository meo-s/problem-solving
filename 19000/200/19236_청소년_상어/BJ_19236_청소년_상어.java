// https://www.acmicpc.net/problem/19236

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_19236_청소년_상어 {

    private static final int FISHING_PORT_SIZE = 4;

    public static class Fish {
        public static int getY(final int state) {
            return (state & 0b0001111) / FISHING_PORT_SIZE;
        }

        public static int getX(final int state) {
            return (state & 0b0001111) % FISHING_PORT_SIZE;
        }

        public static int getDirection(final int state) {
            return (state & 0b1110000) >> FISHING_PORT_SIZE;
        }

        public static byte make(final int y, final int x, final int direction) {
            return (byte) ((direction << FISHING_PORT_SIZE) | (y * FISHING_PORT_SIZE + x));
        }
    }

    public static class FishingPort {
        private static final int FISH_INDEX_BITS = 6;
        private static final int EMPTY = 0b100000;

        private final int[] map;
        private final byte[] fishes;

        private FishingPort(final int size) {
            map = new int[size];
            fishes = new byte[size * size + 1];
        }

        public FishingPort clone() {
            final var clone = new FishingPort(FISHING_PORT_SIZE);
            System.arraycopy(map, 0, clone.map, 0, map.length);
            System.arraycopy(fishes, 0, clone.fishes, 0, fishes.length);
            return clone;
        }

        public int getFishAt(final int y, final int x) {
            return (map[y] >> (FISH_INDEX_BITS * x)) & 0b111111;
        }

        public int popFish(final int y, final int x) {
            final int fish = getFishAt(y, x);
            map[y] = (map[y] & ~(0b111111 << (FISH_INDEX_BITS * x))) | (EMPTY << (FISH_INDEX_BITS * x));
            return fish;
        }

        public void putFish(final int y, final int x, final int fishIndex) {
            map[y] = (map[y] & ~(0b111111 << (FISH_INDEX_BITS * x))) | (fishIndex << FISH_INDEX_BITS * x);
        }

        public static FishingPort from(final BufferedReader br) throws IOException {
            final var ret = new FishingPort(FISHING_PORT_SIZE);
            for (var y = 0; y < FISHING_PORT_SIZE; ++y) {
                final var tokens = br.readLine().split(" ");
                for (var x = 0; x < FISHING_PORT_SIZE; ++x) {
                    final var fishIndex = Byte.parseByte(tokens[x * 2]);
                    final var fishDirection = Byte.parseByte(tokens[x * 2 + 1]) - 1;
                    ret.putFish(y, x, fishIndex);
                    ret.fishes[fishIndex] = Fish.make(y, x, fishDirection);
                }
            }

            return ret;
        }
    }

    public static class FishingPortSimulator {
        private static final int dy[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
        private static final int dx[] = { 0, -1, -1, -1, 0, 1, 1, 1 };

        public static int init(final FishingPort fishingPort) {
            final int preyIndex = fishingPort.popFish(0, 0);
            fishingPort.putFish(0, 0, 0);
            fishingPort.fishes[0] = fishingPort.fishes[preyIndex];
            fishingPort.fishes[preyIndex] = -1;
            return preyIndex;
        }

        public static void updateFishs(final FishingPort fishingPort) {
            for (int fish = 1; fish < fishingPort.fishes.length; ++fish) {
                if (fishingPort.fishes[fish] < 0) {
                    continue;
                }

                final var y = Fish.getY(fishingPort.fishes[fish]);
                final var x = Fish.getX(fishingPort.fishes[fish]);
                final var dir = Fish.getDirection(fishingPort.fishes[fish]);
                for (var i = 0; i < dy.length; ++i) {
                    final var newDir = (dir + i) % dy.length;
                    final var ny = y + dy[newDir];
                    final var nx = x + dx[newDir];
                    if (ny < 0 || FISHING_PORT_SIZE <= ny || nx < 0 || FISHING_PORT_SIZE <= nx) {
                        continue;
                    }

                    final var other = fishingPort.getFishAt(ny, nx);
                    if (other == 0) {
                        continue;
                    }

                    fishingPort.fishes[fish] = Fish.make(ny, nx, newDir);
                    fishingPort.putFish(ny, nx, fishingPort.popFish(y, x));

                    if (other != FishingPort.EMPTY) {

                        if (Fish.getX(fishingPort.fishes[other]) != nx) {
                            System.out.println("fuck");
                        }
                        if (Fish.getY(fishingPort.fishes[other]) != ny) {
                            System.out.println("fuck");
                        }

                        fishingPort.fishes[other] = Fish.make(y, x, Fish.getDirection(fishingPort.fishes[other]));
                        fishingPort.putFish(y, x, other);
                    }

                    break;
                }
            }
        }

        public static int simulate(FishingPort fishingPort) {
            updateFishs(fishingPort);

            final var y = Fish.getY(fishingPort.fishes[0]);
            final var x = Fish.getX(fishingPort.fishes[0]);

            final var dir = Fish.getDirection(fishingPort.fishes[0]);
            var ny = y;
            var nx = x;
            var maxScore = 0;
            for (;;) {
                ny += dy[dir];
                nx += dx[dir];
                if (ny < 0 || FISHING_PORT_SIZE <= ny || nx < 0 || FISHING_PORT_SIZE <= nx) {
                    break;
                }

                final var prey = fishingPort.getFishAt(ny, nx);
                if (prey != FishingPort.EMPTY) {
                    final var newFishingPort = fishingPort.clone();
                    newFishingPort.fishes[0] = Fish.make(ny, nx, Fish.getDirection(newFishingPort.fishes[prey]));
                    newFishingPort.fishes[prey] = -1;
                    newFishingPort.putFish(ny, nx, newFishingPort.popFish(y, x));
                    maxScore = Math.max(maxScore, simulate(newFishingPort) + prey);
                }
            }

            return maxScore;
        }

        public static int getMaxScore(FishingPort fishingPort) {
            fishingPort = fishingPort.clone();
            final var initialScore = FishingPortSimulator.init(fishingPort);
            return simulate(fishingPort) + initialScore;
        }
    }

    public static void main(final String[] args) throws IOException {
        try (final var stdin = new BufferedReader(new InputStreamReader(System.in))) {
            final var fishingPort = FishingPort.from(stdin);
            System.out.println(FishingPortSimulator.getMaxScore(fishingPort) + "\n");
        }
    }
}
