# https://www.acmicpc.net/problem/2141

import sys

readline = lambda: sys.stdin.readline().strip()

villages = [tuple(map(int, readline().split())) for _ in range(int(readline()))]
villages.sort(key=lambda v: v[0])

x = 0
local_sum = villages[x][1]
partial_local_sum = villages[x][1]
for i in range(1, len(villages)):
    local_sum += villages[i][1]
    while 2 * partial_local_sum < local_sum:
        partial_local_sum += villages[(x := x + 1)][1]

print(villages[x][0])
