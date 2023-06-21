# https://www.acmicpc.net/problem/2630

import sys
from itertools import chain

readline = lambda: sys.stdin.readline().strip()

color_papers = [0, 0]
paper = []
for _ in range(N := int(readline())):
    paper.append([*map(int, readline().split())])


def break_down(x1, y1, x2, y2):
    if len(colors := set(chain(*[paper[y][x1:x2] for y in range(y1, y2)]))) == 1:
        color_papers[colors.pop()] += 1
    else:
        x_mid = (x1 + x2) // 2
        y_mid = (y1 + y2) // 2
        break_down(x1, y1, x_mid, y_mid)
        break_down(x_mid, y1, x2, y_mid)
        break_down(x1, y_mid, x_mid, y2)
        break_down(x_mid, y_mid, x2, y2)


break_down(0, 0, N, N)
print(*color_papers, sep='\n')
