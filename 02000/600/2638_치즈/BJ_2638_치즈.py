# https://www.acmicpc.net/problem/2638

import sys


def propagate(dish, y, x):
    dish[y][x] = 2
    points = [(y, x)]
    while 0 < len(points):
        y, x = points.pop()
        for dy, dx in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
            if not (0 <= y + dy < len(dish) and 0 <= x + dx < len(dish[0])): continue
            if dish[y + dy][x + dx] == 0:
                dish[y + dy][x + dx] = 2
                points.append((y + dy, x + dx))


def find_chesses(dish):
    cheeses = []
    for y in range(H):
        for x in range(W):
            if dish[y][x] != 1: continue

            n_exposes = 0
            for dy, dx in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
                if 0 <= y + dy < H and 0 <= x + dx < W:
                    n_exposes += int(dish[y + dy][x + dx] == 2)
            if 2 <= n_exposes:
                cheeses.append((y, x))

    return cheeses


readline = lambda: sys.stdin.readline().strip()

H, W = map(int, readline().split())
dish = [[*map(int, readline().split())] for _ in range(H)]
propagate(dish, 0, 0)

n_hours = 0
while 0 < len(cheeses := find_chesses(dish)):
    n_hours += 1

    for y, x in cheeses:
        propagate(dish, y, x)

print(n_hours)
