# https://www.acmicpc.net/problem/6064

import sys

readline = lambda: sys.stdin.readline().strip()

for _ in range(int(readline())):
    M, N, x, y = map(int, readline().split())
    x, y = x % M, y % N

    if M < N:
        M, N = N, M
        x, y = y, x

    ans = 0
    for n in range(x if 0 < x else M, M * N + 1, M):
        if n % N == y:
            ans = n
            break

    print([-1, n][0 < ans])
