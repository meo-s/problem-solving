# https://www.acmicpc.net/problem/29794

import sys


def shortest(x, y, to):
    return min(to, x + abs(to - y))


def simulate(heros, destinations, K, x, y):
    day, n_movements = 0, 0
    while day < K:
        for level in range(1, 200 - day):
            n_movements += shortest(x, y, destinations[level + day]) * heros[level]

        day += 1

    return n_movements


readline = lambda: sys.stdin.readline().rstrip()
N, M, K = map(int, readline().split())

heros = [0] * 201
for level in map(int, readline().split()):
    heros[level] += 1

monsters = [*map(int, readline().split())]
destinations = [0] * 200
for level in range(1, 200):
    optimal = -1
    for floor in range(len(monsters)):
        if monsters[floor] <= level:
            if optimal == -1 or monsters[optimal] < monsters[floor]:
                optimal = floor

    destinations[level] = optimal

ans = (float('inf'), -1, -1)
for x in range(0, M - 1):
    for y in range(x + 1, M):
        if (n_movements := simulate(heros, destinations, K, x, y)) < ans[0]:
            ans = (n_movements, x + 1, y + 1)

print(*ans[1:], sep=' ')
print(simulate(heros, destinations, K, 0, 0) - ans[0])
