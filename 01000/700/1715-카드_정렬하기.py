# https://www.acmicpc.net/problem/1715

import sys
from heapq import heapify, heappop, heapreplace

readline = lambda: sys.stdin.readline().strip()

heapify(chunks := [int(readline()) for _ in range(int(readline()))])

total_comps = 0
while 1 < len(chunks):
    heapreplace(chunks, (new_chunk := heappop(chunks) + chunks[0]))
    total_comps += new_chunk

print(total_comps)
