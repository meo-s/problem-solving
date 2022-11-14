# https://www.acmicpc.net/problem/2217

import sys
readline = lambda: sys.stdin.readline().strip()


N = int(readline())
ropes = sorted([int(readline()) for _ in range(N)])
ropes = [rope * (N - i) for i, rope in enumerate(ropes)]
print(max(ropes))
