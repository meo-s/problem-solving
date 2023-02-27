# https://www.acmicpc.net/problem/2261

import sys
import operator
import math


def update_min_dist(x, min_dist):
    if 1 < len(pt[x]):
        min_dist = min(min_dist, (pt[x][-1] - pt[x][-2])**2)

    dx = 0
    while (dx := dx + 1) < math.ceil(math.sqrt(min_dist)):
        for nx in [x - dx, x + dx]:
            if 0 <= nx < len(pt) and 0 < len(pt[nx]):
                min_dist = min(min_dist, (pt[nx][-1] - pt[x][-1])**2 + dx**2)

    return min_dist


readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
pt = [[] for _ in range(20001)]
pendings = []
for _ in range(N):
    x, y = map(int, readline().split())
    pendings.append((x + 10000, y))

pendings.sort(key=operator.itemgetter(1))
for x, y in pendings:
    pt[x].append(y)

min_dist = sys.maxsize
for x, _ in reversed(pendings):
    min_dist = update_min_dist(x, min_dist)
    pt[x].pop()

print(min_dist)
