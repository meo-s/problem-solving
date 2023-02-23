# https://www.acmicpc.net/problem/5446

import sys


def trie_add(trie, s, i=0):
    node = trie
    while i < len(s):
        if s[i] not in node:
            node[s[i]] = {}
        node = node[s[i]]
        i += 1

    node['$'] = 0


def trie_mark(trie, s, i=0):
    node = trie
    while i < len(s) and s[i] in node:
        node['-'] = 0
        node = node[s[i]]
        i += 1

    node['-'] = 0


def count_rm(trie):
    n_rms = 1 - int('-' in trie)
    if '-' in trie:
        for prefix in trie.keys():
            n_rms += int(prefix == '$')
            n_rms += count_rm(trie[prefix]) if prefix not in '-$' else 0

    return n_rms


readline = lambda: sys.stdin.readline().rstrip()  # noqa
for _ in range(int(readline())):
    trie = {}
    for _ in range(int(readline())):
        trie_add(trie, readline())
    for _ in range(int(readline())):
        trie_mark(trie, readline())

    print(count_rm(trie))
