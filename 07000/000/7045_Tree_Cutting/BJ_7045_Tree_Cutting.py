# https://www.acmicpc.net/problem/7045

import sys


def centroids_of(edges, centroids, u=0, p=-1):
    is_centroid = True
    tree_size = 1
    for v in edges[u]:
        if v != p:
            tree_size += (subtree_size := centroids_of(edges, centroids, v, u))
            is_centroid = is_centroid and subtree_size <= len(edges) // 2

    is_centroid = is_centroid and len(edges) / 2 <= tree_size
    centroids.append(u) if is_centroid else None
    return tree_size


sys.setrecursionlimit(10**5 + 1)
readline = lambda: sys.stdin.readline().rstrip()  # noqa

N = int(readline())
edges = [[] for _ in range(N)]
for _ in range(N - 1):
    u, v = map(int, readline().split())
    edges[u - 1].append(v - 1)
    edges[v - 1].append(u - 1)

centroids = []
centroids_of(edges, centroids)
if len(centroids) == 0:
    print('NONE')
else:
    print(*sorted(centroid + 1 for centroid in centroids), sep='\n')

