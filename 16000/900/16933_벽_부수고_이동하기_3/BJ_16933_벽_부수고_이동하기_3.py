import sys
from collections import deque


def readline():
    return sys.stdin.readline().rstrip()


INF = float('inf')
H, W, K = map(int, readline().split())
room = [[*map(int, readline())] for _ in range(H)]

visited = [[INF] * W for _ in range(H)]
visited[0][0] = 1
dist = [[[INF] * (K + 1) for _ in range(W)] for _ in range(H)]
dist[0][0][0] = 1

pendings, steps = deque([(0, 0, 0, False)]), deque([])
while min(dist[-1][-1]) == INF and 0 < len(pendings) + len(steps):
    while 0 < len(pendings):
        y, x, k, night = pendings.popleft()
        for dy, dx in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
            if not (0 <= y + dy < H and 0 <= x + dx < W):
                continue

            if room[y + dy][x + dx] == 0:
                if k < visited[y + dy][x + dx]:
                    visited[y + dy][x + dx] = k
                    steps.append((y + dy, x + dx, k, night, dist[y][x][k] + 1))
            elif k < K:
                if k + 1 < visited[y + dy][x + dx]:
                    visited[y + dy][x + dx] = k + 1
                    steps.append((y + dy, x + dx, k + 1, night, dist[y][x][k] + 1))

    for _ in range(len(steps)):
        y, x, k, night, new_dist = steps.popleft()
        if room[y][x] == 1 and night:
            steps.append((y, x, k, not night, new_dist + 1))
        else:
            if new_dist < dist[y][x][k]:
                dist[y][x][k] = new_dist
                pendings.append((y, x, k, not night))

min_dist = min(dist[-1][-1])
print([-1, min_dist][min_dist != INF])
