# https://www.acmicpc.net/problem/9461

import sys

readline = lambda: sys.stdin.readline().strip()

padovan = [1, 1, 1, 2, 2]
for _ in range(101 - len(padovan)):
    padovan.append(padovan[-1] + padovan[-5])

for _ in range(int(readline())):
    print(padovan[int(readline()) - 1])
