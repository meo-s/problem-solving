# https://www.acmicpc.net/problem/17408

import math
import sys


def top2_merge(a, *args):
    ret = [*args[0]]
    for other in args[1:]:
        if other is None:
            continue
        for index in other:
            if index not in ret:
                if a[ret[1]] < a[index]:
                    ret[1] = index
                if a[ret[0]] < a[index]:
                    ret[1] = ret[0]
                    ret[0] = index
    return ret


def seg_update(a, seg, node, left, right, index, value):
    if left <= index < right:
        if right - left == 1:
            a[index] = value
            if seg[node] is None:
                seg[node] = (index, 0)
        else:
            mid = (left + right) // 2
            seg_update(a, seg, node * 2, left, mid, index, value)
            seg_update(a, seg, node * 2 + 1, mid, right, index, value)
            seg[node] = top2_merge(a, seg[node * 2], seg[node * 2 + 1])


def seg_query(a, seg, node, left, right, beg, end):
    if right <= beg or end <= left:
        return (0, 0)

    if beg <= left and right <= end:
        return seg[node]
    else:
        mid = (left + right) // 2
        lr = seg_query(a, seg, node * 2, left, mid, beg, end)
        rr = seg_query(a, seg, node * 2 + 1, mid, right, beg, end)
        return top2_merge(a, lr, rr)


readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
L = math.ceil(math.log2(N))
a = [0] * (N + 1)
seg = [None] * (1 << (L + 1))
for i, n in enumerate(map(int, readline().split())):
    seg_update(a, seg, 1, 1, (1 << L) + 1, i + 1, n)

for _ in range(int(readline())):
    q, *args = map(int, readline().split())
    if q == 1:
        seg_update(a, seg, 1, 1, (1 << L) + 1, *args)
    else:
        ret = seg_query(a, seg, 1, 1, (1 << L) + 1, args[0], args[1] + 1)
        print(a[ret[0]] + a[ret[1]])
