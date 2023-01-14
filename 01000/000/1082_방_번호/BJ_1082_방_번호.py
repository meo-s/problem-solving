# https://www.acmicpc.net/problem/1082

import sys
import operator


def readline():
    return sys.stdin.readline().rstrip()


N = int(readline())
nums = [(i, p) for i, p in enumerate(map(int, readline().split()))]
nums.sort(key=operator.itemgetter(-1))

room = [0]
balance = int(readline()) - nums[0][1]
if nums[0][0] == 0:
    if 1 < len(nums) and 0 <= (balance - (price_diff := nums[1][1] - nums[0][1])):
        balance -= price_diff
        room[0] = 1

if nums[room[0]][0] != 0:
    room = room + [0] * (balance // nums[0][1])
    balance -= nums[0][1] * (balance // nums[0][1])

    i = -1
    while i < len(room) - 1:
        i += 1
        for j in range(len(nums)):
            if nums[room[i]][0] < nums[j][0]:
                if 0 <= (balance - (price_diff := nums[j][1] - nums[room[i]][1])):
                    room[i] = j
                    balance -= price_diff

for i in room:
    print(nums[i][0], end='')
