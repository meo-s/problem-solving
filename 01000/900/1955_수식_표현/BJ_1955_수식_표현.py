# https://www.acmicpc.net/problem/1955

import math
import sys


readline = lambda: sys.stdin.readline().rstrip()

n = int(readline())
factorials = [math.factorial(i) for i in range(8)]

dp = [float('inf')] * (n + 1)
dp[1] = 1

for k in range(2, n + 1):
    for i in range(1, k):
        dp[k] = min(dp[k], dp[i] + dp[k - i])
    for i in range(1, math.floor(math.sqrt(k)) + 1):
        dp[k] = min(dp[k], dp[i] + dp[k // i]) if k % i == 0 else dp[k]
    for i in range(2, min(k, len(factorials))):
        dp[k] = min(dp[k], dp[i]) if k == factorials[i] else dp[k]

print(dp[n])
