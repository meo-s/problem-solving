# https://www.acmicpc.net/problem/18186

import sys


def buy(facs, costs, lb, ub, n):
    for i in range(lb, ub):
        facs[i] -= n
    return costs[ub - lb - 1] * n


readline = lambda: sys.stdin.readline().strip()  # noqa
N, B, C = map(int, readline().split())
facs = [*map(int, readline().split()), 0, 0]

if C < B:
    cost = 0
    costs = (B, B + C, B + 2 * C)
    for i in range(0, N):
        if 0 < (n := min(facs[i:i + 3])):
            if facs[i] < facs[i + 1] and facs[i + 2] < facs[i + 1]:  # 요철 모양 처리
                cost += buy(facs, costs, i, i + 2, min(facs[i], facs[i + 1] - facs[i + 2]))

        for sz_window in range(3, 0, -1):
            cost += buy(facs, costs, i, i + sz_window, n) if 0 < (n := min(facs[i:i + sz_window])) else 0

print(cost if C < B else sum(facs) * B)
