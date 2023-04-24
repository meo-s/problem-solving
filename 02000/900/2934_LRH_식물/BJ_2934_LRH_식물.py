# https://www.acmicpc.net/problem/2934

import sys


def fenwick_tree_update(tree, index, dvalue):
    while index < len(tree):
        tree[index] += dvalue
        index += index & -index


def fenwick_tree_query(tree, index):
    value = 0
    while 0 < index:
        value += tree[index]
        index -= index & -index
    return value


readline = lambda: sys.stdin.readline().rstrip()  # noqa

tree = [0] * 100_001
for _ in range(int(readline())):
    L, R = map(int, readline().split())

    n_flowers = (dflowers := fenwick_tree_query(tree, L))
    if 0 < dflowers:
        fenwick_tree_update(tree, L, -dflowers)
        fenwick_tree_update(tree, L + 1, dflowers)

    n_flowers += (dflowers := fenwick_tree_query(tree, R))
    if 0 < dflowers:
        fenwick_tree_update(tree, R, -dflowers)
        fenwick_tree_update(tree, R + 1, dflowers)

    fenwick_tree_update(tree, L + 1, 1)
    fenwick_tree_update(tree, R, -1)
    print(n_flowers)
