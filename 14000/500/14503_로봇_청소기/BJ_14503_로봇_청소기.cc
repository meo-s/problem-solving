// https://www.acmicpc.net/problem/1789

#include <algorithm>
#include <array>
#include <cmath>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <iterator>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;

constexpr static auto DUST = i32(0);
constexpr static auto WALL = i32(1);
constexpr static auto NONE = i32(-1);

struct Vec2
{
    i32 x;
    i32 y;

    [[nodiscard]] constexpr Vec2 operator-() const noexcept
    {
        return {-x, -y};
    }

    [[nodiscard]] constexpr Vec2 operator+(Vec2 const& rhs) const noexcept
    {
        return {x + rhs.x, y + rhs.y};
    }
};

struct Robot
{
    Vec2 pos;
    i32 dir;
};

constexpr static auto DIRECTIONS = std::array{Vec2(0, -1), Vec2(1, 0), Vec2(0, 1), Vec2(-1, 0)};

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    std::cin >> v;
    return v;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    constexpr auto n_dirs = static_cast<i32>(DIRECTIONS.size());
    auto const n = Read<sz>(std::cin);
    auto const m = Read<sz>(std::cin);

    auto robot = Robot();
    robot.pos.y = Read<i32>(std::cin);
    robot.pos.x = Read<i32>(std::cin);
    robot.dir = Read<i32>(std::cin);

    auto room = std::vector<std::vector<i32>>(n);
    rng::for_each(room, [m](auto& row) {
        row.resize(m);
        std::copy_n(std::istream_iterator<i32>(std::cin), row.size(), begin(row));
    });

    auto num_cleaned_dusts = i32();
    for (;;)
    {
        if (room[robot.pos.y][robot.pos.x] == DUST)
        {
            room[robot.pos.y][robot.pos.x] = NONE;
            ++num_cleaned_dusts;
        }

        auto const delta_dir =
            *rng::find_if(rng::views::iota(i32(1), static_cast<i32>(DIRECTIONS.size()) + 1),
                          [&](auto const delta_dir) {
                              auto const ndir = (robot.dir - delta_dir + n_dirs) % n_dirs;
                              auto const [nx, ny] = robot.pos + DIRECTIONS[ndir];
                              if (ny < i32() || static_cast<i32>(n) <= ny)
                              {
                                  return false;
                              }
                              if (nx < i32() || static_cast<i32>(m) <= nx)
                              {
                                  return false;
                              }
                              return room[ny][nx] == DUST;
                          });
        if (delta_dir <= static_cast<i32>(DIRECTIONS.size()))
        {
            robot.dir = (robot.dir - delta_dir + n_dirs) % n_dirs;
            robot.pos = robot.pos + DIRECTIONS[robot.dir];
            continue;
        }

        auto const [nx, ny] = robot.pos + (-DIRECTIONS[robot.dir]);
        if (ny < i32() || static_cast<i32>(n) <= ny || nx < i32() || static_cast<i32>(m) <= nx ||
            room[ny][nx] == WALL)
        {
            break;
        }
        robot.pos = {nx, ny};
    }

    std::cout << num_cleaned_dusts << std::endl;
    return EXIT_SUCCESS;
}
