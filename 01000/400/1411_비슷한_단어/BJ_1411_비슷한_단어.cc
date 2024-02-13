// https://www.acmicpc.net/problem/1411

#include <algorithm>
#include <array>
#include <cstdlib>
#include <iostream>
#include <numeric>
#include <ranges>
#include <string>
#include <string_view>
#include <unordered_map>

constexpr auto MAX_WORD_LENGTH = std::size_t(50);

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

std::string Shhomify(std::string_view const& word)
{
    thread_local static auto buf = std::array<char, MAX_WORD_LENGTH>();
    std::ranges::fill_n(std::begin(buf), word.length(), '\0');
    auto indices = std::array<std::size_t, 'Z' - 'A' + 1>();
    std::ranges::transform(std::ranges::views::iota(std::size_t(1), word.length() + 1), word,
                           std::begin(buf), [&indices](auto const index, auto const ch) {
                               if (indices[ch - 'a'] == std::size_t())
                               {
                                   indices[ch - 'a'] = index;
                               }
                               return indices[ch - 'a'];
                           });
    return {buf.data(), word.length()};
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto counter = std::unordered_map<std::string, int>();
    std::ranges::for_each(std::ranges::views::iota(std::size_t(), Read<std::size_t>(std::cin)),
                          [&counter]([[maybe_unused]] auto const) {
                              ++counter[Shhomify(Read<std::string>(std::cin))];
                          });
    std::cout << std::accumulate(std::cbegin(counter), std::cend(counter), int(),
                                 [](int const count, auto const& it) {
                                     return count + (it.second * (it.second - 1)) / 2;
                                 })
              << std::endl;
    return EXIT_SUCCESS;
}
