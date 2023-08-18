# https://www.acmicpc.net/problem/7569

import sys
from collections import deque

readline = lambda: sys.stdin.readline().strip()

M, N, H = map(int, readline().split())

boxes = []
n_raws = 0
waitings = deque([])
for h in range(H):
    boxes.append([])
    for row in range(N):
        boxes[-1].append([])
        for col, n in enumerate(map(int, readline().split())):
            boxes[-1][-1].append(n)
            n_raws += 1 if n == 0 else 0
            waitings.append((h, row, col)) if n == 1 else None

n_days = 0
while 0 < n_raws and 0 < len(waitings):
    for _ in range(len(waitings)):
        h, row, col = waitings.popleft()
        for dh, drow, dcol in [[1, 0, 0], [-1, 0, 0], [0, 1, 0], [0, -1, 0], [0, 0, 1], [0, 0, -1]]:
            if (not 0 <= h + dh < H) or (not 0 <= row + drow < N) or (not 0 <= col + dcol < M):
                continue
            if boxes[h + dh][row + drow][col + dcol] == 0:
                boxes[h + dh][row + drow][col + dcol] = 1
                n_raws -= 1
                waitings.append((h + dh, row + drow, col + dcol))

    n_days += 1

print([-1, n_days][n_raws == 0])
