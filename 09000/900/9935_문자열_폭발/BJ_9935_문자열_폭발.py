# https://www.acmicpc.net/problem/9935

string, bomb = input(), input()
bomb = [*bomb]

stack = []
for c in string:
    stack.append(c)
    while stack[-len(bomb):] == bomb:
        for _ in range(len(bomb)):
            stack.pop()

print(['FRULA', ''.join(stack)][0 < len(stack)])
