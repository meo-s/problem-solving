# https://www.acmicpc.net/problem/1026

import sys
readline = lambda: sys.stdin.readline().strip()


N = int(readline())
A = sorted(map(int, readline().split()))
B = sorted(map(int, readline().split()), reverse=True)

print(sum(a * b for a, b in zip(A, B)))
