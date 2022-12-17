# https://www.acmicpc.net/problem/14938

import sys

INF = float('inf')
readline = lambda: sys.stdin.readline().strip()

V, R, E = map(int, readline().split())
items = [*map(int, readline().split())]

dists = [[INF] * (V + 1) for _ in range(V + 1)]
for i in range(1, V + 1):
    dists[i][i] = 0
for _ in range(E):
    u, v, w = map(int, readline().split())
    dists[u][v] = min(dists[u][v], w)
    dists[v][u] = min(dists[v][u], w)

for i in range(1, V + 1):
    for u in range(1, V + 1):
        for v in range(1, V + 1):
            dists[u][v] = min(dists[u][v], dists[u][i] + dists[i][v])

max_items = max(sum(items[v] for v in range(V) if dists[u + 1][v + 1] <= R) for u in range(V))
print(max_items)
