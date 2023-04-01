import sys
from collections import defaultdict

readline = lambda: sys.stdin.readline().rstrip()  # noqa

score = defaultdict(int)
for _ in range(int(readline())):
    score[readline()] += 1
    if 2 <= abs(score['D'] - score['P']):
        break

print(score['D'], score['P'], sep=':')
