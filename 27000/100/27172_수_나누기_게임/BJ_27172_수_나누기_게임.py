# https://www.acmicpc.net/problem/27172

import itertools
import operator

exists = [-1] * 1_000_001

N = int(input())
cards = [*zip(range(N), map(int, input().split()))]
cards.sort(key=operator.itemgetter(1), reverse=True)

scores = [0] * N
for i, card in cards:
    exists[card] = i
    for j in itertools.count(2):
        if len(exists) <= card * j:
            break
        if exists[card * j] != -1:
            scores[exists[card * j]] -= 1
            scores[i] += 1

print(*scores, sep=' ')
