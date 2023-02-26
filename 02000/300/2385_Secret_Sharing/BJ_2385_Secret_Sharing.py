# https://www.acmicpc.net/problem/2385

import functools


def cmp_piece(p1, p2):
    return [-1, 1][p2 + p1 <= p1 + p2]


N = int(input())

pieces = [[], []]
for piece in input().split():
    pieces[piece[0] != '0'].append(piece)

if 0 < len(pieces[0]) and len(pieces[1]) == 0:
    print('INVALID')
else:
    for p in pieces:
        p.sort(key=functools.cmp_to_key(cmp_piece))

    if 0 == len(pieces[0]):
        print(*pieces[1], sep='')
    else:
        print(min([''.join([p, *pieces[0], *pieces[1][:i], *pieces[1][i + 1:]]) for i, p in enumerate(pieces[1])]))
