# https://www.acmicpc.net/problem/4673

import sys

readline = lambda: sys.stdin.readline().strip()

is_self = [True] * 10001
is_self[0] = False
for i in range(1, len(is_self)):
    n, j = i, i
    while 0 < j:
        n += j % 10
        j //= 10

    if n < len(is_self):
        is_self[n] = (n == i)

for i in range(len(is_self)):
    print(i) if is_self[i] else None
