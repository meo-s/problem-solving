// https://www.acmicpc.net/problem/17469

#include <cstdint>
#include <iostream>
#include <iterator>
#include <set>
#include <stack>
#include <tuple>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;

class ColorfulTree {
    mutable std::vector<sz> _parents;
    std::vector<std::set<i32>> _colors;

public:
    ColorfulTree(sz const size, std::vector<i32> const& node_colors)
        : _parents(size), _colors(size) {
        for (auto i = sz(); i < _parents.size(); ++i) {
            _parents[i] = i;
            _colors[i].emplace(node_colors[i]);
        }
    }

    sz Colors(sz const u) const {
        return _colors[Find(u)].size();
    }

    sz Find(sz const u) const {
        if (_parents[u] != u) {
            _parents[u] = Find(_parents[u]);
        }
        return _parents[u];
    }

    void Merge(sz const u, sz const v) {
        auto up = Find(u);
        auto vp = Find(v);
        if (up != vp) {
            if (_colors[up].size() < _colors[vp].size()) {
                std::swap(up, vp);
            }
            _parents[vp] = up;
            _colors[up].merge(_colors[vp]);
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
    auto const q = Read<sz>(std::cin);
    auto parents = std::vector<sz>(n);
    auto node_colors = std::vector<i32>(n);
    parents[0] = 1;
    if (1 < n) {
        std::copy_n(std::istream_iterator<sz>(std::cin), n - 1, std::begin(parents) + 1);
    }
    std::copy_n(std::istream_iterator<i32>(std::cin), n, std::begin(node_colors));

    auto queries = std::vector<std::pair<i32, sz>>(n + q - 1);
    for (auto& query : queries) {
        std::cin >> query.first >> query.second;
        --query.second;
    }

    auto ans = std::stack<sz>();
    auto tree = ColorfulTree(n, node_colors);
    for (auto it = std::crbegin(queries); it != std::crend(queries); ++it) {
        if (it->first == 1) {
            tree.Merge(it->second, parents[it->second] - 1);
        }
        else {
            ans.push(tree.Colors(it->second));
        }
    }
    while (!ans.empty()) {
        std::cout << ans.top() << '\n';
        ans.pop();
    }

    std::cout.flush();
    return 0;
}
