// https://www.acmicpc.net/problem/2357

#include <algorithm>
#include <cmath>
#include <iostream>
#include <iterator>
#include <vector>

using namespace std;
using i32 = int;

struct SegmentTree {
    vector<i32> const& a;
    vector<pair<i32, i32>> tree;

    SegmentTree(vector<i32> const& a) : a{a}, tree(1 << (i32)ceil(log2(a.size()) + 1)) { init(1, 0, a.size()); }

    void init(i32 node, i32 beg, i32 end) {
        if (beg + 1 == end) tree[node] = {a[beg], a[beg]};
        else {
            init(node * 2, beg, (beg + end) / 2);
            init(node * 2 + 1, (beg + end) / 2, end);
            get<0>(tree[node]) = min(get<0>(tree[node * 2]), get<0>(tree[node * 2 + 1]));
            get<1>(tree[node]) = max(get<1>(tree[node * 2]), get<1>(tree[node * 2 + 1]));
        }
    }

    tuple<i32, i32> query(i32 node, i32 beg, i32 end, i32 left, i32 right) {
        if (right <= beg || end <= left) return {1'000'000'000, -1'000'000'000};
        if (left <= beg && end <= right) return tree[node];
        else {
            auto const [lmin, lmax] = query(node * 2, beg, (beg + end) / 2, left, right);
            auto const [rmin, rmax] = query(node * 2 + 1, (beg + end) / 2, end, left, right);
            return {min(lmin, rmin), max(lmax, rmax)};
        }
    }

    tuple<i32, i32> query(i32 left, i32 right) { return query(1, 0, a.size(), left, right); }
};

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    i32 N, M;
    cin >> N >> M;
    auto seq = vector<i32>(N);
    copy_n(istream_iterator<i32>{cin}, N, begin(seq));

    auto segment_tree = SegmentTree{seq};
    while (0 <= --M) {
        i32 a, b;
        cin >> a >> b;
        auto const [min, max] = segment_tree.query(a - 1, b);
        cout << min << ' ' << max << '\n';
    }

    cout.flush();
    return 0;
}
