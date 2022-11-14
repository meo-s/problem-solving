# https://www.acmicpc.net/problem/11047

import sys
readline = lambda: sys.stdin.readline().strip()


N, K = map(int, readline().split())
A = [int(readline()) for _ in range(N)][::-1]

n_coins = 0
while 0 < K:
    n_coins += K // A[0]
    K -= A[0] * (K // A[0])
    A = A[1:]

print(n_coins)
