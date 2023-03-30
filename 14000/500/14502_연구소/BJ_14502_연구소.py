# https://www.acmicpc.net/problem/14502

import sys
import itertools


def propagate(lab, points):
    n_propagated = 0
    visited = [[False] * len(lab[0]) for _ in range(len(lab))]
    while 0 < len(points):
        y, x = points.pop()
        for dy, dx in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
            if not (0 <= y + dy < H and 0 <= x + dx < W):
                continue
            if visited[y + dy][x + dx]:
                continue
            if lab[y + dy][x + dx] != 0:
                continue
            n_propagated += 1
            visited[y + dy][x + dx] = True
            points.append((y + dy, x + dx))

    return n_propagated


readline = lambda: sys.stdin.readline().strip()

H, W = map(int, readline().split())
lab = [[*map(int, readline().split())] for _ in range(H)]

n_airs = 0
viruses = []
for y in range(H):
    for x, n in enumerate(lab[y]):
        lab[y][x] = n
        n_airs += int(n == 0)
        viruses.append((y, x)) if n == 2 else None

max_safe_regions = 0
for walls in itertools.combinations(range(H * W), 3):
    if not all(lab[wall // W][wall % W] == 0 for wall in walls):
        continue

    for wall in walls:
        lab[wall // W][wall % W] = 1

    max_safe_regions = max(max_safe_regions, n_airs - (3 + propagate(lab, [*viruses])))

    for wall in walls:
        lab[wall // W][wall % W] = 0

print(max_safe_regions)
