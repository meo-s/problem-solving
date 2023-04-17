# https://www.acmicpc.net/problem/12218

import sys
from operator import itemgetter


def centroids_of(edges, centroids, u=0, p=-1):
    tree_size = 1
    is_centroid = True
    for v in edges[u]:
        if v != p:
            tree_size += (subtree_size := centroids_of(edges, centroids, v, u))
            is_centroid = is_centroid and (subtree_size <= len(edges) // 2)

    if is_centroid and len(edges) / 2 <= tree_size:
        centroids.append(u)

    return tree_size


def hash(vertices, edges, u, p=-1):
    subtrees = {}
    for v in edges[u]:
        if v != p:
            subtree = hash(vertices, edges, v, u)
            subtrees[subtree] = subtrees.get(subtree, 0) + 1

    is_symmetric = True
    n_self_symmetrics = 0
    for subtree, subtree_count in subtrees.items():
        if not is_symmetric:
            break
        if subtree_count % 2 == 1:
            is_symmetric = subtree[0] and n_self_symmetrics < 1 + int(p == -1)
            n_self_symmetrics += 1

    tree_size = 1
    tree_hash = [vertices[u]]
    for subtree, subtree_count in sorted(subtrees.items(), key=itemgetter(0)):
        _, subtree_size, subtree_hash = subtree
        tree_size += subtree_size * subtree_count
        tree_hash += [subtree_hash] * subtree_count

    tree_hash.append(')')
    return is_symmetric, tree_size, ''.join(tree_hash)


readline = lambda: sys.stdin.readline().rstrip()  # noqa

for t in range(int(readline())):
    N = int(readline())
    vertices = [readline() for _ in range(N)]

    edges = [[] for _ in range(N)]
    for _ in range(N - 1):
        u, v = map(int, readline().split())
        u, v = u - 1, v - 1
        edges[u].append(v)
        edges[v].append(u)

    centroids = []
    centroids_of(edges, centroids)

    centroid = [centroids[0], N][len(centroids) == 2]
    if len(centroids) == 2:
        edges[centroids[0]].remove(centroids[1])
        edges[centroids[1]].remove(centroids[0])

        edges.append([])
        vertices.append('R')
        for c in centroids:
            edges[N].append(c)
            edges[c].append(N)

    print(f'Case #{t + 1}: ' + ['NOT SYMMETRIC', 'SYMMETRIC'][hash(vertices, edges, centroid)[0]])
