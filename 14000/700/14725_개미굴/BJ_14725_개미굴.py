# https://www.acmicpc.net/problem/14725

import sys
import operator


class WordTrie:
    def __init__(self):
        self._words = {}

    def add(self, k, i=0):
        if i == len(k):
            return self
        if k[i] not in self._words:
            self._words[k[i]] = WordTrie()
        return self._words[k[i]].add(k, i + 1)

    def traverse(self, depth=0):
        for word, sub_trie in sorted(self._words.items(), key=operator.itemgetter(0)):
            print('--' * depth, word, sep='')
            sub_trie.traverse(depth + 1)


readline = lambda: sys.stdin.readline().rstrip()

trie = WordTrie()
for _ in range(int(readline())):
    _, *keywords = readline().split()
    trie.add(keywords)

trie.traverse()
