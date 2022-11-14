# https://www.acmicpc.net/problem/2568

import sys
from bisect import bisect_left

readline = lambda: sys.stdin.readline().strip()


N = int(readline())
lines = [tuple(map(int, readline().split())) for _ in range(N)]
lines.sort(key=lambda v: v[0])

dp = [[0]]
for i in range(1, len(lines)):
    if lines[dp[-1][-1]][1] < lines[i][1]:
        dp.append([*dp[-1], i])
    else:
        j = bisect_left(dp, lines[i][1], 0, len(dp) - 1, key=lambda trace: lines[trace[-1]][1])
        dp[j] = [*dp[j - 1], i] if 0 < j else [i]

print(N - len(dp))
k = 0
for i in range(len(lines)):
    if len(dp[-1]) <= k or dp[-1][k] != i:
        print(lines[i][0])
    else:
        k += 1
