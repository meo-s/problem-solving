// https://www.acmicpc.net/problem/6531

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <ranges>
#include <string>
#include <vector>

#define _R ::std::ranges::

using sz = std::size_t;
using u8 = std::uint8_t;
using i32 = std::int32_t;

enum struct CacheState : u8
{
    EMPTY,
    YES,
    NO
};

using Cache = std::vector<std::vector<CacheState>>;

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

bool IsSet(Cache& dp, std::string_view const s, i32 const beg, i32 const end);

bool IsElementList(Cache& dp, std::string_view const s, i32 const beg, i32 const end)
{
    auto& state{dp[beg][end]};
    if (state != CacheState::EMPTY)
    {
        return state == CacheState::YES;
    }

    state = (IsSet(dp, s, beg, end) ? CacheState::YES : [&]() {
        for (auto it = beg + 2; it < end; ++it)
        {
            if (s[it - 1] == ',' && IsElementList(dp, s, beg, it - 1))
            {
                if (it < end && IsElementList(dp, s, it, end))
                { // ElementList "," ElementList
                    return CacheState::YES;
                }
            }
        }
        return end - beg <= 1 ? CacheState::YES : CacheState::NO;
    }());

    return state == CacheState::YES;
}

bool IsSet(Cache& dp, std::string_view const s, i32 const beg, i32 const end)
{
    if (end - beg < 2 || s[beg] != '{' || s[end - 1] != '}')
    {
        return false;
    }
    return IsElementList(dp, s, beg + 1, end - 1);
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    _R for_each(_R views::iota(0, Read<int>(std::cin)), [](auto const test_case) {
        auto const s = Read<std::string>(std::cin);
        auto cache = Cache(static_cast<sz>(s.length()));
        _R for_each(cache, [&s](auto& row) { row.resize(s.length() + 1, {}); });

        auto const test = IsSet(cache, s, 0, static_cast<i32>(s.length()));
        std::cout << "Word #" << test_case + 1 << ": " << (test ? "Set" : "No Set") << '\n';
    });

    return EXIT_SUCCESS;
}
