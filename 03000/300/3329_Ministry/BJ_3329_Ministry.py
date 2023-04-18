# https://www.acmicpc.net/problem/3329

import sys
from collections import deque


def extract_edges(tree, edges, offset=0, depth=0):
    u = len(edges)
    edges.append([])

    max_depth = depth
    while tree[offset + 1] != ')':
        edges[u].append(len(edges))
        offset, subtree_depth = extract_edges(tree, edges, offset + 1, depth + 1)
        max_depth = max(max_depth, subtree_depth)

    return offset + 1, max_depth


def postorder(edges, hashs, placeholders, u=0):
    subtrees = deque([(-1, 0, '(')])
    tree_size = 1
    tree_depth = 0
    for v in edges[u]:
        subtree = postorder(edges, hashs, placeholders, v)
        tree_size += subtree[0]
        tree_depth = max(tree_depth, subtree[1] + 1)
        subtrees.append(subtree)

    subtrees = [subtree[-1] for subtree in sorted(subtrees)]
    subtrees.append(')')
    tree_hash = ''.join(subtrees)
    if tree_hash not in placeholders:
        placeholders[tree_hash] = f'{len(placeholders)})'

    tree_hash = placeholders[tree_hash]
    hashs[tree_depth].add(tree_hash)
    return tree_size, tree_depth, tree_hash


sys.setrecursionlimit(10**6 + 1)
readline = lambda: sys.stdin.readline().rstrip()  # noqa

V, D = extract_edges(readline(), (edges := []))

hashs = [set() for _ in range(D + 1)]
postorder(edges, hashs, {})
print(*[len(uniques) for uniques in hashs], sep='\n')
