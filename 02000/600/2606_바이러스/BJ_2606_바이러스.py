# https://www.acmicpc.net/problem/2606

import sys
from collections import defaultdict

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
M = int(readline())

edges = defaultdict(list)
for _ in range(M):
    v1, v2 = map(int, readline().split())
    edges[v1].append(v2)
    edges[v2].append(v1)

n_propagations = 0
visited = [False, True] + [False] * (N - 1)
computers = [1]
while 0 < len(computers):
    computer = computers.pop()
    for other in edges[computer]:
        if not visited[other]:
            n_propagations += 1
            visited[other] = True
            computers.append(other)

print(n_propagations)
