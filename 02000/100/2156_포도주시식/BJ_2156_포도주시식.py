# https://www.acmicpc.net/problem/2156

wines = [int(input()) for _ in range(int(input()))]

PADDINGS = 3
weights = [[0, 0] for _ in range(len(wines) + PADDINGS)]
weights[PADDINGS] = [wines[0], wines[0]]

for i in range(PADDINGS + 1, len(weights)):
    weights[i][0] = max(*weights[i - 3], *weights[i - 2]) + wines[i - PADDINGS]
    weights[i][1] = max(*weights[i - 2], weights[i - 1][0]) + wines[i - PADDINGS]

print(max(weights[-1] + weights[-2]))
