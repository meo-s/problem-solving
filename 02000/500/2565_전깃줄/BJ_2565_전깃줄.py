# https://www.acmicpc.net/problem/2565

import sys
from bisect import bisect_left

readline = lambda: sys.stdin.readline().strip()


N = int(readline())
lines = [tuple(map(int, readline().split())) for _ in range(N)]
lines.sort(key=lambda v: v[0])

dp = [0]
for i in range(1, len(lines)):
    if lines[dp[-1]][1] < lines[i][1]:
        dp.append(i)
    else:
        j = bisect_left(dp, lines[i][1], 0, len(dp) - 1, key=lambda line_index: lines[line_index][1])
        dp[j] = i

print(N - len(dp))