# https://www.acmicpc.net/problem/15657

import sys


def pick(seq, N, M, indices=[]):
    if len(indices) == M:
        print(' '.join(map(str, [seq[i] for i in indices])))
        return

    for i in range(indices[-1] if 0 < len(indices) else 0, N):
        if 0 < len(indices) and i < indices[-1]:
            break

        indices.append(i)
        pick(seq, N, M, indices)
        indices.pop()


readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())
seq = sorted([*map(int, readline().split())])
pick(seq, N, M)
