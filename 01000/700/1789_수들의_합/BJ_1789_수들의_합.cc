// https://www.acmicpc.net/problem/1789

#include <cmath>
#include <cstdlib>
#include <iostream>

using u64 = std::uint64_t;

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

    auto const s = Read<u64>(std::cin);
    auto i = static_cast<u64>(std::sqrt(static_cast<double>(s)));
    for (; i * (i + 1) <= s * 2; ++i);
    std::cout << i - 1 << std::endl;
    return EXIT_SUCCESS;
}
