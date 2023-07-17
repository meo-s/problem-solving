# https://www.acmicpc.net/problem/2580

import sys


def solve(board, h=0, w=0):
    if h == 9:
        return board
    if w == 9:
        return solve(board, h + 1, 0)
    if board[h][w] != 0:
        return solve(board, h, w + 1)

    bitmask = 0b1111111110
    for i in range(9):
        bitmask &= ~((1 << board[h][i]) | (1 << board[i][w]))
    for i in range(3 * (h // 3), 3 * (h // 3 + 1)):
        for j in range(3 * (w // 3), 3 * (w // 3 + 1)):
            bitmask &= ~(1 << board[i][j])

    for i in range(1, 10):
        if (bitmask & (1 << i)) != 0:
            board[h][w] = i
            if solve(board, h, w + 1) is not None:
                return board
            board[h][w] = 0

    return None


readline = lambda: sys.stdin.readline().rstrip()

board = [[*map(int, readline().split())] for _ in range(9)]
print(*[' '.join(map(str, line)) for line in solve(board)], sep='\n')
