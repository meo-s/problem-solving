# https://www.acmicpc.net/problem/1865

import sys
from collections import defaultdict


def bellman_ford(V, edges, start):
    costs = [INF] * (V + 1)
    costs[start] = 0
    for _ in range(V):
        for u in edges.keys():
            for v, w in edges[u]:
                costs[v] = min(costs[v], costs[u] + w)

    for u in edges.keys():
        for v, w in edges[u]:
            if costs[u] + w < costs[v]:
                return False

    return True


INF = sys.maxsize
readline = lambda: sys.stdin.readline().strip()

for _ in range(int(readline())):
    V, E, W = map(int, readline().split())

    edges = defaultdict(list)
    for _ in range(E):
        u, v, w = map(int, readline().split())
        edges[u].append((v, w))
        edges[v].append((u, w))
    for _ in range(W):
        u, v, w = map(int, readline().split())
        edges[u].append((v, -w))

    print(['NO', 'YES'][not bellman_ford(V, edges, 1)])
