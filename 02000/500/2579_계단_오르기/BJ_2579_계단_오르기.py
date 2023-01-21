# https://www.acmicpc.net/problem/2579

import sys

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
stairs = [0] + [int(readline()) for _ in range(N)]

dp = [0, 0, 0]
for i in range(1, N + 1):
    dp.append(max(dp[-2], dp[-3] + stairs[i - 1]) + stairs[i])

print(dp[-1])
