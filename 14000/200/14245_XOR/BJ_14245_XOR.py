# https://www.acmicpc.net/problem/14245

import sys


def update(tree, index, value):
    while index < len(tree):
        tree[index] ^= value
        index += index & -index


def query(tree, index):
    value = 0
    while 0 < index:
        value ^= tree[index]
        index -= index & -index
    return value


readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
a = [*map(int, readline().split())]

fenwick_tree = [0] * (N + 1)
update(fenwick_tree, 1, a[0])
for i in range(1, N):
    update(fenwick_tree, i + 1, a[i] ^ a[i - 1])

for _ in range(int(readline())):
    q, *args = map(int, readline().split())
    if q == 1:
        beg, end, value = args
        update(fenwick_tree, beg + 1, value)
        update(fenwick_tree, end + 2, value) if end + 2 <= N else None
    else:
        print(query(fenwick_tree, args[0] + 1))
