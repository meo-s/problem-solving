# https://www.acmicpc.net/problem/18185

import sys


def buy(facs, lb, ub, n):
    for i in range(lb, ub):
        facs[i] -= n
    return [3, 5, 7][ub - lb - 1] * n


readline = lambda: sys.stdin.readline().strip()
N = int(readline())
facs = [*map(int, readline().split()), 0, 0]

cost = 0
for i in range(0, N):
    if 0 < (n := min(facs[i:i + 3])):
        if facs[i] < facs[i + 1] and facs[i + 2] < facs[i + 1]:  # 요철 모양 처리
            cost += buy(facs, i, i + 2, min(facs[i], facs[i + 1] - facs[i + 2]))

    for sz_window in range(3, 0, -1):
        cost += buy(facs, i, i + sz_window, n) if 0 < (n := min(facs[i:i + sz_window])) else 0

print(cost)
