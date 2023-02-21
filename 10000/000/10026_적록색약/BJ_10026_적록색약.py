# https://www.acmicpc.net/problem/10026

import sys
from collections import defaultdict, deque

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
screen = []
for _ in range(N):
    screen.append([*readline()])


def count_sections(sections):
    n_sections = 0

    visited = [[False] * N for _ in range(N)]
    for h in range(N):
        for w in range(N):
            if visited[h][w]:
                continue
            n_sections += 1
            visited[h][w] = True
            initial = screen[h][w]

            coords = [(h, w)]
            while 0 < len(coords):
                y, x = coords.pop()
                for dy, dx in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
                    if (not 0 <= y + dy < N) or (not 0 <= x + dx < N):
                        continue
                    if visited[y + dy][x + dx] or screen[y + dy][x + dx] not in sections[initial]:
                        continue
                    visited[y + dy][x + dx] = True
                    coords.append((y + dy, x + dx))

    return n_sections


for section in ['R=R;G=G;B=B', 'R=RG;G=RG;B=B']:
    section = dict([k.split('=') for k in section.split(';')])
    print(count_sections(section), end=' ')
