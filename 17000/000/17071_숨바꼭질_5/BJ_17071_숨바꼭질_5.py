# https://www.acmicpc.net/problem/17071

from collections import deque

LIMIT = 500_001
x, y = map(int, input().split())

dists = [[-1, -1] for _ in range(LIMIT)]  # 0 even, 1 odd
dists[x][0] = 0

points = deque([(x, 0)])
while 0 < len(points):
    x, dist = points.popleft()
    for dx in [-1, 1, x]:
        if 0 <= x + dx < LIMIT:
            if dists[x + dx][dist & 1 ^ 1] < 0:
                dists[x + dx][dist & 1 ^ 1] = dist + 1
                points.append((x + dx, dist + 1))

found = False
sec = -1
while not found and (y := y + (sec := sec + 1)) < LIMIT:
    if sec in dists[y] or (0 <= dists[y][sec & 1] < sec):
        found = True
        continue

    for dy in [-1, 1, -y // 2][:3 - (y % 2)]:
        if not 0 <= y + dy < LIMIT:
            continue
        if 0 <= dists[y + dy][sec & 1 ^ 1] < sec:
            found = True
            break

print([-1, sec][found])
