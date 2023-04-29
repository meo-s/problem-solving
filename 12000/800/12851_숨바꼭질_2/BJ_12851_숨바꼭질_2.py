# https://www.acmicpc.net/problem/12851

from collections import deque

LIMIT = 100_001
N, GOAL = map(int, input().split())

logs = [[float('inf'), 0] for _ in range(LIMIT)]
logs[N][0] = 0
logs[GOAL][1] = int(N == GOAL)

points = deque([N])
while logs[GOAL][1] == 0:
    for _ in range(len(points)):
        x = points.popleft()
        for dx in [-1, 1, x]:
            if not 0 <= x + dx < LIMIT:
                continue
            if logs[x + dx][0] < logs[x][0] + 1:
                continue
            logs[x + dx][0] = logs[x][0] + 1
            logs[x + dx][1] += 1
            points.append(x + dx)

print(*logs[GOAL], sep='\n')
