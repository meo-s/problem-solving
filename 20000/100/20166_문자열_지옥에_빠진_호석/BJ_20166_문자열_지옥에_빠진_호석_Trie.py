# https://www.acmicpc.net/problem/20166

import sys


class Trie:
    def __init__(self):
        self._prefixs = {}

    def add(self, s='', i=0):
        if i == len(s):
            self._prefixs['*'] = self._prefixs.get('*', 0) + 1
            return

        if s[i] not in self._prefixs:
            self._prefixs[s[i]] = Trie()
        self._prefixs[s[i]].add(s, i + 1)

    def query_node(self, s, i=0):
        if i == len(s):
            return self

        return self._prefixs[s[i]].query_node(s, i + 1)

    def query(self, s, i=0):
        if i == len(s):
            return self._prefixs.get('*', 0)
        if s[i] not in self._prefixs:
            return 0
        return self._prefixs[s[i]].query(s, i + 1)


def cache_all_words(trie, board):
    def dfs(node, y, x, depth=1):
        node.add(board[y][x])
        node = node.query_node(board[y][x])

        if depth <= 5:
            for dy, dx in [(-1, 0), (-1, 1), (0, 1), (1, 1), (1, 0), (1, -1), (0, -1), (-1, -1)]:
                ny = (H + y + dy) % H
                nx = (W + x + dx) % W
                dfs(node, ny, nx, depth + 1)

    for y in range(H):
        for x in range(W):
            dfs(trie, y, x)


readline = lambda: sys.stdin.readline().rstrip()
H, W, K = map(int, readline().split())

board = [readline() for _ in range(H)]
cache_all_words((trie := Trie()), board)

for word in (readline() for _ in range(K)):
    print(trie.query(word))
