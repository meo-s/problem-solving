# https://www.acmicpc.net/problem/11000

import sys
import heapq

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
classes = [tuple(map(int, readline().split())) for _ in range(N)]
classes.sort(key=lambda v: v[1])
classes.sort(key=lambda v: v[0])

rooms = [0]
for begin, end in classes:
    if rooms[0] <= begin:
        heapq.heapreplace(rooms, end)
    else:
        heapq.heappush(rooms, end)

print(len(rooms))
