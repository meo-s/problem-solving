// https://www.acmicpc.net/problem/14428

#include <algorithm>
#include <cmath>
#include <iostream>
#include <iterator>
#include <vector>

using namespace std;

struct SegmentTree {

    SegmentTree(vector<int>& a) : a{a}, tree(1 << ((int)ceil(log2(a.size()) + 1))) { init(1, 0, a.size()); }

    void init(int node, int beg, int end) {
        if (beg + 1 == end) tree[node] = beg;
        else {
            init(node * 2, beg, (beg + end) / 2);
            init(node * 2 + 1, (beg + end) / 2, end);
            tree[node] = a[tree[node * 2]] <= a[tree[node * 2 + 1]] ? tree[node * 2] : tree[node * 2 + 1];
        }
    }

    int query(int node, int beg, int end, int left, int right) {
        if (right <= beg || end <= left) return -1;
        if (left <= beg && end <= right) return tree[node];

        auto const lq = query(node * 2, beg, (beg + end) / 2, left, right);
        auto const rq = query(node * 2 + 1, (beg + end) / 2, end, left, right);
        if (lq == -1) return rq;
        if (rq == -1) return lq;
        return a[lq] <= a[rq] ? lq : rq;
    }

    int query(int left, int right) { return query(1, 0, a.size(), left, right); }

    void update(int node, int beg, int end, int index, int value) {
        if (beg <= index && index < end) {
            if (beg + 1 == end) a[index] = value;
            else {
                update(node * 2, beg, (beg + end) / 2, index, value);
                update(node * 2 + 1, (beg + end) / 2, end, index, value);
                tree[node] = a[tree[node * 2]] <= a[tree[node * 2 + 1]] ? tree[node * 2] : tree[node * 2 + 1];
            }
        }
    }

    void update(int index, int value) { update(1, 0, a.size(), index, value); }

    vector<int>& a;
    vector<int> tree;
};

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int N;
    cin >> N;
    auto seq = vector<int>(N);
    copy_n(istream_iterator<int>{cin}, N, seq.begin());

    auto tree = SegmentTree{seq};
    int Q;
    cin >> Q;
    while (0 <= --Q) {
        int a, b, c;
        cin >> a >> b >> c;
        switch (a) {
        case 1:
            tree.update(b - 1, c);
            break;
        case 2:
            cout << tree.query(b - 1, c) + 1 << '\n';
            break;
        }
    }

    cout.flush();
    return 0;
}
