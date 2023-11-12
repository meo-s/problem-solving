// https://www.acmicpc.net/problem/18251

#include <algorithm>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <limits>
#include <vector>

using i32 = std::int32_t;
using i64 = std::int64_t;

template <typename T> T Read(std::istream& is) {
    auto v = T();
    std::cin >> v;
    return v;
}

void dfs(std::vector<i32> const& nodes, std::pair<i32, i32> const& height_range, i32 const node, i32 const height,
         i64& cur_sum, i64& max_sum) {
    if (height < height_range.second) { // height \in [height.first, height.second)
        dfs(nodes, height_range, node * 2, height + 1, cur_sum, max_sum);
        if (height_range.first <= height) {
            cur_sum = std::max(cur_sum + nodes[node], static_cast<i64>(nodes[node]));
            max_sum = std::max(max_sum, cur_sum);
        }
        dfs(nodes, height_range, node * 2 + 1, height + 1, cur_sum, max_sum);
    }
}

i64 dfs(std::vector<i32> const& nodes, std::pair<i32, i32> const& height_range) {
    auto max_sum = std::numeric_limits<i64>::min();
    auto cur_sum = static_cast<i64>(std::numeric_limits<i32>::min());
    dfs(nodes, height_range, 1, 0, cur_sum, max_sum);
    return max_sum;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<i32>(std::cin);
    auto const h = i32(std::log2(n + 1));
    auto nodes = std::vector<i32>(n + 1);
    std::copy_n(std::istream_iterator<i32>(std::cin), n, ++std::begin(nodes));

    auto max_sum = std::numeric_limits<i64>::min();
    for (auto height_low = i32(); height_low < h; ++height_low) {
        for (auto height_high = height_low + 1; height_high <= h; ++height_high) {
            max_sum = std::max(max_sum, dfs(nodes, {height_low, height_high}));
        }
    }
    std::cout << max_sum << std::endl;

    return 0;
}