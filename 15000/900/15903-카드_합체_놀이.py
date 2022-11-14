# https://www.acmicpc.net/problem/15903

import heapq
import sys
readline = lambda: sys.stdin.readline().strip()


N, M = map(int, readline().split())
heapq.heapify(cards := [*map(int, readline().split())])

for _ in range(M):
    min_card = heapq.heappop(cards)
    new_card = min_card + cards[0]
    heapq.heapreplace(cards, new_card)
    heapq.heappush(cards, new_card)

print(sum(cards))
