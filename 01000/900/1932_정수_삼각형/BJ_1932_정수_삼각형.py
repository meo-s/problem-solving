# https://www.acmicpc.net/problem/1932

import sys

readline = lambda: sys.stdin.readline().strip()

dp = [[0]]
for _ in range(int(readline())):
    dp.append([float('-inf')] * len(nums := tuple(map(int, readline().split()))))
    for i, n in enumerate(nums):
        if 0 <= i - 1:
            dp[-1][i] = max(dp[-1][i], dp[-2][i - 1] + n)
        if i < len(dp[-2]):
            dp[-1][i] = max(dp[-1][i], dp[-2][i] + n)

print(max(dp[-1]))
