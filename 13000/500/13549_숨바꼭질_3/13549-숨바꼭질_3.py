# https://www.acmicpc.net/problem/13549

import math
from collections import deque

LIMIT = 100001
N, K = map(int, input().split())

dists = [-1] * LIMIT
dists[N] = 0
points = deque([N])
while dists[K] < 0:
    x = points.popleft()
    for x_next, dist in [(2 * x, dists[x]), (x - 1, dists[x] + 1), (x + 1, dists[x] + 1)]:
        if 0 <= x_next < LIMIT:
            if dists[x_next] < 0:
                dists[x_next] = dist
                [points.appendleft, points.append][dists[x] < dist](x_next)

print(dists[K])

'''
N, K = map(int, input().split())

dists = [-1] * ((MAX_X := 100_000) + 1)
dists[N] = 0

points = deque([N])
while 0 < len(points) and dists[K] < 0:
    x = points.popleft()

    if 0 < x:
        y = int(math.log2(MAX_X // x)) + 1
        while 0 < (y := y - 1):
            if dists[x * 2**y] < 0:
                dists[x * 2**y] = dists[x]
                points.appendleft(x * 2**y)

    for dx in [-1, 1]:
        if 0 <= x + dx <= MAX_X:
            if dists[x + dx] < 0:
                dists[x + dx] = dists[x] + 1
                points.append(x + dx)

print(dists[K])
'''
