# https://www.acmicpc.net/problem/2667

import itertools
import sys
import collections

N = int(sys.stdin.readline().strip())

def propagate(m, xy, v):
    waypoints = collections.deque([xy])
    while 0 < len(waypoints):
        x, y = waypoints.popleft()
        for dx, dy in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            if (0 <= x + dx < N) and (0 <= y + dy < N):
                if m[y + dy][x + dx] == 1:
                    m[y + dy][x + dx] = v
                    waypoints.append((x + dx, y + dy))

n_complexs = 1
complexs = [[int(c) for c in sys.stdin.readline().strip()] for _ in range(N)]
for y in range(N):
    for x in range(N):
        if complexs[y][x] == 1:
            complexs[y][x] = (n_complexs := n_complexs + 1)
            propagate(complexs, (x, y), n_complexs)

counter = collections.Counter(itertools.chain(*complexs))
del counter[0]

print(len(counter))
for v in sorted(counter.values()):
    print(v)
