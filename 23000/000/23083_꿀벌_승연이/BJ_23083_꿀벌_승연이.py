# https://www.acmicpc.net/problem/23083

import sys

M = 1_000_000_007
deltas = [[(-1, 0), (-1, -1), (0, -1)], [(-1, 0), (0, -1), (1, -1)]]
readline = lambda: sys.stdin.readline().rstrip()  # noqa

H, W = map(int, readline().split())
K = int(readline())

dp = [[0] * W for _ in range(H)]
for _ in range(K):
    y, x = map(int, readline().split())
    dp[y - 1][x - 1] = -1

dp[0][0] = 1
for x in range(W):
    for y in range(H):
        if dp[y][x] != -1:
            for dy, dx in deltas[x % 2]:
                if 0 <= y + dy < H and 0 <= x + dx < W:
                    dp[y][x] += max(0, dp[y + dy][x + dx])
                    dp[y][x] %= M

print(dp[H - 1][W - 1])
