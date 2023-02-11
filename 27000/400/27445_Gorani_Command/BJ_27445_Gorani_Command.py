# https://www.acmicpc.net/problem/27455

import sys


def readline():
    return sys.stdin.readline().rstrip()


def diamond_search(leverages, pt, dist):
    if dist == 0:
        y, x = pt
        leverages[y][x] += 1
    else:
        y, x = pt
        y += dist

        step = 0
        while step == 0 or (y - dist, x) != pt:
            if 0 <= y < H and 0 <= x < W:
                leverages[y][x] += 1
            y += dy[step // dist]
            x += dx[step // dist]
            step += 1


dy = [-1, -1, 1, 1]
dx = [1, -1, -1, 1]
H, W = map(int, readline().split())
leverages = [[0] * W for _ in range(H)]

for y in range(H - 1):
    diamond_search(leverages, (y, 0), int(readline()))
for x, dist in enumerate(map(int, readline().split())):
    diamond_search(leverages, (H - 1, x), dist)

for y in range(H):
    for x in range(W):
        if leverages[y][x] == H + (W - 1):
            print(y + 1, x + 1, sep=' ')
            break
