# https://www.acmicpc.net/problem/14589

import math
import sys
from operator import itemgetter
from heapq import heappop
from heapq import heappush

INF = float('inf')
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
L = math.ceil(math.log2(N)) + 1
jumps = [[-1] * L for _ in range(N + 1)]
costs = [[-1] * L for _ in range(N + 1)]
lines = [(-INF, -INF, 0)] + [(*map(int, readline().split()), i + 1) for i in range(N)]

max_r = 0
pendings = []
for l, r, i in sorted(lines, key=itemgetter(0))[1:]:
    while 0 < len(pendings) and pendings[0][0] < l:
        u = heappop(pendings)[1]
        jumps[u][0] = [u, max_r][lines[u][1] < lines[max_r][1]]
        costs[u][0] = int(lines[u][1] < lines[max_r][1])

    if lines[max_r][1] < r:
        max_r = i

    heappush(pendings, (r, i))

for _, u in pendings:  # 힙에 남아 있는 라인들을 처리
    jumps[u][0] = [u, max_r][lines[u][1] < lines[max_r][1]]
    costs[u][0] = int(lines[u][1] < lines[max_r][1])

for i in range(1, L):
    for u in range(1, len(lines)):
        jumps[u][i] = jumps[jumps[u][i - 1]][i - 1]
        costs[u][i] = costs[jumps[u][i - 1]][i - 1] + costs[u][i - 1]

for _ in range(int(readline())):
    u, v = map(int, readline().split())
    if lines[v][0] < lines[u][0]:
        u, v = v, u

    dist = 0
    for i in reversed(range(L)):
        if lines[jumps[u][i]][1] < lines[v][0]:
            dist += costs[u][i]
            u = jumps[u][i]

    if lines[u][1] < lines[v][0]:
        dist += 1
        u = jumps[u][0]

    if lines[v][1] < lines[u][0] or lines[u][1] < lines[v][0]:
        print(-1)
    else:
        print(dist + 1)
