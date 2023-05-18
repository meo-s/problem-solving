# https://www.acmicpc.net/problem/1208

import itertools


def possible_summations_of(seq):
    sums = {}
    for r in range(1, len(seq) + 1):
        for picks in itertools.combinations(seq, r):
            sums[sum(picks)] = sums.get(sum(picks), 0) + 1

    return sums


def bisearch(a, v):
    lb, ub = 0, len(a)
    while lb < ub:
        mid = (lb + ub) // 2
        if a[mid] == v:
            return True
        if a[mid] < v:
            lb = mid + 1
        else:
            ub = mid
    return False


N, S = map(int, input().split())
seq = [*map(int, input().split())]

lsum_counter = possible_summations_of(seq[:len(seq) // 2])
rsum_counter = possible_summations_of(seq[len(seq) // 2:])

n_possibles = lsum_counter.get(S, 0) + rsum_counter.get(S, 0)
rsums = sorted(rsum_counter.keys())
for n in lsum_counter.keys():
    if bisearch(rsums, S - n):
        n_possibles += lsum_counter[n] * rsum_counter[S - n]

print(n_possibles)
