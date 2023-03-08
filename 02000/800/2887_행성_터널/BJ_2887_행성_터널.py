# https://www.acmicpc.net/problem/2887

import sys
from heapq import heappush
from heapq import heappop
from operator import itemgetter


def calc_dist(planet1, planet2):
    return min(abs(c1 - c2) for c1, c2 in zip(planet1, planet2))


INF = float('inf')
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
planets = [(planet_id, *map(int, readline().split())) for planet_id in range(N)]

xyz = [[*planets] for _ in range(3)]
indices = [[0, 0, 0] for _ in range(N)]
for metric in range(3):
    xyz[metric].sort(key=itemgetter(metric + 1))
    for i, (u, *_) in enumerate(xyz[metric]):
        indices[u][metric] = i

cuts = []
thresholds = [0] + [INF] * (N - 1)
for metric in range(3):
    index = indices[0][metric]
    for dindex in [-1, 1]:
        if not 0 <= index + dindex < N:
            continue

        v, *pos = xyz[metric][index + dindex]
        dist = calc_dist(planets[0][1:], pos)
        if dist < thresholds[v]:
            thresholds[v] = dist
            heappush(cuts, (dist, v))

mst_weight = 0
n_conns = 0
visited = [True] + [False] * (N - 1)
while True:
    w, u = heappop(cuts)
    if not visited[u]:
        mst_weight += w
        visited[u] = True
        if (n_conns := n_conns + 1) == N - 1:
            break

        for metric in range(3):
            index = indices[u][metric]
            for dindex in [-1, 1]:
                if not 0 <= index + dindex < N:
                    continue

                v, *pos = xyz[metric][index + dindex]
                dist = calc_dist(planets[u][1:], pos)
                if dist < thresholds[v]:
                    thresholds[v] = dist
                    heappush(cuts, (dist, v))

print(mst_weight)
