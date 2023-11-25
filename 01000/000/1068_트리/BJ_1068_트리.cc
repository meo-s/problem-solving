// https://www.acmicpc.net/problem/1068

#include <cstdint>
#include <iostream>
#include <type_traits>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;
using Graph = std::vector<std::vector<sz>>;

template <typename T>
T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

sz count_leaf_nodes(Graph const& g, sz const trap, sz const u, sz const p) {
    auto traversals = sz();
    auto leaf_nodes = sz();
    for (auto const v : g[u]) {
        if (v != p && v != trap) {
            ++traversals;
            leaf_nodes += count_leaf_nodes(g, trap, v, u);
        }
    }
    return traversals == 0 ? 1 : leaf_nodes;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto g = Graph(Read<sz>(std::cin));
    auto root_node = sz();
    for (auto node = sz(); node < g.size(); ++node) {
        auto const parent = Read<i32>(std::cin);
        if (parent != -1) {
            g[parent].emplace_back(sz(node));
        } else {
            root_node = node;
        }
    }
    std::cout << std::invoke([&]() {
        auto const trap = Read<sz>(std::cin);
        return trap == root_node ? sz() : count_leaf_nodes(g, trap, root_node, root_node);
    }) << std::endl;
    return 0;
}
