# https://www.acmicpc.net/problem/11659

import sys

readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())

cumsums = [0, 0] * N
for i, n in enumerate(map(int, readline().split()), start=1):
    cumsums[i] = n + cumsums[i - 1]

for _ in range(M):
    i, j = map(int, readline().split())
    print(cumsums[j] - cumsums[i - 1])
