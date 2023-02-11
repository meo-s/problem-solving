# https://www.acmicpc.net/problem/27488

import sys


def readline():
    return sys.stdin.readline().rstrip()


def cut(state):
    def cut_straight(state):
        local_cost = 0
        for k, dk in [(0, 1), (len(state) - 1, -1)]:
            l_cost = 0
            valuable = False
            while 0 <= k < len(state) and 0 < state[k]:
                l_cost += state[k] & MUST_CUT
                valuable = valuable or (state[k] == MUST_CUT)
                k += dk

            if valuable and F <= l_cost:
                local_cost += F
                while (k, dk) not in [(0, 1), (len(state) - 1, -1)]:
                    state[(k := k - dk)] = 0

        return local_cost

    cost = cut_straight(state)
    return cost + state.count(MUST_CUT)


MUST_CUT = 1
H, W, F = map(int, input().split())

log = [[1] * W for _ in range(H)]
for y in range(H):
    for x, c in enumerate(readline()):
        if c == '#':
            log[y][x] = 0

hcut = [[0] * W for _ in range(H - 1)]
for y in range(H - 1):
    for x in range(W):
        hcut[y][x] = log[y][x] + log[y + 1][x]

vcut = [[0] * H for _ in range(W - 1)]
for x in range(W - 1):
    for y in range(H):
        vcut[x][y] = log[y][x] + log[y][x + 1]

print(sum(map(cut, hcut)) + sum(map(cut, vcut)))
