# https://www.acmicpc.net/problem/2407

import functools
import sys

readline = lambda: sys.stdin.readline().strip()


@functools.lru_cache()
def fact(n, lb=1):
    return n * fact(n - 1, lb) if lb < n else lb


def nCr(n, r):
    r = max(r, n - r)
    return fact(n, r + 1) // fact(n - r)


print(nCr(*map(int, input().split())))
