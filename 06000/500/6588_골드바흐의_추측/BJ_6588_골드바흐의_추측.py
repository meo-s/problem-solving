# https://www.acmicpc.net/problem/6588

import math
import sys
readline = lambda: sys.stdin.readline().strip()

is_prime = [False, False, False, True] + [False] * (1000001 - 4)
for k in range(6, 1000001, 6):
    for n in [k - 1, k + 1]:
        i, upper_bound = 2, math.floor(math.sqrt(n)) + 1
        while i < upper_bound and n % i != 0:
            i += 1

        is_prime[n] = (i == upper_bound)

def next_prime(prime):
    for n in range(prime + 1, len(is_prime)):
        if is_prime[n]:
            return n
    return None

while (n := int(readline())) != 0:
    odd = 0
    while True:
        if (odd := next_prime(odd)) is None or n // 2 < odd:
            print('Goldbach\'s conjecture is wrong.')
            break
        if is_prime[n - odd]:
            print('%d = %d + %d' % (n, odd, n - odd))
            break
