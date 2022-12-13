# https://www.acmicpc.net/problem/1504

import sys
from collections import defaultdict
from heapq import heappop
from heapq import heappush


def dijkstra(V, edges, start, goal):
    if start == goal:
        return 0

    costs = [INF] * (V + 1)
    costs[start] = 0

    vertices = [(0, start)]
    while 0 < len(vertices):
        cost, u = heappop(vertices)
        for v, w in edges[u]:
            if (new_cost := cost + w) < costs[v]:
                costs[v] = new_cost
                heappush(vertices, (new_cost, v))

    return costs[goal]


INF = float('inf')
readline = lambda: sys.stdin.readline().strip()

V, E = map(int, readline().split())
edges = defaultdict(list)
for _ in range(E):
    u, v, w = map(int, readline().split())
    edges[u].append((v, w))
    edges[v].append((u, w))

v1, v2 = map(int, readline().split())
min_cost = min(dijkstra(V, edges, 1, u) + dijkstra(V, edges, u, v) + dijkstra(V, edges, v, V) \
               for u, v in [(v1, v2), (v2, v1)])
print([-1, min_cost][min_cost != INF])
