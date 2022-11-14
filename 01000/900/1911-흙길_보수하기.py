# https://www.acmicpc.net/problem/1911

import math
import sys
readline = lambda: sys.stdin.readline().strip()

N, L = map(int, readline().split())
puddles = [tuple(map(int, readline().split())) for _ in range(N)]
puddles.sort(key=lambda v: v[0])

x = 0
n_planks = 0
for begin, end in puddles:
    x = max(x, begin)
    n_local_planks = math.ceil((end - x) / L)
    n_planks += n_local_planks
    x += n_local_planks * L

print(n_planks)
