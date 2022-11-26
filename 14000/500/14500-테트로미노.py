# https://www.acmicpc.net/problem/14500

import sys

readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
scores = []
for _ in range(N):
    scores.append([*map(int, readline().split())])


def dfs(x, y, depth=0, dxy=(-1, -1), score=0):
    global N, M
    max_score = (score := score + scores[y][x])
    if depth < 3:
        for dx, dy in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
            if (not 0 <= x + dx < M) or (not 0 <= y + dy < N):
                continue
            if dxy == (-dx, -dy):
                continue
            max_score = max(max_score, dfs(x + dx, y + dy, depth + 1, (dx, dy), score))

    return max_score


def irregular(x, y):
    dxy = [(-1, 0), (0, 1), (1, 0), (0, -1), (-1, 0), (0, 1)]
    max_score = 0
    for i in range(4):
        score = scores[y][x]
        for dx, dy in dxy[i:i + 3]:
            if (not 0 <= x + dx < M) or (not 0 <= y + dy < N):
                score = -1
                break
            score += scores[y + dy][x + dx]
        max_score = max(max_score, score)

    return max_score


max_score = 0
for y in range(N):
    for x in range(M):
        max_score = max(max_score, dfs(x, y), irregular(x, y))

print(max_score)
