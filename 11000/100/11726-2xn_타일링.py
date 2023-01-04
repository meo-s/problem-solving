# https://www.acmicpc.net/problem/11726

N = int(input())
dp = [1, 2] + [0] * (N - 2)
for i in range(2, N):
    dp[i] = (dp[i - 1] + dp[i - 2]) % 10007

print(dp[N - 1])
