# https://www.acmicpc.net/problem/13172

import sys


def pow(n, k, M):
    n %= M
    if k <= 2:
        return n**2 % M if k == 2 else n

    nxn = pow(pow(n, k // 2, M), 2, M)
    return (nxn * n) % M if k % 2 == 1 else nxn


M = 1_000_000_007
readline = lambda: sys.stdin.readline().strip()

Q = 0
for _ in range(int(readline())):
    N, S = map(int, readline().split())
    Q = (Q + (S * pow(N, M - 2, M)) % M) % M

print(Q)
