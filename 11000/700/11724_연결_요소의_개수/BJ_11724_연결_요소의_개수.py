# https://www.acmicpc.net/problem/11724

import sys
from collections import defaultdict

readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
edges = defaultdict(list)
for _ in range(M):
    u, v = map(int, readline().split())
    edges[u].append(v)
    edges[v].append(u)


def dfs(v, visited):
    visited[v] = True
    vertexs = [v]
    while 0 < len(vertexs):
        v = vertexs.pop()
        for v_other in edges[v]:
            if not visited[v_other]:
                visited[v_other] = True
                vertexs.append(v_other)


n_connections = 0
visited = [False] * (N + 1)
for v in range(1, N + 1):
    if not visited[v]:
        n_connections += 1
        dfs(v, visited)

print(n_connections)
