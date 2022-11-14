# https://www.acmicpc.net/problem/11399

import sys
readline = lambda: sys.stdin.readline().strip()


N = int(readline())
times = sorted([*map(int, readline().split())])
print(sum(sum(times[:i + 1]) for i in range(N)))
