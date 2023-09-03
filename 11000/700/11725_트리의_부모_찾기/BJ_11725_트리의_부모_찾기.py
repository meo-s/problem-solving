# https://www.acmicpc.net/problem/11725

import sys
from collections import defaultdict

readline = lambda: sys.stdin.readline().strip()

edges = defaultdict(list)
for _ in range(1, N := int(readline())):
    u, v = map(int, readline().split())
    edges[v].append(u)
    edges[u].append(v)

parents = [0] * (N + 1)

visited = [False] * (N + 1)
vertexs = [1]
while 0 < len(vertexs):
    v = vertexs.pop()
    for u in edges[v]:
        if not visited[u]:
            visited[u] = True
            parents[u] = v
            vertexs.append(u)

for i in range(2, N + 1):
    print(parents[i])
