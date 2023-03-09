# https://www.acmicpc.net/problem/3043

import sys
from operator import itemgetter

readline = lambda: sys.stdin.readline().rstrip()  # noqa

Y, X = 1, 2
N = int(input())
tanks = [[i + 1, *map(int, readline().split())] for i in range(N)]

movements = []

# Y축 배치
tanks.sort(key=itemgetter(Y))
i = N
for dst in reversed(range(1, N + 1)):
    tank_id, y, _ = tanks[(i := i - 1)]
    if 0 < dst - y:
        movements += [f'{tank_id} D'] * (dst - y)
i = -1
for dst in range(1, N + 1):
    tank_id, y, _ = tanks[(i := i + 1)]
    if 0 < y - dst:
        movements += [f'{tank_id} U'] * (y - dst)

# X축 배치
tanks.sort(key=itemgetter(X))
i = -1
for dst in range(1, N + 1):
    tank_id, _, x = tanks[(i := i + 1)]
    if 0 < dst - x:
        movements += [f'{tank_id} R'] * (dst - x)
    elif 0 < x - dst:
        movements += [f'{tank_id} L'] * (x - dst)

print(len(movements))
print(*movements, sep='\n')
