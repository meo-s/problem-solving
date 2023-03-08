# https://www.acmicpc.net/problem/2831

import sys

readline = lambda: sys.stdin.readline().strip()

readline()
men, women = [sorted(map(int, readline().split()), reverse=reverse) for reverse in [False, True]]

n_pairs = 0
while 0 < (len(men) * len(women)):
    if 0 < men[-1] * women[-1]:
        [men, women][women[-1] < 0].pop()
        continue

    man = men.pop()
    if (0 < man and man < abs(women[-1])) or (man < 0 and women[-1] < abs(man)):
        n_pairs += 1
        women.pop()

print(n_pairs)
