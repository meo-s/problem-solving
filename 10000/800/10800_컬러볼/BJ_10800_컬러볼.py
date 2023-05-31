# https://www.acmicpc.net/problem/10800

import sys
import operator


def readline():
    return sys.stdin.readline().rstrip()


N = int(readline())
balls = [(i, *map(int, readline().split())) for i in range(N)]
balls.sort(key=operator.itemgetter(-1))

i = 0
cumsum, dp = 0, [0] * (N + 1)
eatables = [0] * N
while i < N:
    j = 0
    while i + j < N and balls[i][-1] == balls[i + j][-1]:
        eatables[balls[i + j][0]] = cumsum - dp[balls[i + j][1]]
        j += 1

    for k in range(j):
        cumsum += balls[i + k][-1]
        dp[balls[i + k][1]] += balls[i + k][-1]

    i += j

print(*eatables, sep='\n')
