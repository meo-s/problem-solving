// https://www.acmicpc.net/problem/2042

#include <iostream>
#include <cstdint>
#include <vector>
#include <iterator>
#include <algorithm>
#include <cmath>

using namespace std;
using sz = size_t;
using i32 = int32_t;
using i64 = int64_t;

struct SegmentTree {
    SegmentTree(vector<i64>& a) : a{a}, tree(1 << (sz) ceil(log2(a.size()) + 1)) {
        init(1, 0, a.size());
    }

    void init(i32 node, i32 beg, i32 end) {
        if (beg + 1 == end) tree[node] = a[beg];
        else {
            init(node * 2, beg, (beg + end) / 2);
            init(node * 2 + 1, (beg + end) / 2, end);
            tree[node] = tree[node * 2] + tree[node * 2 + 1];
        }
    }

    i64 query(i32 node, i32 beg, i32 end, i32 left, i32 right){ 
        if (right <= beg || end <= left) return 0;
        if (left <= beg && end <= right) return tree[node];
        auto const lq = query(node * 2, beg, (beg + end) / 2, left, right);
        auto const rq = query(node * 2 + 1, (beg + end) / 2, end, left, right);
        return lq + rq;
    }

    i64 query(i32 left, i32 right) {
        return query(1, 0, a.size(), left, right);
    }

    void update(i32 node, i32 beg, i32 end, i32 index, i64 value) {
        if (beg <= index && index < end) {
            if (beg + 1 == end) {
                tree[node] = a[index] = value;
                return;
            }

            update(node * 2, beg, (beg + end) / 2, index, value);
            update(node * 2 + 1, (beg + end) / 2, end, index, value);
            tree[node] = tree[node * 2] + tree[node * 2 + 1];
        }
    }

    void update(i32 index, i64 value) {
        update(1, 0, a.size(), index, value);
    }

    vector<i64>& a;
    vector<i64> tree;
};

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    sz N, M, K;
    cin >> N >> M >> K;

    auto seq = vector<i64>(N);
    copy_n(istream_iterator<i64>{cin}, N, begin(seq));

    auto tree = SegmentTree{seq};
    for (i32 i = 0; i < M + K; ++i) {
        i64 a, b, c; cin >> a >> b >> c;
        switch (a) {
        case 1:
            tree.update(b - 1, c);
            break;
        case 2:
            cout << tree.query(b - 1, c) << '\n';
            break;
        }
    }

    cout.flush();
    return 0;
}
