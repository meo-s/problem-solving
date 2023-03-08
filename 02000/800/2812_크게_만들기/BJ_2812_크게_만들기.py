# https://www.acmicpc.net/problem/2812

import sys


def readline():
    return sys.stdin.readline().rstrip()


N, k = map(int, readline().split())

stack = [float('inf')]
for n in map(int, readline()):
    while 0 < k and stack[-1] < n:
        stack.pop()
        k -= 1
    stack.append(n)

print(*stack[1:len(stack) - k], sep='')
