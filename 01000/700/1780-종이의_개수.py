# https://www.acmicpc.net/problem/1780

import sys
from itertools import chain

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
paper = [[*map(int, readline().split())] for _ in range(N)]


def break_down(x1, x2, y1, y2, counts=[0, 0, 0]):
    if len(elements := set(chain(*[paper[i][x1:x2] for i in range(y1, y2)]))) == 1:
        counts[elements.pop() + 1] += 1
    else:
        sz = (y2 - y1) // 3
        for yy in range(y1, y2, sz):
            for xx in range(x1, x2, sz):
                break_down(xx, xx + sz, yy, yy + sz, counts)

    return counts

print(*break_down(0, N, 0, N), sep='\n')
