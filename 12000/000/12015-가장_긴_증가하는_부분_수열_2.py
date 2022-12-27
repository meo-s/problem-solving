# https://www.acmicpc.net/problem/12015

import sys


def lower_bound(a, v, lb=0, ub=None):
    ub = len(a) if ub is None else ub
    while lb < ub:
        if a[(mid := (lb + ub) // 2)] < v:
            lb = mid + 1
        else:
            ub = mid

    return lb


def LIS(seq):
    dp = [float('-inf')]
    for n in seq:
        if dp[-1] < n:
            dp.append(n)
        else:
            dp[lower_bound(dp, n, ub=len(dp) - 1)] = n

    return len(dp) - 1


readline = lambda: sys.stdin.readline().strip()

N = int(readline())
print(LIS([*map(int, readline().split())]))
