// https://www.acmicpc.net/problem/1013

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <ranges>

#define _R ::std::ranges::
using i32 = std::int32_t;

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    _R for_each(_R views::iota(static_cast<i32>(0), Read<i32>(std::cin)),
                []([[maybe_unused]] auto) {
                    auto count = static_cast<i32>(0);
                    auto const ok = _R all_of(Read<std::string>(std::cin), [&](auto const ch) {
                        return ch == '(' ? (++count, true) : 0 <= --count;
                    });
                    std::cout << (ok && count == static_cast<i32>(0) ? "YES\n" : "NO\n");
                });

    return EXIT_SUCCESS;
}
