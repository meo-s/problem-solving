# https://www.acmicpc.net/problem/2785

import sys
readline = lambda: sys.stdin.readline().strip()


_ = int(readline())
chain_lens = sorted(map(int, readline().split()))

i = 0
n_isolated = len(chain_lens) - 1
n_links = 0
while 0 < n_isolated:
    n_links += min(n_isolated, chain_lens[i])
    n_isolated -= min(n_isolated, chain_lens[i] + 1)
    i += 1

print(n_links)
