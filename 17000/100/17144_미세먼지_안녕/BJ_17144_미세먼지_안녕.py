# https://www.acmicpc.net/problem/17144

import sys

readline = lambda: sys.stdin.readline().strip()

H, W, T = map(int, readline().split())

room = [[0] * W for _ in range(H)]
purifier = None
for y in range(H):
    for x, v in enumerate(map(int, readline().split())):
        room[y][x] = v
        purifier = (y, x) if v == -1 else purifier

for _ in range(T):
    dusts = []
    for y in range(H):
        for x in range(W):
            if 5 <= room[y][x]:
                dusts.append((y, x, room[y][x] // 5))

    for y, x, density in dusts:
        for dy, dx in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
            if (not 0 <= y + dy < H) or (not 0 <= x + dx < W):
                continue
            if room[y + dy][x + dx] < 0:
                continue
            room[y][x] -= density
            room[y + dy][x + dx] += density

    py, px = purifier
    for y, x, y_lb, y_ub, dydx in [(py - 1, px, 0, py, [(-1, 0), (0, 1), (1, 0), (0, -1)]), \
                                   (py, px, py, H, [(1, 0), (0, 1), (-1, 0), (0, -1)])]:
        wy, wx = y, x
        for dy, dx in dydx:
            while (y_lb <= wy + dy < y_ub) and (0 <= wx + dx < W):
                wy, wx = wy + dy, wx + dx
                if room[wy - dy][wx - dx] != -1:
                    room[wy - dy][wx - dx] = room[wy][wx] if 0 <= room[wy][wx] else 0

py, px = purifier
room[py][px] = room[py - 1][px] = 0
print(sum(sum(room, start=[])))
