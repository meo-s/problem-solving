// https://www.acmicpc.net/problem/17140

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using i32 = std::int32_t;
using Matrix = std::array<std::array<i32, 100>, 100>;

using SortRule = decltype([](auto const &lhs, auto const &rhs) {
    return lhs.second != rhs.second ? rhs.second < lhs.second : rhs.first < lhs.first;
});

[[nodiscard]] auto OperationR(Matrix &m)
{
    thread_local static auto buf = std::vector<std::pair<i32, i32>>();
    thread_local static auto counter = std::array<i32, 101>();

    auto h = i32(), w = i32();
    for (auto y = i32(); y < i32(100); ++y)
    {
        std::fill(std::begin(counter), std::end(counter), i32());
        for (auto x = i32(); x < i32(100); ++x)
        {
            ++counter[m[y][x]];
        }

        buf.clear();
        for (auto const n : rng::views::iota(i32(1), i32(101)))
        {
            if (counter[n] != i32())
            {
                buf.emplace_back(n, counter[n]);
            }
        }

        rng::sort(buf, SortRule());
        for (auto x = i32(); x < i32(100); ++x)
        {
            if (buf.empty())
            {
                m[y][x] = i32();
            }
            else
            {
                m[y][x] = buf.back().first;
                m[y][++x] = buf.back().second;
                buf.pop_back();
                h = std::max(h, y + 1);
                w = std::max(w, x + 1);
            }
        }
    }
    return std::make_pair(h, w);
}

[[nodiscard]] auto OperationC(Matrix &m)
{
    thread_local static auto buf = std::vector<std::pair<i32, i32>>();
    thread_local static auto counter = std::array<i32, 101>();

    auto h = i32(), w = i32();
    for (auto x = i32(); x < i32(100); ++x)
    {
        std::fill(std::begin(counter), std::end(counter), i32());
        for (auto y = i32(); y < i32(100); ++y)
        {
            ++counter[m[y][x]];
        }

        buf.clear();
        for (auto const n : rng::views::iota(i32(1), i32(101)))
        {
            if (counter[n] != i32())
            {
                buf.emplace_back(n, counter[n]);
            }
        }

        rng::sort(buf, SortRule());
        for (auto y = i32(); y < i32(100); ++y)
        {
            if (buf.empty())
            {
                m[y][x] = i32();
            }
            else
            {
                m[y][x] = buf.back().first;
                m[++y][x] = buf.back().second;
                buf.pop_back();
                h = std::max(h, y + 1);
                w = std::max(w, x + 1);
            }
        }
    }
    return std::make_pair(h, w);
}

template <typename T>
T Read(std::istream &is)
{
    auto v = T();
    is >> v;
    return v;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const *const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const r = Read<i32>(std::cin) - 1;
    auto const c = Read<i32>(std::cin) - 1;
    auto const k = Read<i32>(std::cin);
    auto m = Matrix();
    std::fill_n(m[0].data(), 100 * 100, i32());
    for (auto y = 0; y < 3; ++y)
    {
        for (auto x = 0; x < 3; ++x)
        {
            m[y][x] = Read<i32>(std::cin);
        }
    }

    auto tick = i32();
    auto hw = std::pair(i32(3), i32(3));
    while (m[r][c] != k && tick < i32(100))
    {
        hw = (hw.second <= hw.first ? OperationR : OperationC)(m);
        ++tick;
    }
    std::cout << (m[r][c] == k ? tick : i32(-1)) << std::endl;
    return 0;
}
