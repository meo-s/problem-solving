# https://www.acmicpc.net/problem/2170

import sys

readline = lambda: sys.stdin.readline().strip()

lines = [list(map(int, readline().split())) for _ in range(int(readline()))]
# lines.sort(key=lambda v: v[1], reverse=True)
lines.sort(key=lambda v: v[0])

last_line = lines[0]
total_len = 0
for line in lines[1:]:
    if last_line[1] < line[0]:
        total_len += last_line[1] - last_line[0]
        last_line = line
    else:
        last_line[1] = max(last_line[1], line[1])

print(total_len + (last_line[1] - last_line[0]))