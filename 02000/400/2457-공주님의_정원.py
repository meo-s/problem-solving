# https://www.acmicpc.net/problem/2457

import sys
import operator
from collections import deque

readline = lambda: sys.stdin.readline().rstrip()

N = int(readline())
flowers = [None] * N
for i in range(N):
    beg_mon, beg_day, end_mon, end_day = map(int, readline().split())
    flowers[i] = (beg_mon * 10**2 + beg_day, end_mon * 10**2 + end_day)

flowers.sort(key=operator.itemgetter(0))

end_day = 301
flowers = deque(flowers)
n_flowers = 0
while end_day < 1201:
    next_end_day = end_day
    while 0 < len(flowers) and flowers[0][0] <= end_day:
        next_end_day = max(next_end_day, flowers.popleft()[1])

    if next_end_day == end_day:
        break

    end_day = next_end_day
    n_flowers += 1

print([0, n_flowers][1201 <= end_day])
