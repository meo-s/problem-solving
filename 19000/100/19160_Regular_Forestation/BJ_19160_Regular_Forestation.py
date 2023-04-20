# https://www.acmicpc.net/problem/19160

import sys
from collections import deque
from collections import defaultdict
from operator import itemgetter


def extract_subtree(tree, u, p):
    subtree = defaultdict(list)
    indices = {u: 0}

    nodes = deque([(u, p)])
    while 0 < len(nodes):
        u, p = nodes.popleft()
        ui = indices[u]

        for v in tree[u]:
            if v != p:
                if v not in indices:
                    indices[v] = len(indices)

                vi = indices[v]
                subtree[ui].append(vi)
                subtree[vi].append(ui)
                nodes.append((v, u))

    return [subtree[i] for i in range(len(indices))]


def centroids_of(tree, centroids, u=0, p=-1):
    tree_size = 1
    is_centroid = True
    for v in tree[u]:
        if v != p:
            tree_size += (subtree_size := centroids_of(tree, centroids, v, u))
            is_centroid = is_centroid and subtree_size <= len(tree) / 2

    if is_centroid and len(tree) / 2 <= tree_size:
        centroids.append(u)

    return tree_size


def hash_tree(tree, uniques, u, p=-1):
    subtrees = {}
    for v in tree[u]:
        if v != p:
            subtree_index = hash_tree(tree, uniques, v, u)
            subtrees[subtree_index] = subtrees.get(subtree_index, 0) + 1

    subtree_hashs = ['R']
    for subtree_index, subtree_count in sorted(subtrees.items(), key=itemgetter(0)):
        subtree_hashs += [f'{subtree_index})'] * subtree_count

    tree_hash = ''.join(subtree_hashs)
    if tree_hash not in uniques:
        uniques[tree_hash] = len(uniques)

    return uniques[tree_hash]


sys.setrecursionlimit(4 * 10**3)
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
tree = [[] for _ in range(N)]
for _ in range(N - 1):
    u, v = map(int, readline().split())
    u, v = u - 1, v - 1
    tree[u].append(v)
    tree[v].append(u)

centroids_of(tree, (cp := []))
if len(cp) == 2:
    print(-1)
else:
    unique_trees = {}
    subtree_indices = set()
    for u in tree[cp[0]]:
        subtree = extract_subtree(tree, u, cp[0])

        centroids_of(subtree, (centroids := []))
        if len(centroids) == 2:
            subtree.append(centroids)
            for i in range(2):
                for j in range(len(subtree[centroids[i]])):
                    if subtree[centroids[i]][j] == centroids[i ^ 1]:
                        subtree[centroids[i]][j] = len(subtree) - 1

        centroid = [centroids[0], len(subtree) - 1][len(centroids) == 2]
        subtree_indices.add(hash_tree(subtree, unique_trees, centroid))

    print([-1, len(tree[cp[0]])][len(subtree_indices) == 1])
