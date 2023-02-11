# https://www.acmicpc.net/problem/27450

import sys
from collections import deque


def readline():
    return sys.stdin.readline().rstrip()


N, K = map(int, readline().split())
cur_power = [*map(int, readline().split())]
obj_power = [*map(int, readline().split())]

total_screams = 0
echos = deque()
n_echos = 0
leverage = 0
for i in range(N):
    leverage -= n_echos
    while 0 < len(echos) and K <= i - echos[0][0]:
        n_echos -= echos.popleft()[1]

    if leverage < (power_diff := obj_power[i] - cur_power[i]):
        new_screams = (power_diff - leverage) // K
        new_screams += int(0 < (power_diff - (leverage + K * new_screams)))

        leverage += K * new_screams

        echos.append((i, new_screams))
        n_echos += new_screams

        total_screams += new_screams

print(total_screams)
