# https://www.acmicpc.net/problem/7570

import sys


def readline():
    return sys.stdin.readline().rstrip()


dp = [0] + [0] * (N := int(readline()))
max_l1s = 0
for n in map(int, readline().split()):
    dp[n] = dp[n - 1] + 1
    max_l1s = max(max_l1s, dp[n])

print(N - max_l1s)
