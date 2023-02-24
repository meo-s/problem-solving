# https://www.acmicpc.net/problem/13504

import sys


def bin_trie_add(trie, n, i=31):
    node = trie
    while 0 < i:
        bit = (n >> (i := i - 1)) & 1
        if bit not in node:
            node[bit] = {}
        node = node[bit]


def bin_trie_max_xor(trie, n, i=31):
    node, xor = trie, 0
    while 0 < i:
        bit = (n >> (i := i - 1)) & 1
        xor |= int(bit ^ 1 in node) << i
        node = node[[bit, bit ^ 1][bit ^ 1 in node]]

    return xor


def trie_clear(trie):
    for node in trie.values():
        trie_clear(node)
    del trie


readline = lambda: sys.stdin.readline().rstrip()  # noqa

for _ in range(int(readline())):
    bin_trie = {}

    readline()
    seq = [*map(int, readline().split())]

    xor = 0
    for n in seq:
        bin_trie_add(bin_trie, (xor := xor ^ n))

    max_xor, xor = 0, 0
    for n in seq:
        max_xor = max(max_xor, bin_trie_max_xor(bin_trie, (xor := xor ^ n) ^ n))

    print(max_xor)

    del seq
    trie_clear(bin_trie)
    seq = None
    bin_trie = None
