# https://www.acmicpc.net/problem/23894

import math
import sys


INF = float('inf')
readline = lambda: sys.stdin.readline().rstrip()  # noqa

L = int(math.log2(10**9)) + 1
N = int(readline())

f = [[0] * (N + 1) for _ in range(L)]
f[0][1:] = map(int, readline().split())

bottlenecks = [INF] * (N + 1)
bottlenecks[1] = 0
for i, x in enumerate(f[0][2:], start=2):
    if x == 1:
        bottlenecks[i] = 1

for n in range(1, L):
    for x in range(1, N + 1):
        f[n][x] = f[n - 1][f[n - 1][x]]
        if bottlenecks[x] == INF and bottlenecks[f[n - 1][x]] != INF:
            bottlenecks[x] = bottlenecks[f[n - 1][x]] + (1 << (n - 1))

for _ in range(int(readline())):
    q, *args = map(int, readline().split())
    if q == 1:
        f[0][1] = args[0]
    else:
        m, x = args
        while 0 < m and bottlenecks[x] <= m:
            m -= bottlenecks[x]
            x = 1
            if 0 < m:
                x = f[0][1]
                m = int((m - 1) % (bottlenecks[x] + 1))

        i = -1
        while 0 < m:
            i += 1
            if (m & (1 << i)) != 0:
                m ^= 1 << i
                x = f[i][x]

        print(x)
