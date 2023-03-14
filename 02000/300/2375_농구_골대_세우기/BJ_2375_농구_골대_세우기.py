# https://www.acmicpc.net/problem/2375

import sys

readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())

xx, yy = [], []
for _ in range(N):
    x, y, n = map(int, readline().split())
    xx += [x] * n
    yy += [y] * n

xx.sort()
yy.sort()

x_mid = xx[max(0, len(xx) // 2 - [0, 1][(len(xx) % 2) ^ 1])]
y_mid = yy[max(0, len(yy) // 2 - [0, 1][(len(xx) % 2) ^ 1])]
print(x_mid, y_mid, sep=' ')
