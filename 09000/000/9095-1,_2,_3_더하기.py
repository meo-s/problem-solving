# https://www.acmicpc.net/problem/9095

import sys
import functools
import operator

readline = lambda: sys.stdin.readline().strip()

fact = lambda n: functools.reduce(operator.mul, (i for i in range(1, n + 1)), 1)
fact = functools.cache(fact)


def break_down(n, upper_bound=3, digits=[0, 0, 0]):
    if n == 0:
        return fact(sum(digits)) // functools.reduce(operator.mul, map(fact, digits))

    n_cases = 0
    for i in range(upper_bound, 0, -1):
        if i <= n:
            digits[i - 1] += 1
            n_cases += break_down(n - i, i, digits)
            digits[i - 1] -= 1

    return n_cases


break_down_ = functools.cache(lambda n: break_down(n))
for _ in range(int(readline())):
    print(break_down_(int(readline())))
