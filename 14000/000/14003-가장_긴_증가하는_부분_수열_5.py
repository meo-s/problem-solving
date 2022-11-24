# https://www.acmicpc.net/problem/14003

import sys
from bisect import bisect_left


def readint():
    n = sys.stdin.read(2)
    while n[-1] not in [' ', '\n']:
        n += sys.stdin.read(1)
    return int(n)


N = int(sys.stdin.readline())
dp = [(readint(), )]
for _ in range(N - 1):
    if dp[-1][-1] < (n := readint()):
        dp.append((dp[-1], n))
    else:
        i = bisect_left(dp, n, 0, len(dp) - 1, key=lambda v: v[-1])
        dp[i] = (dp[i - 1], n) if 0 < i else (n, )

lis = []
while isinstance(dp[-1], tuple):
    lis.append(dp[-1][-1])
    dp[-1] = dp[-1][0]

print(len(lis))
print(*lis[::-1], sep=' ')
