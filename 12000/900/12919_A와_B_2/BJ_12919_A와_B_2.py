# https://www.acmicpc.net/problem/12919


def backpropagate(goal, s):
    if len(s) == len(goal):
        return s == goal
    return (s.endswith('A') and backpropagate(goal, s[:-1])) or \
        (s.startswith('B') and backpropagate(goal, s[1:][::-1]))


print(int(backpropagate(input(), input())))
