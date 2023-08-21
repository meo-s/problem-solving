# https://www.acmicpc.net/problem/9465

TOP, BOTTOM = 0, 1
PADDINGS =2

for _ in range(int(input())):
    N = int(input())
    stickers = [[0] * PADDINGS + [*map(int, input().split())] for _ in range(2)]

    scores= [[0] * (N + PADDINGS) for _ in range(2)]
    for i in range(PADDINGS, N + PADDINGS):
        scores[TOP][i] = max(scores[TOP][i - 2], *scores[BOTTOM][i - 2:i]) + stickers[TOP][i]
        scores[BOTTOM][i] = max(scores[BOTTOM][i - 2], *scores[TOP][i - 2:i]) + stickers[BOTTOM][i]

    print(max(scores[TOP][-1], scores[BOTTOM][-1]))
