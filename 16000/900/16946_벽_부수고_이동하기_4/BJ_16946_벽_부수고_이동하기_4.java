// https://www.acmicpc.net/problem/16946

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

public class BJ_16946_벽_부수고_이동하기_4 {
    private static int[] dh = { 1, -1, 0, 0 };
    private static int[] dw = { 0, 0, 1, -1 };

    public static class Point2<T> {
        public Point2(final T h_, final T w_) {
            h = h_;
            w = w_;
        }

        public T h;
        public T w;
    }

    public static void calcArea(final int[][] room, final HashMap<Integer, Integer> roomAreas, int h, int w) {
        final var H = room.length;
        final var W = room[0].length;

        final var areaIndex = roomAreas.size() + 1;
        room[h][w] = areaIndex;

        var area = 1;
        final var waypoints = new ArrayDeque<Point2<Integer>>();
        waypoints.add(new Point2<>(h, w));
        while (0 < waypoints.size()) {
            final var wp = waypoints.pollFirst();
            for (var i = 0; i < dh.length; ++i) {
                if (wp.h + dh[i] < 0 || H <= wp.h + dh[i] || wp.w + dw[i] < 0 || W <= wp.w + dw[i]) {
                    continue;
                }

                if (room[wp.h + dh[i]][wp.w + dw[i]] != 0) {
                    continue;
                }

                area += 1;
                room[wp.h + dh[i]][wp.w + dw[i]] = areaIndex;
                waypoints.addLast(new Point2<>(wp.h + dh[i], wp.w + dw[i]));
            }
        }

        roomAreas.put(areaIndex, area);
    }

    public static int breakWall(final int[][] room, final HashMap<Integer, Integer> roomAreas, int h, int w) {
        final var H = room.length;
        final var W = room[0].length;

        var n_availables = 1;
        final var visited = new HashSet<Integer>();
        for (var i = 0; i < dh.length; ++i) {
            if (h + dh[i] < 0 || H <= h + dh[i] || w + dw[i] < 0 || W <= w + dw[i]) {
                continue;
            }

            final var roomIndex = room[h + dh[i]][w + dw[i]];
            if (0 < roomIndex) {
                if (!visited.contains(roomIndex)) {
                    visited.add(roomIndex);
                    n_availables += roomAreas.get(roomIndex);
                }
            }
        }

        return n_availables;
    }

    public static void main(String[] args) throws IOException {
        final var stdout = new BufferedWriter(new OutputStreamWriter(System.out));
        final var stdin = new BufferedReader(new InputStreamReader(System.in));

        int H, W;
        {
            final var tokens = new StringTokenizer(stdin.readLine());
            H = Integer.parseInt(tokens.nextToken());
            W = Integer.parseInt(tokens.nextToken());
        }

        final var room = new int[H][W];
        for (var h = 0; h < H; ++h) {
            final var row = stdin.readLine();
            for (var w = 0; w < W; ++w) {
                room[h][w] = (row.charAt(w) == '0' ? 0 : -1);
            }
        }

        final var roomAreas = new HashMap<Integer, Integer>();
        for (var h = 0; h < H; ++h) {
            for (var w = 0; w < W; ++w) {
                if (room[h][w] == 0) {
                    calcArea(room, roomAreas, h, w);
                }
            }
        }

        final var availables = new int[H][W];
        for (var h = 0; h < H; ++h) {
            for (var w = 0; w < W; ++w) {
                if (room[h][w] == -1) {
                    availables[h][w] = breakWall(room, roomAreas, h, w);
                }
            }
        }

        for (var h = 0; h < H; ++h) {
            for (var w = 0; w < W; ++w) {
                stdout.write(availables[h][w] % 10 + "");
            }
            stdout.write("\n");
        }

        stdout.flush();
        stdout.close();
        stdin.close();
    }
}
