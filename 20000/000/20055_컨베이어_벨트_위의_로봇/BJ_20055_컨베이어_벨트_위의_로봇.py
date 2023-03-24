# https://www.acmicpc.net/problem/20055

from collections import deque

N, K = map(int, input().split())
tols = [*map(int, input().split())]

turn = 0
robots = deque()
is_empty = [True] * (2 * N)
while 0 < K:
    turn += 1
    ii = (2 * N - turn) % (2 * N)
    oi = (2 * N + (N - 1) - turn) % (2 * N)

    for _ in range(len(robots)):
        robot = robots.popleft()
        is_empty[robot] = True

        if robot != oi:
            next_i = (robot + 1) % (2 * N)
            if tols[next_i] == 0 or not is_empty[next_i]:
                robots.append(robot)
                is_empty[robot] = False
                continue

            robot = next_i
            tols[robot] -= 1
            K -= int(tols[robot] == 0)
            if robot != oi:
                robots.append(robot)
                is_empty[robot] = False

    if 0 < tols[ii] and is_empty[ii]:
        tols[ii] -= 1
        is_empty[ii] = False
        K -= int(tols[ii] == 0)
        robots.append(ii)

print(turn)
