# https://www.acmicpc.net/problem/1260

import sys
from collections import deque, defaultdict

readline = lambda: sys.stdin.readline().strip()

N, M, V = map(int, readline().split())
edges = defaultdict(set)
for _ in range(M):
    v1, v2 = map(int, readline().split())
    edges[v1].add(v2)
    edges[v2].add(v1)

for k, v in edges.items():
    edges[k] = sorted(v)


def dfs(v, visited):
    visited[v] = True
    print(v, end=' ')

    for v_other in edges[v]:
        if not visited[v_other]:
            dfs(v_other, visited)


def bfs(discoverd, visited):
    v = discoverd.popleft()
    visited[v] = True
    print(v, end=' ')

    for v_other in edges[v]:
        if not visited[v_other]:
            visited[v_other] = True
            discoverd.append(v_other)

    bfs(discoverd, visited) if 0 < len(discoverd) else None


dfs(V, [False] * (N + 1))
print()
bfs(deque([V]), [False] * (N + 1))
