# https://www.acmicpc.net/problem/19644

import sys
from collections import deque


def readline():
    return sys.stdin.readline().rstrip()


L = int(readline())
R, D = map(int, readline().split())
n_bombs = int(readline())

alive = True
bomb_logs = deque([])
for i, hp in enumerate([int(readline()) for _ in range(L)], start=1):
    while 0 < len(bomb_logs) and R <= (i - bomb_logs[0]):
        bomb_logs.popleft()

    if 0 < hp - ((min(i, R) - len(bomb_logs)) * D):
        if n_bombs == 0:
            alive = False
            break
        n_bombs -= 1
        bomb_logs.append(i)

print(['NO', 'YES'][alive])
