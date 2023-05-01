# https://www.acmicpc.net/problem/20157

import sys
from collections import defaultdict


def gcd(a, b):
    while (r := a % b) != 0:
        a = b
        b = r
    return b


readline = lambda: sys.stdin.readline().rstrip()  # noqa

counter = defaultdict(int)
for _ in range(int(readline())):
    x, y = map(int, readline().split())
    z = gcd(abs(x), abs(y)) if x != 0 and y != 0 else max(abs(x), abs(y))
    counter[(x // z, y // z)] += 1

print(max(counter.values()))
