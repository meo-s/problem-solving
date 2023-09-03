# https://www.acmicpc.net/problem/17609

import sys

readline = lambda: sys.stdin.readline().strip()


def check(s, i, j):
    while i <= j:
        if s[i] != s[j]:
            return i, j
        i, j = i + 1, j - 1

    return i, j


for _ in range(int(readline())):
    s = readline()
    i, j = check(s, 0, len(s) - 1)
    if (ans := [2, 0][j < i]) != 0:
        for i, j in [(i + 1, j), (i, j - 1)]:
            i, j = check(s, i, j)
            if j < i:
                ans = 1
                break

    print(ans)
