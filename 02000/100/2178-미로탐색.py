# https://www.acmicpc.net/problem/2178

from collections import deque

N, M = tuple(map(int, input().strip().split(' ')))
maze = []
for _ in range(N):
    maze.append([*map(int, input().strip())])

waypoints = deque([(0, 0)])
while maze[-1][-1] == 1:
    x, y = waypoints.popleft()
    for dx, dy in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
        if (0 <= x + dx < M) and (0 <= y + dy < N):
            if maze[y + dy][x + dx] == 1:
                maze[y + dy][x + dx] += maze[y][x]
                waypoints.append((x + dx, y + dy))

print(maze[-1][-1])
