# https://www.acmicpc.net/problem/20044

import sys
readline = lambda: sys.stdin.readline().strip()


N = int(readline())
caps = sorted(map(int, readline().split()))
print(min(caps[i - 1] + caps[-i] for i in range(1, N + 1)))
