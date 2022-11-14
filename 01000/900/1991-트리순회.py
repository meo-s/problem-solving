# https://www.acmicpc.net/problem/1991

import sys
from collections import namedtuple

Node = namedtuple('Node', ['left_child', 'right_child'])

def preorder_traverse(nodes, cur):
    sys.stdout.write(cur)
    for child in nodes[cur]:
        preorder_traverse(nodes, child) if child is not None else _


def inorder_traverse(nodes, cur):
    left_child, right_child = nodes[cur]
    inorder_traverse(nodes, left_child) if left_child is not None else _
    sys.stdout.write(cur)
    inorder_traverse(nodes, right_child) if right_child is not None else _

def postorder_traverse(nodes, cur):
    for child in nodes[cur]:
        postorder_traverse(nodes, child) if child is not None else _
    sys.stdout.write(cur)


nodes = {}
for _ in range(int(input())):
    root, lc, rc = input().split()
    nodes[root] = Node(lc if lc != '.' else None, rc if rc != '.' else None)

for traverse in [preorder_traverse, inorder_traverse, postorder_traverse]:
    traverse(nodes, 'A')
    print()
