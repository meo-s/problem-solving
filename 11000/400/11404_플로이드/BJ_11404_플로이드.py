# https://www.acmicpc.net/problem/11404

import sys
from collections import defaultdict

INF = float('inf')
readline = lambda: sys.stdin.readline().strip()

V, E = int(readline()), int(readline())

costs = [[INF] * V for _ in range(V)]
for i in range(V):
    costs[i][i] = 0
for _ in range(E):
    u, v, w = map(int, readline().split())
    costs[u - 1][v - 1] = min(costs[u - 1][v - 1], w)

for k in range(V):
    for i in range(V):
        for j in range(V):
            costs[i][j] = min(costs[i][j], costs[i][k] + costs[k][j])

for i in range(V):
    print(' '.join(map(str, [[0, cost][cost != INF] for cost in costs[i]])))
