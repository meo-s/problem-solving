# https://www.acmicpc.net/problem/15666

import sys


def pick(seq, N, M, offset=0, indices=[]):
    if len(indices) == M:
        print(*[seq[i] for i in indices], sep=' ')
        return

    for i in range(offset, N):
        indices.append(i)
        pick(seq, N, M, i, indices)
        indices.pop()


readline = lambda: sys.stdin.readline().strip()
_, M = map(int, readline().split())
seq = sorted(set([*map(int, readline().split())]))
pick(seq, len(seq), M)
