# https://www.acmicpc.net/problem/2885

import math
import sys
readline = lambda: sys.stdin.readline().strip()


target = int(readline())
print(D := 2 ** math.ceil(math.log2(target)), end=' ')

depth = 0
while target != D:
    D //= 2
    target -= D if D < target else 0
    depth += 1

print(depth)
