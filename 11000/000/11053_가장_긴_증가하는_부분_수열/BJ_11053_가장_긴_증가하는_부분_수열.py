# https://www.acmicpc.net/problem/11053

import sys
from bisect import bisect_left

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
seq = [*map(int, readline().split())]

lis = [seq[0]]
for n in seq[1:]:
    if lis[-1] < n:
        lis.append(n)
    else:
        lis[bisect_left(lis, n, 0, len(lis) - 1)] = n

print(len(lis))
