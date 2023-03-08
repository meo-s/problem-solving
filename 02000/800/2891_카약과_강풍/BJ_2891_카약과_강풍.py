# https://www.acmicpc.net/problem/2891

import sys
readline = lambda: sys.stdin.readline().strip()


N, S, R = map(int, readline().split())
tickets = [1] * (N + 1)
for i in map(int, readline().split()):
    tickets[i] = 0
for i in map(int, readline().split()):
    if tickets[i] == 0:
        tickets[i] = 1
    elif 0 < i  and tickets[i - 1] == 0:
        tickets[i - 1] = 1
    elif i < N and tickets[i + 1] == 0:
        tickets[i + 1] = 1

print(tickets.count(0))
