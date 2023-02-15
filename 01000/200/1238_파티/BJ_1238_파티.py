# https://www.acmicpc.net/problem/1238

import sys
from collections import defaultdict
from heapq import heappush
from heapq import heappop
'''
def dijkstra(V, edges, start, goal):
    costs = [INF] * (V + 1)
    costs[start] = 0

    villages = [(0, start)]
    while 0 < len(villages):
        cost, u = heappop(villages)
        for v, w in edges[u]:
            if cost + w < costs[v]:
                costs[v] = cost + w
                heappush(villages, (costs[v], v))

    return costs[goal]


INF = float('inf')
readline = lambda: sys.stdin.readline().strip()

N, M, X = map(int, readline().split())
roads = defaultdict(list)
for _ in range(M):
    u, v, w = map(int, readline().split())
    roads[u].append((v, w))

print(max(dijkstra(N, roads, i, X) + dijkstra(N, roads, X, i) for i in range(1, N + 1)))
'''


def dijkstra(V, edges, start):
    costs = [INF] * (V + 1)
    costs[start] = 0

    villages = [(0, start)]
    while 0 < len(villages):
        cost, u = heappop(villages)
        for v, w in edges[u]:
            if cost + w < costs[v]:
                costs[v] = cost + w
                heappush(villages, (costs[v], v))

    return costs[1:]


INF = float('inf')
readline = lambda: sys.stdin.readline().strip()

N, M, X = map(int, readline().split())
roads_i2X = defaultdict(list)
roads_X2i = defaultdict(list)
for _ in range(M):
    u, v, w = map(int, readline().split())
    roads_i2X[u].append((v, w))
    roads_X2i[v].append((u, w))

print(max([sum(cost) for cost in zip(dijkstra(N, roads_i2X, X), dijkstra(N, roads_X2i, X))]))
