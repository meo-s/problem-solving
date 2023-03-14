# https://www.acmicpc.net/problem/1082

import sys
import operator
from collections import deque


def readline():
    return sys.stdin.readline().rstrip()


N = int(readline())
nums = [(i, p) for i, p in enumerate(map(int, readline().split()))]
nums.sort(key=operator.itemgetter(-1))

balance = int(readline()) - nums[0][1]
transactions = [0] * 10
transactions[nums[0][0]] = int(0 <= balance)

first = nums[0][0]
if transactions[0] == 1:
    if 1 < len(nums):
        if (price_diff := nums[1][1] - nums[0][1]) <= balance:
            balance -= price_diff
            first = nums[1][0]
            transactions[0] = 0
            transactions[first] = 1

if 0 <= balance and transactions[0] != 1:
    cheap = nums[0]

    max_digits = 1 + (balance // cheap[1])
    transactions[cheap[0]] += max_digits - 1
    balance -= cheap[1] * (max_digits - 1)

    num_pivot = 0
    nums.sort(key=operator.itemgetter(0))

    for i in reversed(range(len(nums))):
        if (price_diff := nums[i][1] - nums[first][1]) <= balance:
            num_pivot = i
            transactions[first] -= 1
            transactions[nums[num_pivot][0]] += 1
            balance -= price_diff
            break

    n_remains = max_digits - 1
    while 0 < n_remains:
        price_diff = nums[num_pivot][1] - cheap[1]
        if price_diff <= balance:
            if price_diff == 0:
                transactions[cheap[0]] -= n_remains
                transactions[nums[num_pivot][0]] += n_remains
                n_remains = 0
            else:
                n_remains -= (n := min(balance // price_diff, n_remains))
                transactions[cheap[0]] -= n
                transactions[nums[num_pivot][0]] += n
                balance -= price_diff * n

        num_pivot -= 1

if (n_transactions := sum(transactions)) == 0:
    print('0\n\n')
else:
    head = deque([])
    i, remains = len(nums) - 1, [*transactions]
    while len(head) < min(n_transactions, 50):
        if 0 < remains[i]:
            remains[i] -= 1
            head.append(i)
        else:
            i -= 1

    tail = deque([])
    i, remains = 0, [*transactions]
    while len(tail) < min(n_transactions, 50):
        if 0 < remains[i]:
            remains[i] -= 1
            tail.appendleft(i)
        else:
            i += 1

    print(n_transactions)
    print(*head, sep='')
    print(*tail, sep='')
