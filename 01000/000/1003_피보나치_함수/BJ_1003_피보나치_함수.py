# https://www.acmicpc.net/problem/1003

import sys

readline = lambda: sys.stdin.readline().strip()

fibs = [(1, 0), (0, 1)]
for i in range(2, 41):
    fibs.append(tuple(fibs[-2][j] + fibs[-1][j] for j in range(2)))

for _ in range(int(readline())):
    print(*fibs[int(readline())], sep=' ')
