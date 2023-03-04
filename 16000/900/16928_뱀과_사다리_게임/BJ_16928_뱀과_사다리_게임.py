# https://www.acmicpc.net/problem/16928

import sys
from collections import deque

readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
jumps = {}
for _ in range(N + M):
    f, t = map(int, readline().split())
    jumps[f] = t

visited = [-1, 0] + [-1] * 99
positions = deque([1])
while visited[-1] < 0:
    x = positions.popleft()
    for dx in range(1, 7):
        if not (y := x + dx) <= 100:
            continue
        if visited[y] < 0:
            visited[y] = visited[x] + 1

            while y in jumps:
                if visited[(y := jumps[y])] < 0:
                    visited[y] = visited[x] + 1

            if visited[y] == visited[x] + 1:
                positions.append(y)

print(visited[-1])
