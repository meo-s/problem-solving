# https://www.acmicpc.net/problem/1826

import sys
from collections import deque
from heapq import heappop
from heapq import heappush
from operator import itemgetter

readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
gas_stations = deque(sorted([[*map(int, readline().split())] for _ in range(N)], key=itemgetter(0)))
L, P = map(int, readline().split())

x, n_stops = 0, 0
availables = []
while P != 0 or 0 < len(availables):
    x, P = x + P, 0
    if L <= x:
        break

    while 0 < len(gas_stations) and gas_stations[0][0] <= x:
        _, oil = gas_stations.popleft()
        heappush(availables, -oil)

    P += -heappop(availables) if 0 < len(availables) else 0
    n_stops += 1

print([-1, n_stops][L <= x])
