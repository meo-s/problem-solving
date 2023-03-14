# https://www.acmicpc.net/problem/1802

import sys
import math

readline = lambda: sys.stdin.readline().strip()


def check(orders, begin, end):
    if (end - begin) == 1:
        return True 

    mid = (end + begin) // 2
    for i in range(1, math.ceil((end - begin) / 2)):
        if orders[mid - i] == orders[mid + i]:
            return False

    return check(orders, begin, mid) and check(orders, mid, end)


for _ in range(int(readline())):
    orders = readline()
    print(['NO', 'YES'][check(orders, 0, len(orders))])
