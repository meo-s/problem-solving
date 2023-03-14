# https://www.acmicpc.net/problem/1011

import math
import sys

readline = lambda: sys.stdin.readline().strip()
for _ in range(int(readline())):
    x, y = map(int, readline().split())
    max_v = math.floor(-0.5 + math.sqrt(0.25 + (y - x)))
    remains = (y - x) - max_v * (max_v + 1)
    print(2 * max_v + ((1 if remains <= max_v + 1 else 2) if 0 < remains else 0))
