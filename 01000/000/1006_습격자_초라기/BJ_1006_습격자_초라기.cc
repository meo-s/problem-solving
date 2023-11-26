// https://www.acmicpc.net/problem/1006

#include <algorithm>
#include <array>
#include <cmath>
#include <cstdint>
#include <iostream>
#include <limits>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;
constexpr auto INF = std::numeric_limits<i32>::max() - 1;
constexpr auto INVALID = std::numeric_limits<i32>::max();

template <typename T>
T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

void Invade(sz const n, i32 const w, std::vector<std::array<i32, 2>> const& enemies,
            std::vector<std::array<i32, 3>>& dp) {
    for (auto i = sz(); i < n; ++i) {
        if (dp[i][0] != INVALID) {
            dp[i + 1][0] = std::min(dp[i + 1][0], dp[i][0] + (enemies[i][0] + enemies[i][1] <= w ? 1 : 2));
            if (i + 1 < n) {
                dp[i + 1][1] = std::min(dp[i + 1][1], dp[i][0] + (enemies[i][1] + enemies[i + 1][1] <= w ? 2 : 3));
                dp[i + 1][2] = std::min(dp[i + 1][2], dp[i][0] + (enemies[i][0] + enemies[i + 1][0] <= w ? 2 : 3));
                if (enemies[i][0] + enemies[i + 1][0] <= w && enemies[i][1] + enemies[i + 1][1] <= w) {
                    dp[i + 2][0] = dp[i][0] + 2;
                }
            }
        }

        if (dp[i][1] != INVALID) {
            dp[i + 1][0] = std::min(dp[i + 1][0], dp[i][1] + 1);
            if (i + 1 < n && enemies[i][0] + enemies[i + 1][0] <= w) {
                dp[i + 1][2] = std::min(dp[i + 1][2], dp[i][1] + 1);
            }
        }

        if (dp[i][2] != INVALID) {
            dp[i + 1][0] = std::min(dp[i + 1][0], dp[i][2] + 1);
            if (i + 1 < n && enemies[i][1] + enemies[i + 1][1] <= w) {
                dp[i + 1][1] = std::min(dp[i + 1][1], dp[i][2] + 1);
            }
        }
    }
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto dp = std::vector<std::array<i32, 3>>(10'001);
    for (auto t = Read<i32>(std::cin); 0 < t; --t) {
        auto const n = Read<sz>(std::cin);
        auto const w = Read<i32>(std::cin);
        auto enemies = std::vector<std::array<i32, 2>>(n);
        for (auto row = sz(); row < 2; ++row) {
            for (auto col = sz(); col < n; ++col) {
                std::cin >> enemies[col][row];
            }
        }

        dp.resize(n + 1);
        dp[0] = {0, INVALID, INVALID};
        std::fill(std::begin(dp) + 1, std::end(dp), std::array{INF, INF, INF});
        Invade(n, w, enemies, dp);
        auto ans = dp.back()[0];

        if (1 < n && enemies[0][0] + enemies.back()[0] <= w) {
            dp[0] = {INVALID, INVALID, 0};
            std::fill(std::begin(dp) + 1, std::end(dp), std::array{INF, INF, INF});
            Invade(n, w, enemies, dp);
            ans = std::min(ans, std::min(dp[n - 1][0] + 2, dp[n - 1][1] + 1));
        }

        if (1 < n && enemies[0][1] + enemies.back()[1] <= w) {
            dp[0] = {INVALID, 0, INVALID};
            std::fill(std::begin(dp) + 1, std::end(dp), std::array{INF, INF, INF});
            Invade(n, w, enemies, dp);
            ans = std::min(ans, std::min(dp[n - 1][0] + 2, dp[n - 1][2] + 1));
        }

        if (1 < n && enemies[0][0] + enemies.back()[0] <= w && enemies[0][1] + enemies.back()[1] <= w) {
            dp.resize(n);
            dp[0] = {INVALID, INVALID, INVALID};
            dp[1] = {0, INVALID, INVALID};
            std::fill(std::begin(dp) + 2, std::end(dp), std::array{INF, INF, INF});
            Invade(n - 1, w, enemies, dp);
            ans = std::min(ans, dp.back()[0] + 2);
        }

        std::cout << ans << '\n';
    }

    return 0;
}
