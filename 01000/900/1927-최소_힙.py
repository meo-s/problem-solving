# https://www.acmicpc.net/problem/1927

import sys
from heapq import heappush, heappop

readline = lambda: sys.stdin.readline().strip()

heap = []
for _ in range(int(readline())):
    arg = int(readline())
    if arg == 0:
        print(heappop(heap) if 0 < len(heap) else 0)
    else:
        heappush(heap, arg)
