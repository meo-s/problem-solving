# https://www.acmicpc.net/problem/2294

import sys

INF = float('inf')
readline = lambda: sys.stdin.readline().rstrip()

N, K = map(int, readline().split())
coins = set(int(readline()) for _ in range(N))
coins = [coin for coin in coins if coin <= K]

dp = [INF] * (K + 1)
for coin in coins:
    dp[coin] = 1

for i in range(K + 1):
    for coin in coins:
        if 0 <= i - coin:
            dp[i] = min(dp[i], dp[i - coin] + 1)

print([-1, dp[K]][dp[K] < INF])
