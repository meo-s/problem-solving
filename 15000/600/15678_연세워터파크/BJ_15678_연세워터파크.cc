// https://www.acmicpc.net/problem/15678

#include <algorithm>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <limits>
#include <queue>
#include <unordered_set>
#include <vector>

using i32 = std::int32_t;
using i64 = std::int64_t;

template <typename T> T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<i32>(std::cin);
    auto const d = Read<i32>(std::cin);
    auto bridges = std::vector<i32>(n);
    std::copy_n(std::istream_iterator<i32>(std::cin), n, std::begin(bridges));

    using MaxHeap =
        std::priority_queue<std::pair<i64, i32>, std::vector<std::pair<i64, i32>>,
                            decltype([](auto const& lhs, auto const& rhs) { return lhs.first < rhs.first; })>;

    auto ans = std::numeric_limits<i64>::min();
    auto counter = std::unordered_multiset<i64>();
    auto prev_jumps = MaxHeap();
    for (auto i = n - 1; 0 <= i; --i) {
        while (!prev_jumps.empty() && i + d < prev_jumps.top().second) {
            counter.erase(prev_jumps.top().first);
            prev_jumps.pop();
        }

        auto current_jump = static_cast<i64>(bridges[i]);
        if (!prev_jumps.empty() && 0 < prev_jumps.top().first) {
            current_jump += prev_jumps.top().first;
        }

        ans = std::max(ans, current_jump);
        counter.emplace(current_jump);
        prev_jumps.emplace(current_jump, i);
    }
    std::cout << ans << std::endl;
    return 0;
}
