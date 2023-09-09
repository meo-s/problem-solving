# https://www.acmicpc.net/problem/29792

import sys


readline = lambda: sys.stdin.readline().rstrip()

N, M, K = map(int, readline().split())
D = [int(readline()) for _ in range(N)]

times = []
mesos = []
for k in range(K):
    P, meso = map(int, readline().split())

    times.append([])
    mesos.append(meso)

    for i in range(N):
        times[k].append(P//D[i] + int(P%D[i] != 0))

profits = []
for i in range(N):
    dp = [float('-inf')] * (60 * 15 + 1)
    dp[0] = 0

    for k in range(K):
        for t in reversed(range(len(dp))):
            if t + times[k][i] < len(dp):
                dp[t + times[k][i]] = max(dp[t + times[k][i]], dp[t] + mesos[k])

    profits.append(max(dp))

print(sum(sorted(profits, reverse=True)[:M]))
