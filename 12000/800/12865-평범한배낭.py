# https://www.acmicpc.net/problem/12865

N, K = map(int, input().split())
items = [tuple(map(int, input().split())) for _ in range(N)]
items = sorted(items, key=lambda v: v[1], reverse=True)
items = sorted(items, key=lambda v: v[0])

cache = [[None] * N for _ in range(K + 1)]

def best_pick(empty_space, item_offset):
    if item_offset == N:
        return N, empty_space, 0

    best = (None, empty_space, 0)
    for i in range(item_offset, N):
        if empty_space < items[i][0]:
            break

        _, pe, pv = best_pick(empty_space - items[i][0], i + 1)
        if best[-1] < pv + items[i][1]:
            best = (i, pe, pv + items[i][1])

    print(best)
    return best

print(best_pick(K, 0))
