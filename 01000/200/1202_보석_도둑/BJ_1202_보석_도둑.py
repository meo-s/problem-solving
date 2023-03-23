# https://www.acmicpc.net/problem/1202

import sys
from operator import itemgetter
from heapq import heappush
from heapq import heappop

readline = lambda: sys.stdin.readline().rstrip()  # noqa

N, K = map(int, readline().split())
jewels = sorted([[*map(int, readline().split())] for _ in range(N)], key=itemgetter(0), reverse=True)
backpacks = sorted([int(readline()) for _ in range(K)], reverse=True)

max_value = 0
availables = []
while 0 < len(backpacks):
    threshold = backpacks.pop()
    while 0 < len(jewels) and jewels[-1][0] <= threshold:
        _, value = jewels.pop()
        heappush(availables, -value)

    max_value += -heappop(availables) if 0 < len(availables) else 0

print(max_value)
