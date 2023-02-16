# https://www.acmicpc.net/problem/6198

import sys


def readline():
    return sys.stdin.readline().rstrip()


def count_viewables(heights):
    def count(i):
        j = 1
        while i + j < len(heights) and heights[i + j] < heights[i]:
            j += count(i + j) + 1

        viewables[i] = j - 1
        return viewables[i]

    viewables = [-1] * len(heights)
    for i in range(len(viewables)):
        if viewables[i] < 0:
            count(i)

    return viewables


sys.setrecursionlimit(80000)

N = int(readline())
print(sum(count_viewables([int(readline()) for _ in range(N)])))
