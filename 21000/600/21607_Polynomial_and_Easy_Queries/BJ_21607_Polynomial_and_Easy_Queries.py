# https://www.acmicpc.net/21607

import math
import sys


def fenwick_tree_update(tree, index, dvalue):
    while index < len(tree):
        tree[index][0] += dvalue[0]
        tree[index][1] += dvalue[1]
        index += index & -index


def fenwick_tree_query(tree, index):
    value = [0] * 2
    while 0 < index:
        value[0] += tree[index][0]
        value[1] += tree[index][1]
        index -= index & -index
    return value


M = 100003
L = int(math.log2(5e+5)) + 1
readline = lambda: sys.stdin.readline().rstrip()  # noqa

f = [[[0] * M for _ in range(L)] for _ in range(2)]
for x in range(M):
    f[0][0][x] = (2 * x**2 - 1) % M
    f[1][0][x] = (4 * x**3 - 3 * x) % M
for n in range(1, L):
    for x in range(M):
        f[0][n][x] = f[0][n - 1][f[0][n - 1][x]]
        f[1][n][x] = f[1][n - 1][f[1][n - 1][x]]

N, Q = map(int, readline().split())
tree = [[0] * 2 for _ in range(N + 1)]
Ax = [*map(int, readline().split())]
for _ in range(Q):
    q, *args = map(int, readline().split())
    if q != 3:
        l, r = args
        dvalue = [0] * 2
        dvalue[q - 1] = 1
        fenwick_tree_update(tree, l, dvalue)
        fenwick_tree_update(tree, r + 1, [-v for v in dvalue])
    else:
        x = Ax[args[0] - 1]
        n = fenwick_tree_query(tree, args[0])
        for k in range(len(n)):
            i = -1
            while 0 < n[k]:
                i += 1
                if (n[k] & (1 << i)) != 0:
                    n[k] ^= 1 << i
                    x = f[k][i][x]

        print(x)
