# https://www.acmicpc.net/problem/2623

import sys
from collections import defaultdict

INF = float('inf')
readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())  # N: 가수, M: 보조 PD
next_singers = defaultdict(list)
dependencies = [0] * (N + 1)
dependencies[0] = INF
for _ in range(M):
    _, *singers = map(int, readline().split())
    for u, v in zip(singers[:-1], singers[1:]):
        next_singers[u].append(v)
        dependencies[v] += 1

stage = []
for i, dependency in enumerate(dependencies):
    stage.append(i) if dependency == 0 else None

i = -1
while i < len(stage) - 1:
    i += 1
    for singer in next_singers[stage[i]]:
        dependencies[singer] -= 1
        stage.append(singer) if dependencies[singer] == 0 else None

print(*[[0], stage][len(stage) == N], sep='\n')
