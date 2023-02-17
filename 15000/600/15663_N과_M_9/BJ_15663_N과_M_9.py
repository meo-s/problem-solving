# https://www.acmicpc.net/problem/15663

import sys


def pick(seq, N, M, indices=[]):
    if len(indices) == M:
        print(*[seq[i] for i in indices], sep=' ')
        return

    i = -1
    while i < N - 1:
        if (i := i + 1) in indices:
            continue

        indices.append(i)
        pick(seq, N, M, indices)
        indices.pop()

        while i < N - 1 and seq[i] == seq[i + 1]:
            i += 1


readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
seq = sorted([*map(int, readline().split())])
pick(seq, N, M)
