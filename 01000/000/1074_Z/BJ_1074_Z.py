# https://www.acmicpc.net/problem/1074

N, y, x = map(int, input().split())
nth = 0
for i in range(1, N + 1):
    half_size = 2 ** (N - i)
    phase = 2 * int(half_size <= y) + int(half_size <= x)
    nth += phase * (half_size ** 2)

    y %= half_size
    x %= half_size

print(nth)
