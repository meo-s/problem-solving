// https://www.acmicpc.net/problem/20920

#include <algorithm>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <iterator>
#include <ranges>
#include <unordered_map>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
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

    auto const n = Read<sz>(std::cin);
    auto const m = Read<sz>(std::cin);
    auto counter = std::unordered_map<std::string, i32>();
    for ([[maybe_unused]] auto const _ : rng::views::iota(sz(), n))
    {
        auto const word = Read<std::string>(std::cin);
        if (m <= word.length())
        {
            if (auto it = counter.find(word); it != counter.end())
            {
                ++(it->second);
            }
            else
            {
                counter.emplace(std::move(word), i32());
            }
        }
    }

    auto words = std::vector<decltype(counter)::const_iterator>(n);
    words.resize(0);
    for (auto it = counter.begin(); it != counter.end(); ++it)
    {
        words.emplace_back(it);
    }
    rng::sort(words, [](auto const &lhs, auto const &rhs) {
        if (lhs->second == rhs->second)
        {
            if (lhs->first.length() == rhs->first.length())
            {
                return lhs->first < rhs->first;
            }
            return rhs->first.length() < lhs->first.length();
        }
        return rhs->second < lhs->second;
    });

    rng::copy(words | rng::views::transform(
                          [](auto const it) -> std::string const & { return it->first; }),
              std::ostream_iterator<std::string>(std::cout, "\n"));

    return EXIT_SUCCESS;
}
