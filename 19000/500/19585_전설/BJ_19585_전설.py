# https://www.acmicpc.net/problem/19585

import sys


class Trie:
    def __init__(self):
        self._prefixes = {}

    def add(self, s, i=0):
        prefixes = self._prefixes
        while i < len(s):
            if s[i] not in prefixes:
                prefixes[s[i]] = {}
            prefixes = prefixes[s[i]]
            i += 1

        prefixes['*'] = prefixes.get('*', 0) + 1

    @property
    def prefixes(self):
        return self._prefixes


def is_legend(colors, nicknames, teamname):
    i = 0
    color_prefixes = colors.prefixes
    while i < len(teamname):
        if '*' in color_prefixes and teamname[i:] in nicknames:
            return True

        if teamname[i] not in color_prefixes:
            break

        color_prefixes = color_prefixes[teamname[i]]
        i += 1

    return False


readline = lambda: sys.stdin.readline().rstrip()  # noqa

C, N = map(int, readline().split())

colors = Trie()
for _ in range(C):
    colors.add(readline())
nicknames = set()
for _ in range(N):
    nicknames.add(readline())

for _ in range(int(readline())):
    print(['No', 'Yes'][is_legend(colors, nicknames, readline())])
