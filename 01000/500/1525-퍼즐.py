# https://www.acmicpc.net/problem/1525

import sys
import itertools
from collections import defaultdict, deque

readline = lambda: sys.stdin.readline().strip()

DIGITS = [10**i for i in reversed(range(9))]
enc = lambda puzzle: sum(v * DIGITS[i] for i, v in enumerate(puzzle))
dec = lambda puzzle: [(puzzle // DIGITS[i]) % 10 for i in range(9)]

start_state = enc(itertools.chain(*[map(int, readline().split()) for _ in range(3)]))
visited = defaultdict(lambda: -1)
visited[start_state] = 0

states = deque([start_state])
while visited[123456780] < 0 and 0 < len(states):
    state = states.popleft()
    zero_index = (puzzle := dec(state)).index(0)
    y, x = zero_index // 3, zero_index % 3
    for dy, dx in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
        if not (0 <= y + dy < 3 and 0 <= x + dx < 3):
            continue

        new_puzzle = [*puzzle]
        new_puzzle[y * 3 + x] = puzzle[(y + dy) * 3 + (x + dx)]
        new_puzzle[(y + dy) * 3 + (x + dx)] = puzzle[y * 3 + x]
        if visited[(next_state := enc(new_puzzle))] < 0:
            visited[next_state] = visited[state] + 1
            states.append(next_state)

print([-1, visited[123456780]][0 <= visited[123456780]])
