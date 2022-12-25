# https://www.acmicpc.net/problem/1005

import sys
from heapq import heappop
from heapq import heappush
from collections import defaultdict

readline = lambda: sys.stdin.readline().strip()

for _ in range(int(readline())):
    N, K = map(int, readline().split())
    D = [*map(int, readline().split())]

    next_techs = defaultdict(list)
    dependencies = [0] * N
    for _ in range(K):
        u, v = map(int, readline().split())
        next_techs[u - 1].append(v - 1)
        dependencies[v - 1] += 1

    jobs = []
    for tech, dependency in enumerate(dependencies):
        heappush(jobs, (D[tech], tech)) if dependency == 0 else None

    W = int(readline())
    logs = [-1] * N
    while logs[W - 1] < 0:
        time, u = heappop(jobs)
        logs[u] = time
        for v in next_techs[u]:
            dependencies[v] -= 1
            if dependencies[v] == 0:
                heappush(jobs, (time + D[v], v))

    print(logs[W - 1])
