# https://www.acmicpc.net/problem/4358

import io
import os


class Trie:
    def __init__(self):
        self.prefixes = {}

    def add(self, s, i=0):
        if i == len(s):
            self.prefixes['**'] = self.prefixes.get('**', 0) + 1
            return
        if s[i] not in self.prefixes:
            self.prefixes[s[i]] = Trie()
        self.prefixes[s[i]].add(s, i + 1)

    def preorder(self, callback, trace=[]):
        if '**' in self.prefixes:
            callback(trace, self.prefixes['**'])

        for c in sorted(self.prefixes.keys()):
            if c != '**':
                trace.append(c)
                self.prefixes[c].preorder(callback, trace)
                trace.pop()


inbuf = io.BytesIO(os.read(0, os.fstat(0).st_size))
readline = lambda: inbuf.readline().decode().rstrip()

trie = Trie()
total = 0
while 0 < len(tree := readline()):
    trie.add(tree)
    total += 1

trie.preorder(lambda k, v: print(*k, ' ', f'{(v / total * 1e+2):.4f}', sep=''))
