// https://www.acmicpc.net/problem/1013

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <iterator>
#include <ranges>
#include <regex>

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

    auto const pattern = std::regex{"(?:100+1+|01)+"};
    _R transform(_R views::iota(0, Read<i32>(std::cin)),
                 std::ostream_iterator<char const*>(std::cout, "\n"), [&]([[maybe_unused]] auto) {
                     return std::regex_match(Read<std::string>(std::cin), pattern) ? "YES" : "NO";
                 });

    return EXIT_SUCCESS;
}
