# https://www.acmicpc.net/problem/1012

import sys

readline = lambda: sys.stdin.readline().strip()

for _ in range(int(readline())):
    M, N, K = map(int, readline().split())
    farm = [[-1] * M for _ in range(N)]
    for _ in range(K):
        x, y = map(int, readline().split())
        farm[y][x] = 0

    n_worms = 0
    for y in range(N):
        for x in range(M):
            if farm[y][x] != 0:
                continue

            n_worms += 1
            farm[y][x] = n_worms
            points = [(y, x)]
            while 0 < len(points):
                r, c = points.pop()
                for dr, dc in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
                    if (not 0 <= r + dr < N) or (not 0 <= c + dc < M):
                        continue
                    if farm[r + dr][c + dc] == 0:
                        farm[r + dr][c + dc] = n_worms
                        points.append((r + dr, c + dc))

    print(n_worms)
