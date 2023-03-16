# https://www.acmicpc.net/problem/1854

import sys
from heapq import heappush
from heapq import heappop
from heapq import heapreplace


def dijkstra(edges, K):
    dists = [[] for _ in range(len(edges))]
    dists[0].append(0)

    pendings = [(0, 0)]
    while 0 < len(pendings):
        dist, u = heappop(pendings)
        for v, w in edges[u]:
            if len(dists[v]) < K:
                heappush(dists[v], -(dist + w))
            elif dists[v][0] < -(dist + w):
                heapreplace(dists[v], -(dist + w))
            else:
                continue
            heappush(pendings, (dist + w, v))

    return dists


INF = float('inf')
readline = lambda: sys.stdin.readline().rstrip()  # noqa

V, E, K = map(int, readline().split())

edges = [[] for _ in range(V)]
for _ in range(E):
    u, v, w = map(int, readline().split())
    edges[u - 1].append((v - 1, w))

for dists in dijkstra(edges, K):
    print(-dists[0] if len(dists) == K else -1)
