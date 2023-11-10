// https://www.acmicpc.net/problem/8452

#include <algorithm>
#include <array>
#include <cstdint>
#include <deque>
#include <iostream>
#include <tuple>
#include <utility>
#include <vector>

using i32 = std::int32_t;

template <typename T> T Read(std::istream& is) {
    auto v = T();
    std::cin >> v;
    return v;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<i32>(std::cin);
    auto const m = Read<i32>(std::cin);
    auto const q = Read<i32>(std::cin);

    auto edges = std::vector<std::tuple<i32, i32, i32>>(m);
    for (auto& edge : edges) {
        std::cin >> std::get<0>(edge) >> std::get<1>(edge);
        std::get<2>(edge) = q;
    }

    auto ans = std::vector<i32>(q, 0);
    auto queries = std::vector<std::deque<i32>>(n);
    for (auto i = i32(); i < q; ++i) {
        if (Read<char>(std::cin) == 'U') {
            std::get<2>(edges[Read<i32>(std::cin) - 1]) = i;
        } else {
            ans[i] = -1;
            queries[Read<i32>(std::cin) - 1].emplace_back(i);
        }
    }

    auto g = std::vector<std::vector<std::pair<i32, i32>>>(n);
    for (auto const& [u, v, threshold] : edges) {
        g[u - 1].emplace_back(v - 1, threshold);
    }

    auto waypoints = std::deque<std::tuple<i32, i32, i32>>();
    auto thresholds = std::vector<i32>(n, -1);
    waypoints.emplace_back(0, 0, q);
    thresholds[0] = q;
    while (!waypoints.empty()) {
        auto const [u, dist, current_threshold] = waypoints.front();
        waypoints.pop_front();
        for (auto const& [v, threshold] : g[u]) {
            auto const next_threshold = std::min(current_threshold, threshold);
            if (thresholds[v] < next_threshold) {
                thresholds[v] = next_threshold;
                waypoints.emplace_back(v, dist + 1, next_threshold);
                while (!queries[v].empty() && queries[v].front() < next_threshold) {
                    ans[queries[v].front()] = dist + 1;
                    queries[v].pop_front();
                }
            }
        }
    }

    for (auto const v : ans) {
        if (v != 0) {
            std::cout << v << '\n';
        }
    }

    std::cout.flush();
    return 0;
}
