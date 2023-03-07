# https://www.acmicpc.net/problem/1918


def postfix(exp):
    priorities = {'*': 1, '/': 1, '+': 2, '-': 2, ')': 3, '(': 4}
    postfix_, ops = [], []
    for c in exp:
        if ord('A') <= ord(c) <= ord('Z'):
            postfix_.append(c)
            continue
        if c == '(':
            ops.append(c)
            continue

        while 0 < len(ops) and (priorities[ops[-1]] <= priorities[c]):
            postfix_.append(ops.pop())

        ops.append(c) if c != ')' else ops.pop()

    while 0 < len(ops):
        if (c := ops.pop()) != '(':
            postfix_.append(c)

    return ''.join(postfix_)


print(postfix(input()))
