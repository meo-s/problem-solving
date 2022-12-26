# https://www.acmicpc.net/problem/10165

import sys
import operator

readline = lambda: sys.stdin.readline().strip()

N, M = int(readline()), int(readline())

plans = []
for i in range(1, M + 1):
    beg, end = map(int, readline().split())
    beg = [beg, -N + beg][end < beg]
    plans.append((beg, end, i))

plans.sort(key=operator.itemgetter(1), reverse=True)
plans.sort(key=operator.itemgetter(0))

routes = []
for begi, endi, i in plans:
    is_overlapped = False

    for range_, begk, endk in [(reversed(range(len(routes))), begi, endi), \
                               (range(len(routes)), -N + begi, -N + endi)]:
        if not is_overlapped:
            for j in range_:
                begj, endj, _ = routes[j]
                if (begj <= begk < endj) and (endk <= endj):
                    is_overlapped = True
                if is_overlapped or endj <= begk:
                    break

    if not is_overlapped:
        routes.append((begi, endi, i))

routes.sort(key=operator.itemgetter(-1))
print(' '.join(map(str, (route[-1] for route in routes))))
