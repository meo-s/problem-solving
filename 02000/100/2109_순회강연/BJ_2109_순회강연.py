# https://www.acmicpc.net/problem/2109

import sys
import operator


def readline():
    return sys.stdin.readline().rstrip()


N = int(readline())
lectures = [tuple(map(int, readline().split())) for _ in range(N)]
lectures.sort(key=operator.itemgetter(0), reverse=True)

days = [*range(1, N + 1)]
max_pay = 0
for pay, due in lectures:
    lb, ub = 0, min(due, len(days))
    while lb < ub:
        mid = (lb + ub) // 2
        if days[mid] <= due:
            lb = mid + 1
        else:
            ub = mid

    lb = max(0, lb - 1)
    if days[lb] <= due:
        days.pop(lb)
        max_pay += pay

print(max_pay)
