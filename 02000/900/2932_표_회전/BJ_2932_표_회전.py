# https://www.acmicpc.net/problem/2932

import sys

readline = lambda: sys.stdin.readline().rstrip()  #noqa

N, K = map(int, readline().split())
movements = []
for _ in range(K):
    n, gy, gx = map(int, readline().split())
    gy, gx = gy - 1, gx - 1

    y, x = (n - 1) // N, (n - 1) % N
    for sy, mx, sx, my in movements:
        x = (x + mx) % N if sy == y else x
        y = (y + my) % N if sx == x else y

    mx, my = (gx - x + N) % N, (gy - y + N) % N
    movements.append((y, mx, gx, my))
    print(mx + my)
