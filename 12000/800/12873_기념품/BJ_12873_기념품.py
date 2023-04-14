# https://www.acmicpc.net/problem/12873

from itertools import count
from collections import deque

N = int(input())
people = deque([*range(1, N + 1)])

for i in count(1):
    if len(people) == 1:
        break
    i = (i**3) % len(people)
    while 0 <= (i := i - 1):
        people.append(people.popleft())
    people.pop()

print(people[0])

