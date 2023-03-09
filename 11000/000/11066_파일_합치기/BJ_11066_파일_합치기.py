# https://www.acmicpc.net/problem/11066

import sys
from itertools import repeat

INF = float('inf')
readline = lambda: sys.stdin.readline().rstrip()  # noqa

for _ in range(int(readline())):
    N = int(readline())
    dp = [[[INF] * N for _ in range(N)] for _ in range(2)]
    dp[0][0][:] = map(int, readline().split())
    dp[1][0][:] = repeat(0, N)
    for k in range(1, N):
        for i in range(N - k):
            for j in range(k):
                size = dp[0][j][i] + dp[0][k - (j + 1)][i + j + 1]
                cost = dp[1][j][i] + dp[1][k - (j + 1)][i + j + 1] + size
                if size < dp[0][k][i]:
                    dp[0][k][i] = size
                    dp[1][k][i] = cost
                elif size == dp[0][k][i]:
                    dp[1][k][i] = min(dp[1][k][i], cost)

    print(dp[1][N - 1][0])
