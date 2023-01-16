import sys
import operator
from collections import deque


def readline():
    return sys.stdin.readline().rstrip()


def align_RB(balls):
    if balls[0][2] != 'R':
        balls[0], balls[1] = balls[::-1]
    return balls


def align_top(balls):
    balls.sort(key=operator.itemgetter(0))
    return balls


def align_bottom(balls):
    balls.sort(key=operator.itemgetter(0), reverse=True)
    return balls


def align_left(balls):
    balls.sort(key=operator.itemgetter(1))
    return balls


def align_right(balls):
    balls.sort(key=operator.itemgetter(1), reverse=True)
    return balls


def tuplify(balls):
    tup1, tup2 = tuple(balls[0][:2]), tuple(balls[1][:2])
    return (tup1 + tup2) if balls[0][2] == 'R' else (tup2 + tup1)


H, W = map(int, readline().split())
balls = []
board = [[None] * W for _ in range(H)]
for h in range(H):
    for w, c in enumerate(readline()):
        board[h][w] = c if c in '#O' else '.'
        balls.append((h, w, c)) if c in ['R', 'B'] else None

visited = set()
waypoints = deque([(0, tuplify(balls))])
min_turns = -1
while min_turns < 0 and 0 < len(waypoints) and waypoints[0][0] < 10:
    n_turns, (hR, wR, hB, wB) = waypoints.popleft()

    for align, dh, dw in [(align_bottom, 1, 0), (align_top, -1, 0), (align_right, 0, 1), (align_left, 0, -1)]:
        balls = align([[hR, wR, 'R', False], [hB, wB, 'B', False]])
        for i, ball in enumerate(balls):
            while not ball[-1]:
                if not (0 <= ball[0] + dh < H and 0 <= ball[1] + dw < W):
                    break

                is_collapsed = (board[ball[0] + dh][ball[1] + dw] == '#')
                if not is_collapsed and 0 < i:
                    for j in range(0, i):
                        if not balls[j][-1] and balls[j][0] == ball[0] + dh and balls[j][1] == ball[1] + dw:
                            is_collapsed = True
                            break

                if not is_collapsed:
                    ball[0] += dh
                    ball[1] += dw
                else:
                    break

                ball[-1] = (board[ball[0]][ball[1]] == 'O')

        align_RB(balls)
        if not balls[1][-1]:
            if balls[0][-1]:
                min_turns = n_turns + 1
                break

            if (state := tuplify(balls)) not in visited:
                visited.add(state)
                waypoints.append((n_turns + 1, state))

print(min_turns)
