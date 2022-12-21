# https://www.acmicpc.net/problem/11054

import sys
import operator
from bisect import bisect_left


def LIS(seq):
    lis, dp = [], [float('-inf')]
    for i, n in enumerate(seq):
        if dp[-1] < n:
            dp.append(n)
        else:
            dp[bisect_left(dp, n, 0, len(dp) - 1)] = n

        lis.append((len(dp) - 1, dp[-1]))

    return lis


readline = lambda: sys.stdin.readline().strip()

N = int(readline())
seq = [*map(int, readline().split())]

lis, lds = LIS(seq), LIS(seq[::-1])
print(max(lis[i][0] + lds[j][0] - int(lis[i][1] == lds[j][1]) for i, j in zip(range(N), reversed(range(N)))))
