# https://www.acmicpc.net/problem/17136

import sys


def attachable(board, h, w, sz_paper):
    if (not h + sz_paper <= 10) or (not w + sz_paper <= 10):
        return False
    bitmask = (2**sz_paper - 1) << w
    return all(board[h + dh] & bitmask == bitmask for dh in range(sz_paper))


def attach(board, h, w, sz_paper):
    bitmask = (2**sz_paper - 1) << w
    for dh in range(sz_paper):
        board[h + dh] &= ~bitmask


def dettach(board, h, w, sz_paper):
    bitmask = (2**sz_paper - 1) << w
    for dh in range(sz_paper):
        board[h + dh] |= bitmask


def solve(board, h=0, w=0, remains=None):
    if remains is None:
        remains = [5] * 5
    if w == 10:
        return solve(board, h + 1, 0, remains)
    if h == 10:
        return 25 - sum(remains)
    if board[h] & (1 << w) == 0:
        return solve(board, h, w + 1, remains)

    min_papers = INF
    for sz_paper in range(1, len(remains) + 1):
        if 0 < remains[sz_paper - 1]:
            if attachable(board, h, w, sz_paper):
                remains[sz_paper - 1] -= 1
                attach(board, h, w, sz_paper)
                min_papers = min(min_papers, solve(board, h, w + 1, remains))
                remains[sz_paper - 1] += 1
                dettach(board, h, w, sz_paper)

    return min_papers


INF = float('inf')
readline = lambda: sys.stdin.readline().rstrip()

board = [0] * 10
for h in range(10):
    for w, v in enumerate(map(int, readline().split())):
        board[h] |= v << w

min_papers = solve(board)
print([-1, min_papers][min_papers != INF])
