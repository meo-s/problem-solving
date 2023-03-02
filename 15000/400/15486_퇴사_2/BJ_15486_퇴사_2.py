import sys

readline = lambda: sys.stdin.readline().rstrip()

N = int(readline())
schedules = [tuple(map(int, readline().split())) for _ in range(N)]

dp = [0] * (N + 1)
max_earn = 0
for i, (time, earn) in enumerate(schedules, start=0):
    dp[i] = max_earn = max(max_earn, dp[i])
    if i + time <= N:
        dp[i + time] = max(dp[i + time], max_earn + earn)

print(max(dp))
