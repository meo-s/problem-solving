# https://www.acmicpc.net/problem/1946

import sys
import functools

readline = lambda: sys.stdin.readline().strip()


def cmp(v1, v2):
    if v1[0] == v2[0]:
        return -1 if v1[1] < v2[1] else 0
    return -1 if v1[0] < v2[0] else 0


for _ in range(int(readline())):
    N = int(readline())
    people = sorted([tuple(map(int, readline().split())) for _ in range(N)], key=functools.cmp_to_key(cmp))
    print(people)
