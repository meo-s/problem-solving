# https://www.acmicpc.net/problem/6549

import sys
import itertools
from collections import namedtuple


def readline():
    return sys.stdin.readline().rstrip()


Bar = namedtuple('Bar', ['x', 'h'])
while 0 < (histogram := [*map(int, readline().split())])[0]:
    N, *bars = histogram

    st = [Bar(0, -1)]
    largest_area = 0
    for x, h in enumerate(itertools.chain(bars, [0]), start=1):
        x0 = x
        while h < st[-1].h:
            largest_area = max(largest_area, st[-1].h * (x - st[-1].x))
            x0 = st.pop().x

        st.append(Bar(x0, h))

    print(largest_area)
