# https://www.acmicpc.net/problem/2138

import sys

readline = lambda: sys.stdin.readline().strip()

N = int(readline())
initial, goal = [*map(int, readline())], [*map(int, readline())]

possible = False
for weight in [0, 1]:
    now = [*initial]
    n_flips = weight
    flip(0) if 0 < n_flips else None

    def flip(index):
        now[index] ^= 1
        if 0 < index:
            now[index - 1] ^= 1
        if index < N - 1:
            now[index + 1] ^= 1

    for i in range(1, N):
        if now[i - 1] != goal[i - 1]:
            flip(i)
            n_flips += 1

    if now[-1] == goal[-1]:
        possible = True
        break

print(n_flips if possible else -1)
