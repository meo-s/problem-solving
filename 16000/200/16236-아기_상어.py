# https://www.acmicpc.net/problem/16236

import sys
from collections import deque

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
sea = [[0] * N for _ in range(N)]


def eat(x, y, level):
    seconds = [[-1] * N for _ in range(N)]
    seconds[y][x] = 0

    prey = None
    candidates = deque([(x, y)])
    while 0 < len(candidates) and prey is None:
        for _ in range(len(candidates)):
            x, y = candidates.popleft()
            for dx, dy in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
                if (not 0 <= x + dx < N) or (not 0 <= y + dy < N):
                    continue
                if level < sea[y][x] or 0 <= seconds[y + dy][x + dx]:
                    continue

                seconds[y + dy][x + dx] = seconds[y][x] + 1
                if not 0 < sea[y + dy][x + dx] < level:
                    candidates.append((x + dx, y + dy))
                else:
                    if prey is not None:
                        px, py = prey
                        if (py < y + dy) or (py == y + dy and px < x + dx):
                            continue

                    prey = x + dx, y + dy

    if prey is not None:
        px, py = prey
        sea[py][px] = 0
        return px, py, seconds[py][px]
    else:
        return None, None, 0


baby_shark = None
for y in range(N):
    for x, n in enumerate(map(int, readline().split())):
        if n == 9:
            baby_shark = (x, y)
        else:
            sea[y][x] = n

score = 0
level, n_exp = 2, 0
while True:
    *baby_shark, n_seconds = eat(*baby_shark, level)
    if n_seconds == 0:
        break

    n_exp += 1
    level, n_exp = level + (n_exp // level), n_exp % level
    score += n_seconds

print(score)
