# https://www.acmicpc.net/problem/2239

import sys


def solve(board):
    def search(board, h, w):
        if h == 9:
            return True
        if w == 9:
            return search(board, h + 1, 0)
        if board[h][w] != 0:
            return search(board, h, w + 1)

        bitmask = 0b111111111
        for i in range(9):
            if board[h][i] != 0:
                bitmask = bitmask & ~(1 << (board[h][i] - 1))
            if board[i][w] != 0:
                bitmask = bitmask & ~(1 << (board[i][w] - 1))

        for i in range(3 * (h // 3), 3 * (h // 3 + 1)):
            for j in range(3 * (w // 3), 3 * (w // 3 + 1)):
                if board[i][j] != 0:
                    bitmask = bitmask & ~(1 << (board[i][j] - 1))

        solved = False
        for i in range(9):
            if 0 < bitmask & (1 << i):
                board[h][w] = i + 1
                if (solved := search(board, h, w + 1)):
                    break
                board[h][w] = 0

        return solved

    return search(board, 0, 0)


readline = lambda: sys.stdin.readline().strip()

board = [[*map(int, readline())] for _ in range(9)]
solve(board)
print(*[''.join(map(str, row)) for row in board], sep='\n')
