// https://www.acmicpc.net/problem/17836

#include <array>
#include <cstdint>
#include <deque>
#include <iostream>
#include <iterator>
#include <limits>
#include <tuple>
#include <utility>
#include <vector>

using i32 = std::int32_t;
using u32 = std::uint32_t;
constexpr auto WALL = i32(1);
constexpr auto SWORD = i32(2);
constexpr auto INF = std::numeric_limits<i32>::max();
constexpr auto DELTAS =
    std::array{std::make_pair(-1, 0), std::make_pair(0, 1), std::make_pair(1, 0), std::make_pair(0, -1)};

template <typename T> T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const h = Read<i32>(std::cin);
    auto const w = Read<i32>(std::cin);
    auto const t = Read<i32>(std::cin);
    auto m = std::vector<std::vector<i32>>(h);
    auto dists = std::vector<std::vector<std::array<i32, 2>>>(h);
    for (auto y = i32(); y < h; ++y) {
        m[y].resize(w);
        std::copy_n(std::istream_iterator<i32>(std::cin), w, std::begin(m[y]));
        dists[y].resize(w);
        std::fill_n(std::begin(dists[y]), w, std::array{INF, INF});
    }

    auto waypoints = std::deque<std::tuple<i32, i32, i32, i32>>();
    dists[0][0][0] = 0;
    waypoints.emplace_back(0, 0, 0, 0);
    while (!waypoints.empty()) {
        auto const [y, x, dist, phase] = waypoints.front();
        waypoints.pop_front();

        for (auto const& [dy, dx] : DELTAS) {
            auto const ny = y + dy;
            auto const nx = x + dx;
            if (ny < 0 || h <= ny || nx < 0 || w <= nx) {
                continue;
            }
            if (m[ny][nx] == WALL && phase == 0) {
                continue;
            }
            if (dist + 1 < dists[ny][nx][phase]) {
                dists[ny][nx][phase] = dist + 1;
                waypoints.emplace_back(ny, nx, dist + 1, (m[ny][nx] != SWORD ? phase : 1));
            }
        }
    }

    auto const time = std::min(dists[h - 1][w - 1][0], dists[h - 1][w - 1][1]);
    if (time <= t) {
        std::cout << time << std::endl;
    } else {
        std::cout << "Fail" << std::endl;
    }
    return 0;
}
