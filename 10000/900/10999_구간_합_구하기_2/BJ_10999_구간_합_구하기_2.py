# https://www.acmicpc.net/problem/10999

import sys
import math


def update(tree, pendings, node, left, right, beg, end, dv):
    before = tree[node]
    if beg == left and end == right:
        tree[node] += dv * (end - beg)
        if 1 < end - beg:
            pendings[node * 2] += dv
            pendings[node * 2 + 1] += dv
    else:
        mid = (left + right) // 2
        if end <= mid:
            tree[node] += update(tree, pendings, node * 2, left, mid, beg, end, dv)
        elif mid <= beg:
            tree[node] += update(tree, pendings, node * 2 + 1, mid, right, beg, end, dv)
        else:
            tree[node] += update(tree, pendings, node * 2, left, mid, beg, mid, dv)
            tree[node] += update(tree, pendings, node * 2 + 1, mid, right, mid, end, dv)

    return tree[node] - before


def query(tree, pendings, node, left, right, beg, end):
    if (dv := pendings[node]) != 0:
        pendings[node] = 0
        tree[node] += dv * (right - left)
        if 1 < right - left:
            pendings[node * 2] += dv
            pendings[node * 2 + 1] += dv

    if beg == left and end == right:
        return tree[node]

    mid = (left + right) // 2
    if end <= mid:
        return query(tree, pendings, node * 2, left, mid, beg, end)
    elif mid <= beg:
        return query(tree, pendings, node * 2 + 1, mid, right, beg, end)
    else:
        lr = query(tree, pendings, node * 2, left, mid, beg, mid)
        rr = query(tree, pendings, node * 2 + 1, mid, right, mid, end)
        return lr + rr


readline = lambda: sys.stdin.readline().rstrip()  # noqa
N, M, K = map(int, readline().split())

tree = [0] * ((1 << (math.ceil(math.log2(N)) + 1)) + 1)
size = 1 << (math.ceil(math.log2(N)))
pendings = [0] * len(tree)
for i in range(1, N + 1):
    update(tree, pendings, 1, 1, size + 1, i, i + 1, int(readline()))

for _ in range(M + K):
    q, *args = map(int, readline().split())
    if q == 1:
        b, c, d = args
        update(tree, pendings, 1, 1, size + 1, b, c + 1, d)
    else:
        b, c = args
        print(query(tree, pendings, 1, 1, size + 1, b, c + 1))
