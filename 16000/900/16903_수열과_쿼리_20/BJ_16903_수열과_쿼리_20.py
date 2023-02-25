# https://www.acmicpc.net/problem/16903

import sys


def bin_trie_add(bin_trie, n, i=30):
    node = bin_trie
    while 0 <= (i := i - 1):
        bit = (n >> i) & 1
        if bit not in node:
            node[bit] = {}
        node['$'] = node.get('$', 0) + 1
        node = node[bit]

    node['$'] = node.get('$', 0) + 1


def bin_trie_remove(bin_trie, n, i=30):
    node = bin_trie
    while 0 <= (i := i - 1):
        bit = (n >> i) & 1
        node['$'] -= 1
        node = node[bit]

    node['$'] -= 1


def bin_trie_max_xor(bin_trie, n, i=30):
    xor = 0
    node = bin_trie
    while 0 <= (i := i - 1):
        bit = (n >> i) & 1
        xor |= int(bit ^ 1 in node and 0 < node[bit ^ 1]['$']) << i
        node = node[[bit, bit ^ 1][bit ^ 1 in node and 0 < node[bit ^ 1]['$']]]

    return xor


readline = lambda: sys.stdin.readline().rstrip()  # noqa

bin_trie = {}
bin_trie_add(bin_trie, 0)

fn = [bin_trie_add, bin_trie_remove]
for _ in range(int(readline())):
    q, x = map(int, readline().split())
    if q < 3:
        fn[q - 1](bin_trie, x)
    else:
        print(bin_trie_max_xor(bin_trie, x))
