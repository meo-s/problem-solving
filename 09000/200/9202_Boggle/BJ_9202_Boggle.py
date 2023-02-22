# https://www.acmicpc.net/problem/9202

import sys


def trie_add(trie, s, i=0):
    node = trie
    while i < len(s):
        if s[i] not in node:
            node[s[i]] = {}
        node = node[s[i]]
        i += 1

    node['*'] = 0


def play_boggle(game_index, board, words):
    def dfs(y, x, prefixes, founds, depth=0, trace=[], visited=None):
        if visited is None:
            visited = [[False] * N for _ in range(N)]

        visited[y][x] = True

        if '*' in prefixes and prefixes['*'] != game_index:
            prefixes['*'] = game_index
            founds.append(''.join(trace))

        if depth < len(SCORES) - 1:
            for dy, dx in [(-1, 0), (-1, 1), (0, 1), (1, 1), (1, 0), (1, -1), (0, -1), (-1, -1)]:
                ny = y + dy
                nx = x + dx
                if ny < 0 or N <= ny or nx < 0 or N <= nx:
                    continue

                if not visited[ny][nx] and board[ny][nx] in prefixes:
                    trace.append(board[ny][nx])
                    dfs(ny, nx, prefixes[board[ny][nx]], founds, depth + 1, trace, visited)
                    trace.pop()

        visited[y][x] = False
        return founds

    trace = []
    all_founds = []
    for y in range(N):
        for x in range(N):
            if board[y][x] in words:
                trace.append(board[y][x])
                dfs(y, x, words[trace[0]], founds=all_founds, trace=trace)
                trace.pop()

    if 0 < len(all_founds):
        max_len = max(map(len, all_founds))
        longests = [found for found in all_founds if len(found) == max_len]
        longests = sorted(sorted(all_founds), key=len, reverse=True)
        print(sum(SCORES[len(found)] for found in all_founds), longests[0], len(all_founds), sep=' ')
    else:
        print('0 0')


readline = lambda: sys.stdin.readline().rstrip()  # noqa
SCORES = [0, 0, 0, 1, 1, 2, 3, 5, 11]

words = {}
for _ in range(int(readline())):
    trie_add(words, readline())

readline()

N = 4
for i in range(int(readline())):
    readline() if 0 < i else None
    play_boggle(i + 1, [readline() for _ in range(N)], words)
