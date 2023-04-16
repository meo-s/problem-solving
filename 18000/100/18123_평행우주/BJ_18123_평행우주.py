# https://www.acmicpc.net/problem/18123

import sys
from collections import deque
from operator import itemgetter


def centroids_of(tree, centroids, u=0, p=-1):
    is_centroid = True
    tree_size = 1
    for v in tree[u]:
        if v != p:
            tree_size += (subtree_size := centroids_of(tree, centroids, v, u))
            is_centroid = is_centroid and subtree_size <= len(tree) // 2

    if is_centroid and len(tree) / 2 <= tree_size:
        centroids.append(u)

    return tree_size


def hash(tree, u, p=-1):
    subtrees = []
    for v in tree[u]:
        subtrees.append(hash(tree, v, u)) if v != p else None

    tree_size = 1
    tree_hash = 0b1
    for subtree_size, subtree_hash in sorted(subtrees, key=itemgetter(1)):
        tree_size += subtree_size
        tree_hash = (tree_hash << (2 * subtree_size)) | subtree_hash

    return tree_size, tree_hash << 1


readline = lambda: sys.stdin.readline().rstrip()  # noqa

unique_trees = set()
for _ in range(int(readline())):
    V = int(readline())
    edges = [[] for _ in range(V)]
    for _ in range(V - 1):
        u, v = map(int, readline().split())
        edges[u].append(v)
        edges[v].append(u)

    centroids = []
    centroids_of(edges, centroids)

    tree_hash = max(hash(edges, centroid) for centroid in centroids)
    unique_trees.add(tree_hash)

print(len(unique_trees))
