# https://www.acmicpc.net/problem/2873

import sys

readline = lambda: sys.stdin.readline().rstrip()  # noqa

H, W = map(int, readline().split())
board = [[*map(int, readline().split())] for _ in range(H)]

if H % 2 == 1:
    steer = ['R' * (W - 1) + 'D', 'L' * (W - 1) + 'D']
    for y in range(H):
        print(steer[y % 2][:W - int(y == H - 1)], end='')

elif W % 2 == 1:
    steer = ['D' * (H - 1) + 'R', 'U' * (H - 1) + 'R']
    for x in range(W):
        print(steer[x % 2][:H - int(x == W - 1)], end='')

else:
    min_pos = None
    min_happiness = float('inf')
    for y in range(H):
        for x in range((y % 2) ^ 1, W, 2):
            if board[y][x] < min_happiness:
                min_pos = (y, x)
                min_happiness = board[y][x]

    phase = 0
    steer = ['R' * (W - 1) + 'D' + 'L' * (W - 1) + 'D', 'L' * (W - 1) + 'D' + 'R' * (W - 1) + 'D']
    for y in range(0, H, 2):
        if y // 2 != min_pos[0] // 2:
            print(steer[int(min_pos[0] // 2 < y // 2)][:W * 2 - int(y == H - 2)], end='')
        else:
            dy = [1, 0, -1, 0]
            dx = [0, 1, 0, 1]
            direction = 0
            ny, nx = y, 0
            while True:
                if (ny + dy[direction], nx + dx[direction]) == min_pos:
                    print('R', end='')
                    nx += 1

                print('DRUR'[direction], end='')

                ny += dy[direction]
                nx += dx[direction]
                direction = (direction + 1) % len(dy)

                if (ny, nx) == (y + 1, W - 1):
                    if (ny, nx) != (H - 1, W - 1):
                        print('D', end='')
                    break

print('')
