# https://www.acmicpc.net/problem/10844

N = int(input())

cache = [[0] * 10 for _ in range(N)]
cache[0] = [1] * 10

for length in range(1, N):
    for i in range(0, 10):
        if 0 < i:
            cache[length][i] += cache[length - 1][i - 1]
            cache[length][i] %= 1_000_000_000
        if i < 9:
            cache[length][i] += cache[length - 1][i + 1]
            cache[length][i] %= 1_000_000_000

ans = 0
for i in range(1, 10):
    ans += cache[N - 1][i]
    ans %= 1_000_000_000

print(ans)
