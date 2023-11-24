// https://www.acmicpc.net/problem/16566

#include <algorithm>
#include <iostream>
#include <iterator>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;

class DisjointSet {
    mutable std::vector<i32> _parents;

public:
    DisjointSet(sz const size)
        : _parents(size) {
        Clear();
    }

    void Clear() {
        for (auto i = sz(); i < _parents.size(); ++i) {
            _parents[i] = i32(i);
        }
    }

    i32 Find(i32 const u) {
        if (_parents[u] != u) {
            _parents[u] = Find(_parents[u]);
        }
        return _parents[u];
    }

    void Merge(i32 const u, i32 const v) {
        auto const up = Find(u);
        auto const vp = Find(v);
        if (up != vp) {
            _parents[vp] = up;
        }
    }
};

template <typename T>
T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<sz>(std::cin);
    auto const m = Read<sz>(std::cin);
    auto const k = Read<sz>(std::cin);

    auto cards = std::vector<i32>(m);
    std::copy_n(std::istream_iterator<i32>(std::cin), m, std::begin(cards));
    std::sort(std::begin(cards), std::end(cards));

    auto disjoint_set = DisjointSet(n + 1);
    for (auto i = sz(), j = sz(); i <= n; ++i) {
        disjoint_set.Merge(cards[j], i32(i));
        if (i == cards[j]) {
            if (++j == m) {
                break;
            }
        }
    }
    for (auto i = sz(); i < k; ++i) {
        auto const card = Read<i32>(std::cin);
        auto const min_largest_card = disjoint_set.Find(card + 1);
        std::cout << min_largest_card << '\n';
        if (min_largest_card + 1 <= i32(n)) {
            disjoint_set.Merge(min_largest_card + 1, min_largest_card);
        }
    }
    std::cout.flush();
    return 0;
}
