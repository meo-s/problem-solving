# https://www.acmicpc.net/problem/1753

import sys
from collections import defaultdict
from heapq import heappop, heappush

INF = float('inf')
readline = lambda: sys.stdin.readline().strip()

V, E = map(int, readline().split())

costs = [INF] * (V + 1)
costs[(start := int(readline()))] = 0

edges = defaultdict(list)
for _ in range(E):
    u, v, w = map(int, readline().split())
    edges[u].append((v, w))

vertexs = [(0, start)]
while 0 < len(vertexs):
    cost, u = heappop(vertexs)
    for v, w in edges[u]:
        if cost + w < costs[v]:
            costs[v] = cost + w
            heappush(vertexs, (costs[v], v))

for cost in costs[1:]:
    print(['INF', cost][cost != INF])
