# https://www.acmicpc.net/problem/14425

import sys


class Trie:
    def __init__(self):
        self._prefixes = {}

    def add(self, s, i=0):
        if i == len(s):
            self._prefixes['*'] = None
            return
        if s[i] not in self._prefixes:
            self._prefixes[s[i]] = Trie()
        self._prefixes[s[i]].add(s, i + 1)

    def query(self, s, i=0):
        if i == len(s):
            return '*' in self._prefixes
        if s[i] in self._prefixes:
            return self._prefixes[s[i]].query(s, i + 1)
        return False


def readline():
    return sys.stdin.readline().rstrip()


N, M = map(int, readline().split())
trie = Trie()
for _ in range(N):
    trie.add(readline())

print(sum(int(trie.query(readline())) for _ in range(M)))