// https://www.acmicpc.net/problem/9996

#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <regex>
#include <string>

using i32 = std::int32_t;

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

    auto n = Read<i32>(std::cin);
    auto const pattern = std::regex([]() {
        auto pattern = Read<std::string>(std::cin);
        if (auto const offset = pattern.find('*'); offset != pattern.length())
        {
            pattern = pattern.replace(offset, 1, ".*");
        }
        return pattern;
    }());
    while (0 <= --n)
    {
        std::cout << (std::regex_match(Read<std::string>(std::cin), pattern) ? "DA" : "NE") << '\n';
    }
    return EXIT_SUCCESS;
}
