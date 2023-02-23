# https://www.acmicpc.net/problem/11840

import sys


def bin_trie_add(bin_trie, n, v, i=29):
    node = bin_trie
    while 0 <= i:
        if (bit := (n >> i) & 1) not in node:
            node[bit] = {}
        node = node[bit]
        i -= 1

    node['$'] = max(node.get('$', 0), v)
    return bin_trie


def bin_trie_max_xor(bin_trie, n, i=29):
    xor = 0
    node = bin_trie
    while 0 <= i:
        bit = (n >> i) & 1
        xor |= int((bit ^ 1) in node) << ((i := i - 1) + 1)
        node = node[[bit, bit ^ 1][(bit ^ 1) in node]]

    return (xor, node['$'])


readline = lambda: sys.stdin.readline().rstrip()  # noqa

N, x = map(int, readline().split())
seq = [*map(int, readline().split())]

bin_trie = {}
xor = 0
for i, a in enumerate(seq, start=1):
    bin_trie_add(bin_trie, (xor := xor ^ a), i)

xor = 0
i, k = 0, 0
for m, n in enumerate(seq, start=1):
    max_xor, j = bin_trie_max_xor(bin_trie, (xor := xor ^ n) ^ n)
    if x <= max_xor and k < j - m + 1:
        i, k = m, j - m + 1

print(i, k, sep=' ')
