# https://www.acmicpc.net/problem/9082

import sys

readline = lambda: sys.stdin.readline().strip()


def decay_weights(weights, i):
    weights[i] -= 1
    if 0 <= i - 1:
        weights[i - 1] -= 1
    if i + 1 < len(weights):
        weights[i + 1] -= 1


for _ in range(int(readline())):
    n_mines = 0

    N = int(readline())
    weights = [*map(int, readline())]
    for i, c in enumerate(readline()):
        if c == '*':
            n_mines += 1
            decay_weights(weights, i)

    for i in range(1, len(weights)):
        for di in [-1, 0]:
            if 0 < weights[i - 1] and 0 < weights[max(i + di - 1, 0)]:
                n_mines += 1
                decay_weights(weights, i + di)

    print(n_mines)
