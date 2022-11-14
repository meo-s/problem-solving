# https://www.acmicpc.net/problem/2012

import sys
readline = lambda: sys.stdin.readline().strip()


N = int(readline())
priors = sorted(map(int, [readline() for _ in range(N)]))
print(sum(abs((i + 1) - prior) for i, prior in enumerate(priors)))
