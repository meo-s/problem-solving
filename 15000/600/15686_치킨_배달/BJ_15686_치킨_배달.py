# https://www.acmicpc.net/problem/15686

import sys
from itertools import combinations


def chicken_dist(homes, stores):
    def L1(pt1, pt2):
        return abs(pt1[0] - pt2[0]) + abs(pt1[1] - pt2[1])

    total_dist = 0
    for home in homes:
        min_dist = float('inf')
        for store in stores:
            min_dist = min(min_dist, L1(home, store))

        total_dist += min_dist

    return total_dist


readline = lambda: sys.stdin.readline().strip()

N, M = map(int, readline().split())

homes, stores = [], []
for y in range(N):
    for x, n in enumerate(map(int, readline().split())):
        homes.append((y, x)) if n == 1 else None
        stores.append((y, x)) if n == 2 else None

min_chicken_dist = float('inf')
for comb in combinations(range(len(stores)), M):
    live_stores = [stores[j] for j in comb]
    min_chicken_dist = min(chicken_dist(homes, live_stores), min_chicken_dist)

print(min_chicken_dist)
