# https://www.acmicpc.net/problem/1463

from collections import deque

n = int(input())

nums = deque([n])
history = [-1] * 1_000_001
history[n] = 0
while history[1] < 0:
    n = nums.popleft()
    if history[n - 1] < 0:
        history[n - 1] = history[n] + 1
        nums.append(n - 1)
    for dividor in [2, 3]:
        if n % dividor == 0 and history[n // dividor] < 0:
            history[n // dividor] = history[n] + 1
            nums.append(n // dividor)

print(history[1])
