// https://www.acmicpc.net/problem/1091

#include <algorithm>
#include <array>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <numeric>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
constexpr auto MAX_CARDS = sz(48);

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

[[nodiscard]] i32 FindCycle(std::vector<i32> const& s, std::vector<bool>& visited,
                            i32 const u) noexcept
{
    if (visited[u])
    {
        return i32();
    }
    visited[u] = true;
    return FindCycle(s, visited, s[u]) + 1;
}

[[nodiscard]] i32 FindCycle(std::vector<i32> const& s)
{
    auto cycle = i32(1);
    rng::for_each(rng::views::iota(i32(), static_cast<i32>(s.size())),
                  [&, visited = std::vector<bool>(s.size())](auto const u) mutable {
                      if (!visited[u])
                      {
                          cycle = std::lcm(cycle, FindCycle(s, visited, u));
                      }
                  });
    return cycle;
}

void Shuffle(std::vector<i32>& card2pos, std::vector<i32> const& s)
{
    thread_local static auto pos2card = std::array<i32, MAX_CARDS>{};

    rng::for_each(rng::views::iota(i32(), static_cast<i32>(card2pos.size())),
                  [&](auto const i) { pos2card[card2pos[i]] = i; });
    rng::for_each(rng::views::iota(i32(), static_cast<i32>(card2pos.size())),
                  [&](auto const i) { card2pos[pos2card[i]] = s[i]; });
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<sz>(std::cin);
    auto p = std::vector<i32>(n);
    auto s = std::vector<i32>(n);
    std::copy_n(std::istream_iterator<i32>(std::cin), n, rng::begin(p));
    std::copy_n(std::istream_iterator<i32>(std::cin), n, rng::begin(s));

    std::cout << [&]() {
        auto card2pos = std::vector<i32>(n);
        rng::copy(rng::views::iota(i32(), static_cast<i32>(n)), rng::begin(card2pos));
        auto const check_sequence = [&]() {
            return rng::all_of(rng::views::iota(sz(), n),
                               [&](auto const i) { return card2pos[i] % 3 == p[i]; });
        };

        auto const cycle = FindCycle(s);
        for (auto i = i32(); i < cycle; ++i)
        {
            if (check_sequence())
            {
                return i;
            }
            Shuffle(card2pos, s);
        }
        return i32(-1);
    }() << std::endl;
    return EXIT_SUCCESS;
}
