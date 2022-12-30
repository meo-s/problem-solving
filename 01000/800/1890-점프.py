# https://www.acmicpc.net/problem/1890

import sys

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
board = [[*map(int, readline().split())] for _ in range(N)]

jumps = [[0] * N for _ in range(N)]
jumps[0][0] = 1

for i in range(N * 2):
    for j in range(N):
        if not 0 <= i - j < N:
            continue
        if board[i - j][j] == 0:
            continue
        for dy, dx in [(board[i - j][j], 0), (0, board[i - j][j])]:
            if 0 <= i - j + dy < N and 0 <= j + dx < N:
                jumps[i - j + dy][j + dx] += jumps[i - j][j]

print(jumps[-1][-1])
