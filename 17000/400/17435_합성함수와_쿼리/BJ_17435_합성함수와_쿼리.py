# https://www.acmicpc.net/problem/17435

import sys
import math

log2N = math.floor(math.log2(500_000)) + 1
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
fn = [[None] * N for _ in range(log2N)]
fn[0][:] = map(int, readline().split())
for n in range(1, log2N):
    for x in range(0, N):
        fn[n][x] = fn[n - 1][fn[n - 1][x] - 1]

for _ in range(int(readline())):
    n, x = map(int, readline().split())
    while 0 < n:
        jump = int(math.log2(n))
        x = fn[jump][x - 1]
        n -= 1 << jump

    print(x)
