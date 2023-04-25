# https://www.acmicpc.net/problem/5812

import math
import sys


class Node:

    __slots__ = ['value', 'depth', 'parents']
    L = math.ceil(math.log2(1e+6)) + 1

    def __init__(self, v=None, p=None):
        self.value = v
        self.depth = p.depth + 1 if p is not None else -1
        self.parents = [p] + [None] * (Node.L - 1)
        for i in range(1, Node.L):
            if self.parents[i - 1] is not None:
                self.parents[i] = self.parents[i - 1].parents[i - 1]


readline = lambda: sys.stdin.readline().rstrip()  # noqa

cursor = Node()
history = []
for _ in range(int(readline())):
    q, arg = readline().split()
    if q == 'T':
        history.append(cursor)
        cursor = Node(arg, cursor)
    elif q == 'U':
        history.append(cursor)
        cursor = history[max(-(int(arg) + 1), -len(history))]
    else:
        n = cursor.depth - int(arg)
        node = cursor
        for i in reversed(range(Node.L)):
            if n == 0:
                break
            if (1 << i) <= n:
                n -= 1 << i
                node = node.parents[i]

        print(node.value)
