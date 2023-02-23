# https://www.acmicpc.net/problem/5427

import sys
from collections import deque

readline = lambda: sys.stdin.readline().strip()

for _ in range(int(readline())):
    W, H = map(int, readline().split())
    building = []
    fires = deque([])
    sanggeuns = deque([])
    for h in range(H):
        building.append([None] * W)
        for w, c in enumerate(readline()):
            building[h][w] = c if c != '@' else '.'
            fires.append((h, w)) if c == '*' else None
            sanggeuns.append((h, w)) if c == '@' else None

    done = False
    n_seconds = 0
    visited = [[False] * W for _ in range(H)]
    while not done and 0 < len(sanggeuns):
        n_seconds += 1

        for _ in range(len(fires)):
            h, w = fires.popleft()
            for dh, dw in [(1, 0), (-1, 0), (0, 1,), (0, -1)]:
                if (not 0 <= h + dh < H) or (not 0 <= w + dw < W):
                    continue
                if building[h + dh][w + dw] == '.':
                    building[h + dh][w + dw] = '*'
                    fires.append((h + dh, w + dw))

        for _ in range(len(sanggeuns)):
            h, w = sanggeuns.popleft()
            if h in [0, H - 1] or w in [0, W - 1]:
                done = True
                break

            for dh, dw in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
                if (not 0 <= h + dh < H) or (not 0 <= w + dw < W):
                    continue
                if building[h + dh][w + dw] == '.' and not visited[h + dh][w + dw]:
                    visited[h + dh][w + dw] = True
                    sanggeuns.append((h + dh, w + dw))

    print(['IMPOSSIBLE', n_seconds][done])
