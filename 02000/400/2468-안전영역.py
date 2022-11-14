# https://www.acmicpc.net/problem/2468

import itertools

N = int(input())

def visit_contour(regions, visits, min_height, xy):
    points = [xy]
    while 0 < len(points):
        x, y = points.pop()
        visits[y][x] = True

        for x, y in [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]:
            if 0 <= x < N and 0 <= y < N:
                if min_height <= regions[y][x] and not visits[y][x]:
                    points.append((x, y))
                    visits[y][x] = True


def count_contours(regions, min_height):
    n_contours = 0

    visits = [[False] * N for _ in range(N)]
    for y in range(N):
        for x in range(N):
            if min_height <= regions[y][x] and not visits[y][x]:
                visit_contour(regions, visits, min_height, (x, y))
                n_contours += 1

    return n_contours

regions = [list(map(int, input().strip().split())) for _ in range(N)]

max_contours = 1
for min_height in set(itertools.chain(*regions)):
    max_contours = max(max_contours, count_contours(regions, min_height))

print(max_contours)
