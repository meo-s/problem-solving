# https://www.acmicpc.net/problem/1992

import itertools

N = int(input())
img = [tuple(int(c) for c in input()) for _ in range(N)]

compressed = ''
sections = [(0, 0, N, N)]
while 0 < len(sections):
    x, y, w, h = sections.pop()
    if w * h == 0:
        compressed += ')'
        continue

    partial_img = set(sum(itertools.chain(img[row][x:x + w] for row in range(y, y + h)), tuple()))
    if len(partial_img) != 1:
        hw, hh = w // 2, h // 2
        sections.append((0, 0, 0, 0))  # right bottom
        sections.append((x + hw, y + hh, hw, hh))  # right bottom
        sections.append((x, y + hh, hw, hh))  # left bottom
        sections.append((x + hw, y, hw, hh))  # right top
        sections.append((x, y, hw, hh))  # left top
        compressed += '('
    else:
        compressed += str(partial_img.pop())

print(compressed)
