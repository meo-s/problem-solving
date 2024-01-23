// https://www.acmicpc.net/problem/1969

#include <algorithm>
#include <array>
#include <cstdint>
#include <iostream>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;

constexpr i32 CharacterToIndex(char const ch)
{
    switch (ch)
    {
    case 'A':
        return i32(0);
    case 'C':
        return i32(1);
    case 'G':
        return i32(2);
    case 'T':
        return i32(3);
    default:
        return i32(-1);
    }
}

constexpr char IndexToCharacter(i32 const index)
{
    switch (index)
    {
    case 0:
        return 'A';
    case 1:
        return 'C';
    case 2:
        return 'G';
    case 3:
        return 'T';
    default:
        return '\0';
    }
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

    auto const n = Read<i32>(std::cin);
    auto const m = Read<i32>(std::cin);
    auto count = std::vector<std::array<i32, 4>>(static_cast<sz>(m));
    for ([[maybe_unused]] auto const _ : rng::views::iota(i32(), n))
    {
        for (auto const i : rng::views::iota(i32(), m))
        {
            ++count[i][CharacterToIndex(Read<char>(std::cin))];
        }
    }

    auto hamming_dist = i32();
    for (auto const k : rng::views::iota(i32(), m))
    {
        auto const i = *rng::find_if(rng::views::iota(i32(), i32(4)),
                                     [target = rng::max(count[k]), &count = count[k]](
                                         auto const i) { return count[i] == target; });
        hamming_dist += n - count[k][i];
        std::cout << IndexToCharacter(i);
    }
    std::cout << '\n' << hamming_dist << std::endl;
    return 0;
}
