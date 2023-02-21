# https://www.acmicpc.net/problem/5670

import sys


class Trie:
    def __init__(self):
        self._prefixes = {}

    def add(self, s, v, i=0):
        if i == len(s):
            self._prefixes['*'] = v
            return

        if s[i] not in self._prefixes:
            self._prefixes[s[i]] = Trie()
        self._prefixes[s[i]].add(s, v, i + 1)

    def query(self, s, i=0):
        if i == len(s) - 1:
            return self._prefixes[s[i]] if s[i] in self._prefixes else None

        if s[i] not in self._prefixes:
            return None

        return self._prefixes[s[i]].query(s, i + 1)

    def next(self):
        return [*self._prefixes.values()][0]

    def list_next(self):
        return self._prefixes.items()

    def __len__(self):
        return len(self._prefixes) - int('*' in self._prefixes)


def count_inputs(n_words, trie):
    def traverse(node, depth=0):
        while len(node) == 1 and node.query('*') is None:
            node = node.next()

        if node.query('*') is not None:
            req_inputs[node.query('*')] = depth

        if 0 < len(node):
            for prefix, sub_trie in node.list_next():
                if prefix != '*':
                    traverse(sub_trie, depth + 1)

    req_inputs = [0] * n_words
    for _, sub_trie in trie.list_next():
        traverse(sub_trie, 1)

    return req_inputs


sys.set_int_max_str_digits(10**5)
readline = lambda: sys.stdin.readline().rstrip()

while 0 < len(N := readline()):
    N = int(N)
    trie = Trie()
    for i in range(N):
        trie.add(readline(), i)

    print('%.2f' % (sum(count_inputs(N, trie)) / N))
