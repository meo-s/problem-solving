// https://www.acmicpc.net/problem/2146

#include <algorithm>
#include <array>
#include <cstdlib>
#include <deque>
#include <iostream>
#include <iterator>
#include <limits>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using Map = std::vector<std::vector<i32>>;

using Point = std::pair<i32, i32>;
constexpr Point operator+(Point const& lhs, Point const& rhs) noexcept
{
    return {lhs.first + rhs.first, lhs.second + rhs.second};
}

constexpr static auto DELTAS = std::array<Point, 4>{{{-1, 0}, {0, 1}, {1, 0}, {0, -1}}};

void FillIsland(Map& m, Point const& where, i32 const value)
{
    m[where.second][where.first] = value;
    for (auto const& delta : DELTAS)
    {
        auto const [nx, ny] = where + delta;
        if (nx < 0 || static_cast<i32>(m.size()) <= nx)
        {
            continue;
        }
        if (ny < 0 || static_cast<i32>(m.size()) <= ny)
        {
            continue;
        }
        if (m[ny][nx] == 0)
        {
            FillIsland(m, {nx, ny}, value);
        }
    }
}

i32 FindShortestBridge(Map const& m, Point const& where)
{
    thread_local static auto points = std::deque<std::pair<Point, i32>>();
    thread_local static auto dists = std::vector<std::vector<i32>>();

    if (dists.size() != m.size())
    {
        dists.resize(m.size());
    }
    rng::for_each(dists, [size = m.size()](auto& row) {
        if (row.size() != size)
        {
            row.resize(size);
        }
        std::fill(std::begin(row), std::end(row), std::numeric_limits<i32>::max());
    });

    auto threshold = static_cast<i32>(m.size() * 2);
    points.emplace_back(where, i32());
    dists[where.second][where.first] = i32();
    while (!points.empty())
    {
        auto const [pt, dist] = points.front();
        points.pop_front();

        if (threshold <= dist || dists[pt.second][pt.first] != dist)
        {
            continue;
        }

        for (auto const& delta : DELTAS)
        {
            auto const [nx, ny] = pt + delta;
            if (nx < 0 || static_cast<i32>(m.size()) <= nx)
            {
                continue;
            }
            if (ny < 0 || static_cast<i32>(m.size()) <= ny)
            {
                continue;
            }

            if (m[ny][nx] != -1 && m[ny][nx] != m[where.second][where.first])
            {
                threshold = std::min(threshold, dist);
            }
            else
            {
                auto const ndist = dist + (m[ny][nx] == i32(-1) ? i32(1) : i32(0));
                if (ndist < dists[ny][nx])
                {
                    dists[ny][nx] = ndist;
                    points.emplace_back(Point(nx, ny), ndist);
                }
            }
        }
    }

    return threshold;
}

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

template <>
Map Read<Map>(std::istream& is)
{
    auto const n = Read<sz>(std::cin);
    auto map = Map(n);
    rng::for_each(map, [n](auto& row) {
        row.resize(n);
        std::copy_n(std::istream_iterator<i32>(std::cin), n, begin(row));
        rng::transform(row, begin(row), [](auto const v) { return v - 1; });
    });
    return map;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto map = Read<Map>(std::cin);
    auto num_islands = i32();
    for (auto const y : rng::views::iota(i32(), static_cast<i32>(map.size())))
    {
        for (auto const x : rng::views::iota(i32(), static_cast<i32>(map.size())))
        {
            if (map[y][x] == 0)
            {
                FillIsland(map, {x, y}, ++num_islands);
            }
        }
    }

    auto ans = static_cast<i32>(map.size() * 2);
    auto visited = std::vector<bool>(static_cast<sz>(num_islands), false);
    for (auto const y : rng::views::iota(i32(), static_cast<i32>(map.size())))
    {
        for (auto const x : rng::views::iota(i32(), static_cast<i32>(map.size())))
        {
            if (map[y][x] != i32(-1) && !visited[map[y][x] - 1])
            {
                visited[map[y][x] - 1] = true;
                ans = std::min(ans, FindShortestBridge(map, {x, y}));
            }
        }
    }
    std::cout << ans << std::endl;
    return EXIT_SUCCESS;
}
