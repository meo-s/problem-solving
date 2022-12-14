# https://www.acmicpc.net/problem/10830

import sys


def identity(N):
    m = [[0] * N for _ in range(N)]
    for i in range(N):
        m[i][i] = 1
    return m


def matmul(m1, m2):
    assert len(m1[0]) == len(m2)
    p, q, r = len(m1), len(m2[0]), len(m2)
    m = [[0] * p for _ in range(q)]
    for row in range(p):
        for col in range(q):
            m[row][col] = sum(m1[row][i] * m2[i][col] for i in range(r)) % 1000
    return m


def matpow(m, k):
    if k <= 2:
        return matmul(m, identity(len(m))) if k == 1 else matmul(m, m)
    else:
        mxm = matpow(matpow(m, k // 2), 2)
        mxm = matmul(mxm, m) if k % 2 == 1 else mxm
        return mxm


readline = lambda: sys.stdin.readline().strip()

N, B = map(int, readline().split())

m = []
for _ in range(N):
    m.append([*map(int, readline().split())])

for row_vec in matpow(m, B):
    print(*row_vec, sep=' ')
