# https://www.acmicpc.net/problem/1389

import sys
readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
matrix = [[0] * (N + 1) for _ in range(N + 1)]
for col in range(1, N + 1):
    for row, n in enumerate(map(int, readline().split()), start=1):
        matrix[row][col] = n + matrix[row - 1][col] + matrix[row][col - 1] - matrix[row - 1][col - 1]

for _ in range(M):
    x1, y1, x2, y2 = map(int, readline().split())
    print(matrix[y2][x2] - matrix[y2][x1 - 1] - matrix[y1 - 1][x2] + matrix[y1 - 1][x1 - 1])

