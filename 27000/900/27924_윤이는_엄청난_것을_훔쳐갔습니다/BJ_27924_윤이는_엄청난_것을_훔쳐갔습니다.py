# https://www.acmicpc.net/problem/27924

import sys
from collections import deque

readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
edges = [[] for _ in range(N + 1)]
for _ in range(N - 1):
    u, v = map(int, readline().split())
    edges[u].append(v)
    edges[v].append(u)

a, b, c = map(int, readline().split())
visited = [False] * (N + 1)
visited[a] = True
blocked = [False] * (N + 1)
blocked[b] = True
blocked[c] = True

waypoints = [deque([a]), deque([b, c])]
while 0 < len(waypoints[0]):
    for _ in range(len(waypoints[0])):
        u = waypoints[0].popleft()
        if len(edges[u]) == 1:
            print('YES')
            sys.exit(0)
        if not blocked[u]:  # 정점 도착 이후 경찰에게 잡힌 경우는 배제
            for v in edges[u]:
                if not blocked[v] and not visited[v]:
                    visited[v] = True
                    waypoints[0].append(v)

    for _ in range(len(waypoints[1])):
        u = waypoints[1].popleft()
        for v in edges[u]:
            if not blocked[v]:
                blocked[v] = True
                waypoints[1].append(v)

print('NO')
