# https://www.acmicpc.net/problem/1543

cnt = 0
doc, query = input(), input()
while len(query) <= len(doc):
    if doc.startswith(query):
        cnt += 1
        doc = doc[len(query):]
    else:
        doc = doc[1:]

print(cnt)
