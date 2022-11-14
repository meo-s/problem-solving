# https://www.acmicpc.net/problem/7562

from collections import deque

NOT_VISITED = -1

for _ in range(int(input())):
    board_size = int(input())
    board = [[NOT_VISITED] * board_size for _ in range(board_size)]

    x, y = tuple(map(int, input().split()))
    board[y][x] = 0

    gx, gy = tuple(map(int, input().split()))
    waypoints = deque([(x, y)])
    while board[gy][gx] == NOT_VISITED:
        x, y = waypoints.popleft()
        for dx, dy in [(1, 2), (2, 1), (2, -1), (1, -2), (-1, -2), (-2, -1), (-2, 1), (-1, 2)]:
            if (not 0 <= x + dx < board_size) or (not 0 <= y + dy < board_size):
                continue
            if board[y + dy][x + dx] != NOT_VISITED:
                continue

            board[y + dy][x + dx] = board[y][x] + 1
            waypoints.append((x + dx, y + dy))

    print(board[gy][gx])
