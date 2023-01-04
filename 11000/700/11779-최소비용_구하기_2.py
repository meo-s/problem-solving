# https://www.acmicpc.net/problem/11779

import sys
from collections import defaultdict
from heapq import heappop
from heapq import heappush

INF = float('inf')
readline = lambda: sys.stdin.readline().strip()

N, M = (int(readline()) for _ in range(2))
buses = defaultdict(lambda: defaultdict(lambda: INF))
for _ in range(M):
    u, v, w = map(int, readline().split())
    buses[u][v] = min(buses[u][v], w)

start, goal = map(int, readline().split())
costs = [(INF, None) for _ in range(N + 1)]
costs[start] = (0, None)

cities = [(0, start)]
while 0 < len(cities):
    cost, u = heappop(cities)
    for v, w in buses[u].items():
        if (new_cost := costs[u][0] + w) < costs[v][0]:
            costs[v] = (new_cost, u)
            heappush(cities, (new_cost, v))

trace = [goal]
while costs[trace[0]][1] is not None:
    trace.insert(0, costs[trace[0]][1])

print(costs[goal][0])
print(len(trace))
print(*trace, sep=' ')
