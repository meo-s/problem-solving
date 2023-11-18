// https://www.acmicpc.net/problem/2342

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <limits>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;

inline constexpr auto NUM_CELLS = 5;
using DPTable = std::array<std::array<i32, NUM_CELLS>, NUM_CELLS>;

constexpr auto MOVE_COST = std::array<std::array<i32, NUM_CELLS>, NUM_CELLS> {
    {
        {0, 2, 2, 2, 2},  // [0][x] = 0 to x
        {0, 1, 3, 4, 3},  // [1][x] = 1 to x
        {0, 3, 1, 3, 4},  // [2][x] = 2 to x
        {0, 4, 3, 1, 3},  // [3][x] = 3 to x
        {0, 3, 4, 3, 1},  // [4][x] = 4 to x
    },
};

template <typename T>
T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

void InitDPTable(DPTable& table) {
    auto const head = reinterpret_cast<i32*>(table.data());
    std::fill(head, head + NUM_CELLS * NUM_CELLS, std::numeric_limits<i32>::max());
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto dp = std::vector<std::array<std::array<i32, 5>, 5>> (100'001);
    dp.resize(1);
    InitDPTable(dp[0]);
    dp[0][0][0] = 0;
    for (;;) {
        auto const to = Read<sz>(std::cin);
        if (to == 0) {
            break;
        }
        auto const k = dp.size();
        dp.emplace_back();
        InitDPTable(dp.back());
        for (auto i = sz(); i < NUM_CELLS; ++i) {
            for (auto j = sz(); j < NUM_CELLS; ++j) {
                if (dp[k - 1][i][j] != std::numeric_limits<i32>::max()) {
                    if (i != to) {
                        dp[k][i][to] = std::min(dp[k][i][to], dp[k - 1][i][j] + MOVE_COST[j][to]);
                    }
                    if (j != to) {
                        dp[k][to][j] = std::min(dp[k][to][j], dp[k - 1][i][j] + MOVE_COST[i][to]);
                    }
                }
            }
        }
    }

    auto ans = std::numeric_limits<i32>::max();
    for (auto i = sz(); i < NUM_CELLS; ++i) {
        for (auto j = sz(); j < NUM_CELLS; ++j) {
            ans = std::min(ans, dp.back()[i][j]);
        }
    }
    std::cout << ans << std::endl;
    return 0;
}

