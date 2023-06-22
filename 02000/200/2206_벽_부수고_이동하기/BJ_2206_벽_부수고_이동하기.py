# https://www.acmicpc.net/problem/2206

import sys
from collections import deque

readline = lambda: sys.stdin.readline().strip()

H, W = map(int, readline().split())
miro = [[*map(int, readline())] for _ in range(H)]

dists = [[[float('inf'), float('inf')] for _ in range(W)] for _ in range(H)]
dists[0][0][0] = 1

points = deque([((0, 0), False)])
while (float('inf') in dists[-1][-1]) and 0 < len(points):
    (y, x), broken = points.popleft()
    for dy, dx in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
        if (not 0 <= y + dy < H) or (not 0 <= x + dx < W):
            continue
        if miro[y + dy][x + dx] == 1:
            if broken:
                continue
            if (dist := dists[y][x][0] + 1) < dists[y + dy][x + dx][1]:
                dists[y + dy][x + dx][1] = dist
                points.append(((y + dy, x + dx), True))
        else:
            if (dist := dists[y][x][broken] + 1) < dists[y + dy][x + dx][broken]:
                dists[y + dy][x + dx][broken] = dist
                points.append(((y + dy, x + dx), broken))

min_dist = min(dists[-1][-1])
print([-1, min_dist][min_dist != float('inf')])
