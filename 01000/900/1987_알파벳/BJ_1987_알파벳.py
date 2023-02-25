# https://www.acmicpc.net/problem/1987

import sys


def dfs(board):
    def _dfs(board, pos, depth, bitmask):
        y, x = pos
        max_depth = depth
        for dy, dx in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
            if not (0 <= y + dy < H and 0 <= x + dx < W):
                continue

            alphabet = 1 << (ord(board[y + dy][x + dx]) - ord('A'))
            if 0 < (bitmask & alphabet):
                continue

            max_depth = max(max_depth, _dfs(board, (y + dy, x + dx), depth + 1, bitmask | alphabet))

        return max_depth

    return _dfs(board, (0, 0), 1, 1 << (ord(board[0][0]) - ord('A')))


readline = lambda: sys.stdin.readline().strip()

H, W = map(int, readline().split())
board = [readline() for _ in range(H)]

print(dfs(board))
