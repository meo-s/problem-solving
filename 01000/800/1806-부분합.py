# https://www.acmicpc.net/problem/1806

import sys

INF = float('inf')
readline = lambda: sys.stdin.readline().strip()

N, S = map(int, readline().split())
seq = [*map(int, readline().split())]

partial_len = INF
partial_sum = 0
lb, ub = 0, -1
while ub < len(seq) - 1:
    partial_sum += seq[(ub := ub + 1)]
    while lb < ub and S <= partial_sum - seq[lb]:
        partial_sum -= seq[lb]
        lb += 1

    partial_len = min(partial_len, ub - lb + 1) if S <= partial_sum else partial_len

print([0, partial_len][partial_len != INF])
