# https://www.acmicpc.net/problem/11727

N = int(input())

dp = [1, 3]
for _ in range(N - 1):
    dp.append((2 * dp[-2] + dp[-1]) % 10007)

print(dp[N - 1])
