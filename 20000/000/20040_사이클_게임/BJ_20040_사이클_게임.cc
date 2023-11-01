// https://www.acmicpc.net/problem/20040

#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <iterator>
#include <type_traits>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;

class DisjointSet {
  public:
    DisjointSet(sz const size) : _parents(size) { Clear(); }

    void Clear() {
        for (i32 i = 0; i < _parents.size(); ++i) {
            _parents[i] = i;
        }
    }

    i32 Find(i32 const u) const {
        if (_parents[u] != u) {
            _parents[u] = Find(_parents[u]);
        }
        return _parents[u];
    }

    bool Merge(i32 const u, i32 const v) {
        auto const up = Find(u);
        auto const vp = Find(v);
        if (up != vp) {
            _parents[vp] = up;
        }
        return up != vp;
    }

  private:
    mutable vector<i32> _parents;
};

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto in = istream_iterator<i32>(cin);
    auto const n = *in;
    auto const m = *++in;
    auto points = DisjointSet(sz(n));
    for (remove_cvref_t<decltype(m)> i = 0; i < m; ++i) {
        if (!points.Merge(*++in, *++in)) {
            cout << i + 1 << endl;
            exit(0);
        }
    }

    cout << 0 << endl;
    return 0;
}
