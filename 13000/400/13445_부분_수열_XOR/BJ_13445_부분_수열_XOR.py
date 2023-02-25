# https://www.acmicpc.net/problem/13445


def bin_trie_add(trie, n, i=20):
    node = trie
    while 0 <= (i := i - 1):
        bit = (n >> i) & 1
        if bit not in node:
            node[bit] = {}
        node['$'] = node.get('$', 0) + 1
        node = node[bit]
    node['$'] = node.get('$', 0) + 1


def bin_trie_remove(trie, n, i=20):
    node = trie
    while 0 <= (i := i - 1):
        bit = (n >> i) & 1
        node['$'] -= 1
        node = node[bit]
    node['$'] -= 1


def bin_trie_query(trie, n, k, i=20):
    less = 0
    node = trie
    while 0 <= (i := i - 1) and node is not None:
        nbit, kbit = (n >> i) & 1, (k >> i) & 1
        if nbit == kbit:
            less += node[1]['$'] if nbit == 1 and 1 in node else 0
            node = node.get(0, None)
        else:
            less += node[0]['$'] if nbit == 0 and 0 in node else 0
            node = node.get(1, None)

    return less


N, K = map(int, input().split())
seq = [*map(int, input().split())]

bin_trie = {}

xor = 0
for n in seq:
    bin_trie_add(bin_trie, (xor := xor ^ n))

ans = 0
xor = 0
for i, n in enumerate(seq):
    bin_trie_remove(bin_trie, xor) if 0 < i else None
    ans += bin_trie_query(bin_trie, xor, K)
    xor ^= n

print(ans)
