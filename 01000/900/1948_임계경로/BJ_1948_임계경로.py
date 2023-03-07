# https://www.acmicpc.net/problem/1948

import sys
from collections import deque
from heapq import heappush
from heapq import heappop
from operator import itemgetter


def dijkstra(g, from_):
    dists = [0 for _ in range(len(g))]
    traces = [[] for _ in range(len(g))]
    heappush(pendings := [], (0, from_))
    while 0 < len(pendings):
        dist, u = heappop(pendings)
        if -dist == dists[u]:
            for edge_id, v, w in g[u]:
                if dists[v] < -dist + w:
                    heappush(pendings, (dist - w, v))
                    dists[v] = -dist + w
                    traces[v].clear()
                if dists[v] == -dist + w:
                    traces[v].append(edge_id)

    return dists, traces

readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
M = int(readline())

g = [{} for _ in range(N)]
edges = [0] * M
for edge_id in range(M):
    u, v, w = map(int, readline().split())
    u, v = u - 1, v - 1
    edges[edge_id] = u

    if v in g[u]:
        g[u][v] = max(g[u][v], (edge_id, v, w), key=itemgetter(-1))
    else:
        g[u][v] = (edge_id, v, w)

for u, max_edges in enumerate(g):
    g[u] = (*max_edges.values(), )

from_, to = map(int, readline().split())
dists, traces = dijkstra(g, from_ - 1)

n_roads = 0
visited = [False] * N
pendings = deque([to - 1])
while 0 < len(pendings):
    u = pendings.popleft()
    n_roads += len(traces[u])
    for edge_id in traces[u]:
        v = edges[edge_id]
        if not visited[v]:
            visited[v] = True
            pendings.append(v)

print(dists[to - 1])
print(n_roads)
