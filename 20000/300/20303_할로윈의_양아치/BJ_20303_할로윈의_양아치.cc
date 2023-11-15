// https://www.acmicpc.net/problem/20303

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <ranges>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;
using Graph = std::vector<std::vector<sz>>;

std::pair<i32, i32> dfs(Graph const& g, std::vector<bool>& visited, std::vector<i32> const& candies, sz const u) {
    visited[u] = true;
    auto total_children = 1;
    auto total_candies = candies[u];
    for (auto const v : g[u]) {
        if (!visited[v]) {
            auto const result = dfs(g, visited, candies, v);
            total_children += result.first;
            total_candies += result.second;
        }
    }
    return {total_children, total_candies};
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto in = std::istream_iterator<i32>(std::cin);
    auto const n = sz(*in);
    auto const m = sz(*++in);
    auto const k = *++in;
    auto candies = std::vector<i32>(n);
    std::copy_n(std::istream_iterator<i32>(std::cin), n, std::begin(candies));
    auto g = Graph(n);
    for (auto i = sz(); i < m; ++i) {
        auto const a = sz(*++in - 1);
        auto const b = sz(*++in - 1);
        g[a].emplace_back(b);
        g[b].emplace_back(a);
    }

    auto visited = std::vector<bool>(n);
    auto dp = std::vector<i32>(k, -1);
    dp[0] = 0;
    for (auto i = sz(); i < n; ++i) {
        if (!visited[i]) {
            auto const choice = dfs(g, visited, candies, i);
            for (auto j = k - 1; 0 <= j; --j) {
                if (dp[j] != -1 && j + choice.first < k) {
                    dp[j + choice.first] = std::max(dp[j + choice.first], dp[j] + choice.second);
                }
            }
        }
    }
    std::cout << std::ranges::max(std::views::all(dp)) << std::endl;

    return 0;
}
