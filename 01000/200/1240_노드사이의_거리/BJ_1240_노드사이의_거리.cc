// https://www.acmicpc.net/problem/1240

#include <cstdint>
#include <iostream>
#include <utility>
#include <vector>

using i32 = std::int32_t;
using Graph = std::vector<std::vector<std::pair<i32, i32>>>;

template <typename T>
T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

i32 Find(Graph const& g, i32 const target, i32 const u, i32 const p = -1) {
    if (u == target) {
        return 0;
    }
    for (auto const& [v, w] : g[u]) {
        if (v != p) {
            if (auto const dist = Find(g, target, v, u); 0 <= dist) {
                return dist + w;
            }
        }
    }
    return -1;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<i32>(std::cin);
    auto m = Read<i32>(std::cin);
    auto g = Graph(n);
    for (auto i = i32(); i < n - 1; ++i) {
        auto const u = Read<i32>(std::cin) - 1;
        auto const v = Read<i32>(std::cin) - 1;
        auto const w = Read<i32>(std::cin);
        g[u].emplace_back(v, w);
        g[v].emplace_back(u, w);
    }
    while (0 < m--) {
        auto const u = Read<i32>(std::cin) - 1;
        auto const v = Read<i32>(std::cin) - 1;
        std::cout << Find(g, u, v) << '\n';
    }
    std::cout.flush();
    return 0;
}
