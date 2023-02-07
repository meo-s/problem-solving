# https://www.acmicpc.net/problem/1473

import sys
from collections import deque


def readline():
    return sys.stdin.readline().rstrip()


def rotate_walls(state, y, x):
    state ^= 1 << (y + 7)
    state ^= 1 << x
    return state


def local_state_of(state, y, x):
    return ((state >> (y + 7)) ^ (state >> x)) & 1


MOVABLE_DOOR_STATE = {-10: 0b1010, 10: 0b1010, -1: 0b0101, 1: 0b0101}
DOOR = [  # UDLR
    [0b1111, 0b1111],  # 0: A
    [0b0000, 0b0000],  # 1: B
    [0b1010, 0b0101],  # 2: C
    [0b0101, 0b1010],  # 3: D
]

H, W = map(int, readline().split())
miro = [[*map(lambda c: ord(c) - ord('A'), readline())] for _ in range(H)]

visited = [[set() for _ in range(W)] for _ in range(H)]
visited[0][0].add(0)

min_dist = -1
waypoints = deque([(0, 0, 0, 0)])  # y, x, state, dist
while 0 < len(waypoints):
    y, x, state, dist = waypoints.popleft()
    if y == H - 1 and x == W - 1:
        min_dist = dist
        break

    rotated_state = rotate_walls(state, y, x)
    if rotated_state not in visited[y][x]:
        visited[y][x].add(rotated_state)
        waypoints.append((y, x, rotated_state, dist + 1))

    local_state = local_state_of(state, y, x)
    for dy, dx in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
        if not (0 <= y + dy < H and 0 <= x + dx < W):
            continue

        if state in visited[y + dy][x + dx]:
            continue

        dlocal_state = local_state_of(state, y + dy, x + dx)
        overlapped_door = (DOOR[miro[y][x]][local_state] & DOOR[miro[y + dy][x + dx]][dlocal_state])
        if (overlapped_door & MOVABLE_DOOR_STATE[dy * 10 + dx]) == 0:
            continue

        visited[y + dy][x + dx].add(state)
        waypoints.append((y + dy, x + dx, state, dist + 1))

print(min_dist)
