# https://www.acmicpc.net/problem/1167

import sys
from collections import defaultdict, deque


def bfs(tree, V, start=1):
    visited = [False] * (V + 1)
    visited[start] = True

    farthest = (start, 0)
    vertices = deque([farthest])
    while 0 < len(vertices):
        u, diameter = vertices.popleft()
        for v, w in tree[u]:
            if not visited[v]:
                visited[v] = True
                vertices.append((v, diameter + w))
                if farthest[1] < vertices[-1][1]:
                    farthest = vertices[-1]

    return farthest


readline = lambda: sys.stdin.readline().strip()

tree = defaultdict(list)
for _ in range(V := int(readline())):
    info = [*map(int, readline().split())]
    for i in range(1, len(info) - 1, 2):
        v, w = info[i:i + 2]
        tree[info[0]].append((v, w))

print(bfs(tree, V, bfs(tree, V)[0])[1])
