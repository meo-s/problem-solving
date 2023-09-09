# https://www.acmicpc.net/problem/29791

import sys


def simulate(history, cooldown):
    n_binds = 1
    prev_at = history[0]
    for cur in history[1:]:
        if cooldown <= cur - prev_at:
            prev_at = cur
            n_binds += 1

    return n_binds


readline = lambda: sys.stdin.readline().rstrip()
readline()
print(simulate(sorted(map(int, readline().split())), 100), end=' ')
print(simulate(sorted(map(int, readline().split())), 360))
