# https://www.acmicpc.net/problem/1916

import sys
from collections import defaultdict
from heapq import heappop
from heapq import heappush

INF = float('inf')
readline = lambda: sys.stdin.readline().strip()

N, M = map(int, (readline() for _ in range(2)))
buses = defaultdict(lambda: defaultdict(lambda: INF))
for _ in range(M):
    u, v, w = map(int, readline().split())
    buses[u][v] = min(buses[u][v], w)

start, goal = map(int, readline().split())
costs = [INF] * (N + 1)
costs[start] = 0

cities = [(0, start)]
while 0 < len(cities):
    cost, u = heappop(cities)
    for v, w in buses[u].items():
        if cost + w < costs[v]:
            costs[v] = cost + w
            heappush(cities, (costs[v], v))

print(costs[goal])
