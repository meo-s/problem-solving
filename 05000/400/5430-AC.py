# https://www.acmicpc.net/problem/5430

import sys
from collections import deque

readline = lambda: sys.stdin.readline().strip()

for _ in range(int(readline())):
    funcs = readline()
    readline()

    nums = readline()[1:-1]
    nums = deque(map(int, nums.split(',')) if 0 < len(nums) else [])

    operations = [{'R': lambda: None, 'D': nums.popleft}, {'R': lambda: None, 'D': nums.pop}]

    reversed = 0
    try:
        for func in funcs:
            reversed ^= 1 if func == 'R' else 0
            operations[reversed][func]()

        print('[', ','.join(map(str, (operations[reversed]['D']() for _ in range(len(nums))))), ']', sep='')
    except IndexError:
        print('error')
