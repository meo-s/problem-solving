# https://www.acmicpc.net/problemre

import sys


def reconstruct(inorder, io_lb, io_ub, postorder, po_lb, po_ub):
    if io_ub <= io_lb:
        return

    root = postorder[po_ub - 1]
    assert (root_index := inorder.index(root, io_lb, io_ub)) != -1

    print(root, end=' ')
    if 0 < root_index:
        reconstruct(inorder, io_lb, root_index, postorder, po_lb, po_lb + (root_index - io_lb))
    if root_index < io_ub - 1:
        reconstruct(inorder, root_index + 1, io_ub, postorder, po_lb + (root_index - io_lb), po_ub - 1)


sys.setrecursionlimit(100001)
readline = lambda: sys.stdin.readline().strip()

N = int(readline())
inorder = [*map(int, readline().split())]
postorder = [*map(int, readline().split())]
reconstruct(inorder, 0, N, postorder, 0, N)
