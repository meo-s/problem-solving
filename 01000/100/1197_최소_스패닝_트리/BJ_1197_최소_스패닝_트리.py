# https://www.acmicpc.net/problem/1197

import sys
from collections import defaultdict
from heapq import heappush, heappop

readline = lambda: sys.stdin.readline().strip()

V, E = map(int, readline().split())
edges = defaultdict(list)
for _ in range(E):
    u, v, w = map(int, readline().split())
    edges[u].append((v, w))
    edges[v].append((u, w))

cuts = []
for v, w in edges[1]:
    heappush(cuts, (w, v))

cost = 0
visited = [False] * (V + 1)
visited[1] = True
while 0 < len(cuts):
    w, u = heappop(cuts)
    if not visited[u]:
        cost += w
        visited[u] = True
        for v, w in edges[u]:
            heappush(cuts, (w, v))

print(cost)
