# https://www.acmicpc.net/problem/14939

import sys

N = 10


def toggle(lights, row, col):
    for drow, dcol in [(1, 0), (-1, 0), (0, 1), (0, -1), (0, 0)]:
        if 0 <= row + drow < len(lights) and 0 <= col + dcol < len(lights):
            lights[row + drow] ^= 1 << (col + dcol)


def search(lights):
    def search_impl(lights, row=0, col=0, count=0):
        global N

        if row == N:
            return [float('inf'), count][sum(lights) == 0]
        if col == N:
            return search_impl(lights, row + 1, 0, count)

        if row == 0:
            toggle(lights, row, col)
            case1 = search_impl(lights, row, col + 1, count + 1)
            toggle(lights, row, col)
            case2 = search_impl(lights, row, col + 1, count)
            return min(case1, case2)
        if 0 < row and (lights[row - 1] & (1 << col)):
            toggle(lights, row, col)
            count = search_impl(lights, row, col + 1, count + 1)
            toggle(lights, row, col)
            return count
        else:
            return search_impl(lights, row, col + 1, count)

    min_count = search_impl(lights)
    return [-1, min_count][min_count != float('inf')]


readline = lambda: sys.stdin.readline().strip()

lights = [0] * N
for i in range(N):
    for j, c in enumerate(readline()):
        lights[i] |= (1 << j) if c == 'O' else 0

print(search(lights))
