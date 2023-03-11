# https://www.acmicpc.net/problem/4386

import sys


def find_next(thresholds, visited):
    u = -1
    for v in range(len(visited)):
        if not visited[v]:
            u = u if 0 <= u else v
            u = v if thresholds[v] < thresholds[u] else u

    return u


def dist_sq_of(a, b):
    return (a[0] - b[0])**2 + (a[1] - b[1])**2


INF = float('inf')
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
stars = [tuple(map(float, readline().split())) for _ in range(N)]

n_conns = 0
visited = [False] * N
thresholds = [0] + [INF] * (N - 1)
mst_wegiht = 0
while n_conns < N:
    u = find_next(thresholds, visited)
    n_conns += 1
    visited[u] = True
    mst_wegiht += thresholds[u] ** 0.5

    for v in range(1, N):
        if not visited[v]:
            thresholds[v] = min(thresholds[v], dist_sq_of(stars[u], stars[v]))

print('%.2f' % mst_wegiht)
