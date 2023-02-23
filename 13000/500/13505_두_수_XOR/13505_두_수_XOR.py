# https://www.acmicpc.net/problem/13505

import sys


def bin_trie_add(trie, n, i=29):
    node = trie
    while 0 <= i:
        bit = (n >> i) & 1
        if bit not in node:
            node[bit] = {}
        node = node[bit]
        i -= 1

    return trie


def bin_trie_maximum_xor(trie, n, i=29):
    xor = 0
    node = trie
    while 0 <= i:
        bit = (n >> i) & 1
        xor |= int((bit ^ 1) in node) << ((i := i - 1) + 1)
        node = node[[bit, bit ^ 1][(bit ^ 1) in node]]

    return xor


readline = lambda: sys.stdin.readline().rstrip()  # noqa

readline()
bin_trie = bin_trie_add({}, (nums := [*map(int, readline().split())])[0])

max_xor = 0
for n in nums[1:]:
    max_xor = max(max_xor, bin_trie_maximum_xor(bin_trie, n))
    bin_trie_add(bin_trie, n)

print(max_xor)
