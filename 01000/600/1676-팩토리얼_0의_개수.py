# https://www.acmicpc.net/problem/1676

n_zeros = 0
fact = 1
for n in range(1, int(input()) + 1):
    fact *= n
    while fact % 10 == 0:
        n_zeros += 1
        fact //= 10

print(n_zeros)
