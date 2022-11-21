# https://www.acmicpc.net/problem/1946

import sys

readline = lambda: sys.stdin.readline().strip()

for _ in range(int(readline())):
    N = int(readline())
    people = sorted([tuple(map(int, readline().split())) for _ in range(N)], key=lambda v: v[0], reverse=True)

    n_recruits = 0
    rank_bound = N
    while 0 < len(people):
        _, rank = people.pop()
        if rank_bound < rank:
            continue

        n_recruits += 1
        rank_bound = min(rank_bound, rank)

    print(n_recruits)
