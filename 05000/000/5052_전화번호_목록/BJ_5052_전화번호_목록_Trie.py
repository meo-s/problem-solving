# https://www.acmicpc.net/problem/5052

import sys


class Trie:
    def __init__(self):
        self._prefixes = {}

    def add(self, s, i=0):
        if '*' in self._prefixes:
            return False

        if i == len(s):
            self._prefixes['*'] = self._prefixes.get('*', 0) + 1
            return True
        if s[i] not in self._prefixes:
            self._prefixes[s[i]] = Trie()
        return self._prefixes[s[i]].add(s, i + 1)


readline = lambda: sys.stdin.readline().rstrip()

for _ in range(int(readline())):
    trie = Trie()
    is_consistent = True 
    for tel in sorted((readline() for _ in range(int(readline()))), key=len):
        if not trie.add(tel):
            is_consistent = False
            break

    print(['NO', 'YES'][is_consistent])
