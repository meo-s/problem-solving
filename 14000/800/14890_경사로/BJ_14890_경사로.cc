// https://www.acmicpc.net/problem/14890

#include <algorithm>
#include <array>
#include <cstdint>
#include <cstdlib>
#include <functional>
#include <iostream>
#include <iterator>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;

template <typename T>
using Vector2D = std::vector<std::vector<T>>;

constexpr auto MAX_COLS = static_cast<sz>(100);

struct Vec2
{
    i32 x;
    i32 y;
};

struct SharedContext
{
    Vector2D<i32> map;
    i32 n;
    i32 L;
};

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

template <>
SharedContext Read<SharedContext>(std::istream& is)
{
    auto ctx = SharedContext();
    ctx.n = Read<i32>(std::cin);
    ctx.L = Read<i32>(std::cin);
    ctx.map.resize(static_cast<sz>(ctx.n));
    for (auto& row : ctx.map)
    {
        row.resize(ctx.n);
        std::copy_n(std::istream_iterator<i32>(std::cin), ctx.n, rng::begin(row));
    }
    return ctx;
}

[[nodiscard]] bool InstallSlope(SharedContext const& ctx, std::array<bool, MAX_COLS>& has_slope,
                                Vec2 const start, i32 Vec2::*const axis, auto&& next) noexcept
{
    return rng::all_of(rng::views::iota(i32(1), ctx.L + i32(1)), [&](auto const delta) {
        auto const it = next(start, delta);
        if (it.*axis < i32(0) || ctx.n <= it.*axis)
        {
            return false;
        }
        if (has_slope[it.*axis] || ctx.map[start.y][start.x] - ctx.map[it.y][it.x] != i32(1))
        {
            return false;
        }
        return has_slope[it.*axis] = true;
    });
}

[[nodiscard]] bool TestRoad(SharedContext const& ctx, std::array<bool, MAX_COLS>& has_slope,
                            Vec2 const start, i32 Vec2::*const axis, auto&& next) noexcept
{
    for (auto delta = i32();;)
    {
        auto const it = next(start, delta);
        auto const next_it = next(it, i32(1));
        if (next_it.*axis < 0 || ctx.n <= next_it.*axis)
        {
            return true;
        }

        auto const height_diff = ctx.map[it.y][it.x] - ctx.map[next_it.y][next_it.x];
        if (height_diff <= i32(0))
        {
            ++delta;
            continue;
        }
        if (height_diff == i32(1))
        {
            if (InstallSlope(ctx, has_slope, it, axis, next))
            {
                delta += ctx.L;
                continue;
            }
        }
        return false;
    }
}

[[nodiscard]] auto GenNextFn(i32 Vec2::*const axis, auto&& op)
{
    return [axis, op](auto const v, auto const delta) {
        auto next_v = v;
        next_v.*axis = op(next_v.*axis, delta);
        return next_v;
    };
}

[[nodiscard]] bool TestRoad(SharedContext const& ctx, i32 Vec2::*const axis, Vec2 start) noexcept
{
    thread_local static auto has_slope = std::array<bool, 100>();
    rng::fill_n(rng::begin(has_slope), ctx.n, false);

    if (!TestRoad(ctx, has_slope, start, axis, GenNextFn(axis, std::plus())))
    {
        return false;
    }
    start.*axis = ctx.n - 1;
    if (!TestRoad(ctx, has_slope, start, axis, GenNextFn(axis, std::minus())))
    {
        return false;
    }
    return true;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const ctx = Read<SharedContext>(std::cin);
    auto const x_axises = rng::count_if(rng::views::iota(i32(), ctx.n), [&](auto const x) {
        return TestRoad(ctx, &Vec2::y, {x, 0});
    });
    auto const y_axises = rng::count_if(rng::views::iota(i32(), ctx.n), [&](auto const y) {
        return TestRoad(ctx, &Vec2::x, {0, y});
    });
    std::cout << x_axises + y_axises << std::endl;
    return EXIT_SUCCESS;
}
