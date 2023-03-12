# https://www.acmicpc.net/problem/27740

from operator import itemgetter

N = int(input())
bits = []
for i, bit in enumerate(map(int, input().split())):
    bits.append(i) if bit == 1 else None

min_cost = min((N - bits[0], N - bits[0], 0), (bits[-1] + 1, -(bits[-1] + 1), 0), key=itemgetter(0))
for i in range(len(bits) - 1):
    L = bits[i] + 1
    R = N - bits[i + 1]
    min_cost = min(min_cost, (2 * L + R, -L, L + R), key=itemgetter(0))
    min_cost = min(min_cost, (2 * R + L, R, -(R + L)), key=itemgetter(0))

shifts = []
shifts += ['L'] * abs(min(0, min_cost[1])) + ['R'] * abs(max(0, min_cost[1]))
shifts += ['L'] * abs(min(0, min_cost[2])) + ['R'] * abs(max(0, min_cost[2]))
print(len(shifts))
print(*shifts, sep='')
