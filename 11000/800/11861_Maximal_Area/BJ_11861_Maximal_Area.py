# https://www.acmicpc.net/problem/11861

from itertools import chain
from collections import namedtuple

Bar = namedtuple('Bar', ['x', 'h'])

st = []
area = 0
input()  # N
for i, h in enumerate(chain(map(int, input().split()), [0])):
    x0 = i
    while 0 < len(st) and h < st[-1].h:
        area = max(area, (i - st[-1].x) * st[-1].h)
        x0 = st.pop().x

    if len(st) == 0 or st[-1].h < h:
        st.append(Bar(x0, h))

print(area)
