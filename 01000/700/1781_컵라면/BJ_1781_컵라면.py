# https://www.acmicpc.net/problem/1781

import sys
from heapq import heappop
from heapq import heappush
from operator import itemgetter

readline = lambda: sys.stdin.readline().rstrip()  # noqa

problems = [tuple(map(int, readline().split())) for _ in range(int(readline()))]
problems.sort(key=itemgetter(0))

day = problems[-1][0]
n_noddles = 0
availables = []
while 0 < day:
    while 0 < len(problems) and problems[-1][0] == day:
        heappush(availables, -problems.pop()[-1])

    if 0 < len(availables):
        n_noddles += -heappop(availables)

    day -= 1

print(n_noddles)
