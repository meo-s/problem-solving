# https://www.acmicpc.net/problem/1459

x, y, W, S = map(int, input().split())

cost = min(W * 2, S) * min(x, y)
cost += min(W * 2, S * 2) * (abs(x - y) // 2)
cost += W if (x + y) % 2 != 0 else 0

print(cost)
