# https://www.acmicpc.net/problem/2001

import sys
from itertools import chain
from collections import deque

readline = lambda: sys.stdin.readline().rstrip()  # noqa

V, E, J = map(int, readline().split())
jpoints = [int(readline()) - 1 for _ in range(J)]

tols = [[0] * V for _ in range(V)]
for _ in range(E):
    u, v, w = map(int, readline().split())
    u, v = u - 1, v - 1
    tols[u][v] = w
    tols[v][u] = w

for k in range(V):
    for u in range(V):
        for v in range(V):
            if tols[u][v] < min(tols[u][k], tols[k][v]):
                tols[u][v] = min(tols[u][k], tols[k][v])

max_jewels = 0
waypoints = deque([(0, 0, 0)])  # 현재 섬, 보석 개수, 보석 비트마스크
visited = [[False] * V for _ in range(2**J)]
while 0 < len(waypoints):
    u, j, jmask = waypoints.popleft()
    max_jewels = max(max_jewels, j) if u == 0 else max_jewels

    for jindex, v in enumerate(jpoints):
        if u == v:
            if (jmask & (1 << jindex)) == 0:
                visited[jmask | (1 << jindex)][v] = True
                waypoints.append((u, j + 1, jmask | (1 << jindex)))
            break

    for v in chain(jpoints, [0]):
        if j <= tols[u][v] and not visited[jmask][v]:
            visited[jmask][v] = True
            waypoints.append((v, j, jmask))

print(max_jewels)
