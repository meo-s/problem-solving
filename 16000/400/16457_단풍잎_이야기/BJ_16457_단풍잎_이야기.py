# https://www.acmicpc.net/problem/16457

import sys
from itertools import combinations


def to_bitmask(bits):
    bitmask = 0
    for bit in bits:
        bitmask |= 1 << bit
    return bitmask


readline = lambda: sys.stdin.readline().rstrip()  # noqa
N, M, K = map(int, readline().split())
S = [to_bitmask(map(int, readline().split())) for _ in range(M)]

max_clearables = 0
for picks in combinations(range(1, 2 * N + 1), N):
    usable = to_bitmask(picks)
    max_clearables = max(max_clearables, sum((s & usable) == s for s in S))

print(max_clearables)
