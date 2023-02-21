# https://www.acmicpc.net/problem/4358

import os
import io

readline = io.BytesIO(os.read(0, os.fstat(0).st_size)).readline

total = 0
trees = {}
while 0 < len(tree := readline().decode().rstrip()):
    trees[tree] = trees.get(tree, 0) + 1
    total += 1

for k in sorted(trees.keys()):
    print('%s %.4f' % (k, trees[k] / total * 1e+2))
