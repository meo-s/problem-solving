// https://www.acmicpc.net/problem/13306

#include <cstdint>
#include <iostream>
#include <tuple>
#include <utility>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;

class DisjointSet {

    std::vector<sz> _parents;

  public:
    DisjointSet(sz const size) : _parents(size) { Clear(); }

    void Clear() {
        for (auto i = sz(); i < _parents.size(); ++i) {
            _parents[i] = i;
        }
    }

    sz Find(sz const u) { return _parents[u] != u ? (_parents[u] = Find(_parents[u])) : u; }

    bool Merge(sz const u, sz const v) {
        auto const up = Find(u);
        auto const vp = Find(v);
        if (up != vp) {
            _parents[vp] = up;
        }
        return up != vp;
    }
};

template <typename T> T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<sz>(std::cin);
    auto const q = Read<sz>(std::cin);

    auto parents = std::vector<std::pair<sz, bool>>(n + 1);
    for (auto i = sz(2); i <= n; ++i) {
        std::cin >> std::get<0>(parents[i]);
        std::get<1>(parents[i]) = true;
    }

    auto queries = std::vector<std::tuple<i32, sz, sz>>((n - 1) + q);
    for (auto& query : queries) {
        std::cin >> std::get<0>(query) >> std::get<1>(query);
        if (std::get<0>(query) != 0) {
            std::cin >> std::get<2>(query);
        } else {
            parents[std::get<1>(query)].second = false;
        }
    }

    auto tree = DisjointSet(n + 1);
    for (auto i = sz(2); i <= n; ++i) {
        if (parents[i].second) {
            tree.Merge(i, parents[i].first);
        }
    }

    auto ans = std::vector<bool>(queries.size());
    for (auto i = queries.size(); 0 < i; --i) {
        auto const& [t, a, b] = queries[i - 1];
        if (t == 0) {
            tree.Merge(parents[a].first, a);
        } else {
            ans[i - 1] = (tree.Find(a) == tree.Find(b));
        }
    }

    for (auto i = sz(); i < queries.size(); ++i) {
        if (std::get<0>(queries[i])) {
            std::cout << (ans[i] ? "YES\n" : "NO\n");
        }
    }

    std::cout.flush();
    return 0;
}
