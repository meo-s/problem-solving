# https://www.acmicpc.net/problem/27311

import sys


def readline():
    return sys.stdin.readline().rstrip()


def find_heart(latte):
    def _find(latte, v):
        H = len(latte)
        W = len(latte[0])

        x_lt = W
        y_lt = H
        x_rb = 0
        y_rb = 0
        for y in range(H):
            for x in range(W):
                y_lt = min(y_lt, y) if latte[y][x] == v else y_lt
                x_lt = min(x_lt, x) if latte[y][x] == v else x_lt
                y_rb = max(y_rb, y + 1) if latte[y][x] == v else y_rb
                x_rb = max(x_rb, x + 1) if latte[y][x] == v else x_rb

        has_heart = False
        if y_lt < y_rb and x_lt < x_rb:
            H = y_rb - y_lt
            W = x_rb - x_lt
            latte = latte[y_lt:y_rb]
            latte = [latte_line[x_lt:x_rb] for latte_line in latte]

            if latte[0][-1] == abs(v - 1) or latte[-1][-1] == abs(v - 1):
                latte = [latte_line[::-1] for latte_line in latte]
            if latte[-1][0] == abs(v - 1):
                latte = latte[::-1]

            M = -1
            while M < W - 1 and latte[0][(M := M + 1)] == abs(v - 1):
                pass

            has_heart = H == W and 0 < M
            for y in range(H):
                if not has_heart:
                    break

                for x in range(W):
                    if latte[y][x] != [v, abs(v - 1)][y < M and x < M]:
                        has_heart = False
                        break

        return has_heart

    return _find([*latte], 1)


for _ in range(int(readline())):
    H, W = map(int, readline().split())
    latte = [[0] * W for _ in range(H)]
    for h in range(H):
        for w, c in enumerate(readline()):
            latte[h][w] = int(c == '#')

    print(int(find_heart(latte)))
