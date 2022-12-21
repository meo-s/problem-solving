# https://www.acmicpc.net/problem/11401

import sys


def power(n, k):
    M = power.M
    n %= M

    if k <= 2:
        return n**2 % M if k == 2 else n

    nxn = power(n, k // 2)
    nxn = nxn**2 % M
    return (nxn * n) % M if k % 2 == 1 else nxn


def factorial(n, stop=0):
    M = factorial.M

    n_factorial = 1
    for i in reversed(range(stop + 1, n + 1)):
        n_factorial = (n_factorial * (i % M)) % M

    return n_factorial


M = 1_000_000_007
power.M, factorial.M = M, M
readline = lambda: sys.stdin.readline().strip()

n, r = map(int, readline().split())
r = min(r, n - r)
print((factorial(n, n - r) * power(factorial(r), M - 2)) % M)
