# https://www.acmicpc.net/problem/5719

import sys
from collections import deque
from heapq import heappush, heappop


def readline():
    return sys.stdin.readline().rstrip()


def dijkstra(graph, start, trace=None, blacklist=set()):
    if trace is not None:
        trace.clear()
        for _ in range(V):
            trace.append([])

    dists = [float('inf')] * len(graph)
    dists[start] = 0

    pendings = [(0, start)]
    while 0 < len(pendings):
        dist, u = heappop(pendings)
        for edge_id, v, w in graph[u]:
            if edge_id not in blacklist:
                if dist + w < dists[v]:
                    trace[v].clear() if trace is not None else None
                    dists[v] = dist + w
                    heappush(pendings, (dist + w, v))

                if dist + w == dists[v]:
                    trace[v].append(edge_id) if trace is not None else None

    return trace or dists


while True:
    V, E = map(int, readline().split())
    if (V, E) == (0, 0):
        break

    S, D = map(int, readline().split())

    edges = [None] * E
    graph = [[] for _ in range(V)]
    for edge_id in range(E):
        u, v, w = map(int, readline().split())
        edges[edge_id] = (u, v)
        graph[u].append((edge_id, v, w))

    trace = dijkstra(graph, S, trace=[])
    visited = set()
    pendings = deque([D])
    blacklist = set()
    while 0 < len(pendings):
        u = pendings.popleft()
        for edge_id in trace[u]:
            blacklist.add(edge_id)
            v, _ = edges[edge_id]
            if v not in visited:
                visited.add(v)
                pendings.append(v)

    dists = dijkstra(graph, S, blacklist=blacklist)
    print([-1, dists[D]][dists[D] < float('inf')])
