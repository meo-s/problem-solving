# https://www.acmicpc.net/problem/1010

import sys
import functools


@functools.lru_cache()
def dfs(N, M, depth=0, offset=0):
    if N == depth:
        return 1

    n_cases = 0
    for i in range(offset, M - (N - depth) + 1):
        n_cases += dfs(N, M, depth + 1, i + 1)

    return n_cases


readline = lambda: sys.stdin.readline().strip()
for _ in range(int(readline())):
    print(dfs(*map(int, readline().split())))
