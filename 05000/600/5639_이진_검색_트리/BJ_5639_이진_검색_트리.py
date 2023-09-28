# https://www.acmicpc.net/problem/5639

import io
import os
import sys
import operator
from dataclasses import dataclass


@dataclass
class Node:
    value: int
    parent: 'Node' = None
    left: 'Node' = None
    right: 'Node' = None


def reconstruct(preorder):
    root = Node(float('INF'))
    cur = root
    relations = []
    for n in preorder:
        for i, (relation, node) in enumerate(relations):
            if not relation(n, node.value):
                cur = relations[i][1]
                relations = relations[:i]
                break

        if n < cur.value:
            relations.append((operator.lt, cur))
            cur.left = Node(n, cur)
            cur = cur.left
        else:
            relations.append((operator.gt, cur))
            cur.right = Node(n, cur)
            cur = cur.right

    return root


def postorder(node):
    for child in [node.left, node.right]:
        if child is not None:
            postorder(child)
    if node.parent is not None:
        print(node.value)


sys.setrecursionlimit(10**4)
stdin = io.BytesIO(os.read(0, os.fstat(0).st_size))
readline = lambda: stdin.readline().decode().strip()

preorder = []
while 0 < len(n := readline()):
    preorder.append(int(n))

postorder(reconstruct(preorder))
