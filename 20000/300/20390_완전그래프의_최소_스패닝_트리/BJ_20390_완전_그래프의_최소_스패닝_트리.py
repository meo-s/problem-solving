# https://www.acmicpc.net/problem/20390


def dist_of(i, j):
    if j < i:
        i, j = j, i
    return ((X[i] * A + X[j] * B) % C) ^ D


def find_next(visited, edges):
    u = 0
    for i in range(1, N):
        if not visited[i]:
            if u == 0 or edges[i] < edges[u]:
                u = i
    return u


N = int(input())
A, B, C, D = map(int, input().split())
X = [*map(int, input().split())]

visited = [True] + [False] * (N - 1)
min_edges = [float('inf')] * N

for i in range(1, N):
    min_edges[i] = dist_of(0, i)

n_edges = 0
mst_weight = 0
while n_edges < N - 1:
    u = find_next(visited, min_edges)
    visited[u] = True

    n_edges += 1
    mst_weight += min_edges[u]

    for v in range(1, N):
        if u != v and not visited[v]:
            min_edges[v] = min(min_edges[v], dist_of(u, v))

print(mst_weight)
