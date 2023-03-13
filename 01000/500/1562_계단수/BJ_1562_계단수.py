# https://www.acmicpc.net/problem/1562

from itertools import repeat


N = int(input())
M = 1_000_000_000

dp = [[[0] * (1 << 10) for _ in range(10)] for _ in range(2)]
for i in range(1, 10):
    dp[0][i][1 << i] = 1

for _ in range(1, N):
    for n in range(10):
        dp[1][n][:] = repeat(0, 1 << 10)
        if 0 < n:
            for i in range(1 << 10):
                dp[1][n][i | (1 << n)] += dp[0][n - 1][i]
                dp[1][n][i | (1 << n)] %= M
        if n < 9:
            for i in range(1 << 10):
                dp[1][n][i | (1 << n)] += dp[0][n + 1][i]
                dp[1][n][i | (1 << n)] %= M

    dp[:] = dp[::-1]

print(sum(dp[0][n][-1] for n in range(10)) % M)
