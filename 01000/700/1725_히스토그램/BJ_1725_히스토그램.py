# https://www.acmicpc.net/problem/1725

import sys
from collections import namedtuple

Bar = namedtuple('Bar', ['x', 'h'])
readline = lambda: sys.stdin.readline().rstrip()  # noqa
N = int(readline())

st = []
max_area = 0
for i, h in enumerate([int(readline()) for _ in range(N)] + [0]):
    min_x = i
    while 0 < len(st) and h < st[-1].h:
        min_x, cur_h = st.pop()
        max_area = max(max_area, (i - min_x) * cur_h)

    st.append(Bar(min_x, h)) if 0 < h else None

print(max_area)
