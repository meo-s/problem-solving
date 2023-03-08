# https://www.acmicpc.net/problem/1219

import sys

INF = float('inf')
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N, S, G, M = map(int, readline().split())

edges = [{} for _ in range(N)]
for u, v, cost in (map(int, readline().split()) for _ in range(M)):
    edges[u][v] = max(edges[u].get(v, -INF), -cost)

for u, min_edges in enumerate(edges):
    edges[u] = [*min_edges.items()]

earns = [*map(int, readline().split())]

dists = [-INF] * N
dists[S] = earns[S]
neg_cycles = []
for i in range(N):
    for u in range(N):
        if dists[u] != -INF:
            for v, cost in edges[u]:
                if dists[v] < dists[u] + cost + earns[v]:
                    dists[v] = dists[u] + cost + earns[v]
                    neg_cycles.append(u) if i == N - 1 else None

neg_visited = [False] * N
for u in neg_cycles:
    neg_visited[u] = True

while 0 < len(neg_cycles):
    u = neg_cycles.pop()
    for v, _ in edges[u]:
        if not neg_visited[v]:
            neg_visited[v] = True
            neg_cycles.append(v)

if dists[G] == -INF:
    print('gg')
else:
    print(['Gee', dists[G]][not neg_visited[G]])
