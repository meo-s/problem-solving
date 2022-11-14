# https://www.acmicpc.net/problem/6064

import sys
readline = lambda: sys.stdin.readline().strip()


for _ in range(int(readline())):
    M, N, x, y = map(int, readline().split())

    nth = 1
    a, b = 1, 1
    while a != x or b != y:
        nth += 1
        a = max(1, (a + 1) % (M + 1))
        b = max(1, (b + 1) % (N + 1))

    print(nth)
