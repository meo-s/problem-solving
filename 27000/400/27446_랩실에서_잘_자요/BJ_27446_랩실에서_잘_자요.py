# https://www.acmicpc.net/problem/27446

N, M = map(int, input().split())

paper = [False] * N + [True]
for page in map(int, input().split()):
    paper[page - 1] = True

k = 0
tol = 0
cost = 0
for i in range(N + 1):
    if not paper[i]:
        k += 1 + tol
        tol = 0
    elif 0 < k:
        tol += 1
        if 3 <= tol or i == N:
            cost += 5 + 2 * k
            k = 0
            tol = 0

print(cost)
