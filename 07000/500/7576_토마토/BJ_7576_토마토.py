# https://www.acmicpc.net/problem/7576

import sys
from collections import deque

readline = lambda: sys.stdin.readline().strip()

M, N = map(int, readline().split())

box = []
n_raws = 0
waitings = deque([])
for row in range(N):
    box.append([])
    for col, n in enumerate(map(int, readline().split())):
        box[-1].append(n)
        n_raws += 1 if n == 0 else 0
        waitings.append((row, col)) if n == 1 else None

n_days = 0
while 0 < n_raws and 0 < len(waitings):
    for _ in range(len(waitings)):
        row, col = waitings.popleft()
        for drow, dcol in [[1, 0], [-1, 0], [0, 1], [0, -1]]:
            if (not 0 <= row + drow < N) or (not 0 <= col + dcol < M):
                continue
            if box[row + drow][col + dcol] == 0:
                box[row + drow][col + dcol] = 1
                n_raws -= 1
                waitings.append((row + drow, col + dcol))

    n_days += 1

print([-1, n_days][n_raws == 0])
