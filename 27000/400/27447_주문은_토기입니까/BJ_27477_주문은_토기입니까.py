# https://www.acmicpc.net/problem/27477

from collections import deque

N, M = map(int, input().split())
customers = deque([*map(int, input().split())])
serving_times = deque()

now = 0
n_cups = 0
while 0 < len(customers) + len(serving_times):
    if 0 < len(customers) and customers[0] < now:
        break

    if 0 < len(serving_times) and serving_times[0] <= now:
        if now < serving_times[0]:
            break
        serving_times.popleft()
    elif 0 < n_cups and 0 < len(customers) and (customers[0] - M <= now < customers[0]):
        serving_times.append(customers.popleft())
        n_cups -= 1
    else:
        n_cups += 1

    now += 1

print(['fail', 'success'][(len(customers) + len(serving_times)) == 0])
