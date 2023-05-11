# https://www.acmicpc.net/problem/2448

import sys

readline = lambda: sys.stdin.readline().strip()


def tri(h, x_offset=0, y_offset=0, paper=None, reverse=False):
    if 0 < h:
        if paper == None:
            paper = [[' '] * (2 * h) for _ in range(h)]

        if not reverse:
            for y in range(h):
                for x in range(h - (y + 1), h + y):
                    paper[y][x] = '*'
            tri(h // 2, h - 1, h - 1, paper, reverse=True)
        else:
            y_offset -= 1 if h == 1 else 0

            for y in range(h):
                for x in range(-y, y + 1):
                    paper[y_offset - y][x_offset + x] = ' '

            tri(h // 2, x_offset - h, y_offset, paper, reverse=True)
            tri(h // 2, x_offset + h, y_offset, paper, reverse=True)
            tri(h // 2, x_offset, y_offset - h, paper, reverse=True)

    return paper


print(*map(''.join, tri(int(input()))), sep='\n')
