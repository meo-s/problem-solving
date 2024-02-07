// https://www.acmicpc.net/problem/1135

#include <algorithm>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using i32 = std::int32_t;
using Tree = std::vector<std::vector<i32>>;

template <typename T>
T Read(std::istream& is)
{
    auto v = T();
    is >> v;
    return v;
}

template <>
Tree Read<Tree>(std::istream& is)
{
    auto const n = Read<i32>(std::cin);
    auto tree = Tree(static_cast<std::size_t>(n));
    (void)Read<i32>(std::cin);
    for (auto u = i32(1); u < n; ++u)
    {
        tree[Read<i32>(std::cin)].emplace_back(u);
    }
    return tree;
}

[[nodiscard]] i32 CallEveryone(Tree const& tree, i32 const u)
{
    if (tree[u].empty())
    {
        return i32();
    }

    auto required_times = std::vector<i32>(tree[u].size());
    rng::transform(tree[u], rng::begin(required_times),
                   [&](auto const v) { return CallEveryone(tree, v); });

    rng::sort(required_times, std::greater{}, {});
    rng::transform(rng::views::iota(i32(), static_cast<i32>(required_times.size())), required_times,
                   rng::begin(required_times),
                   [](auto const i, auto const required_time) { return (i + 1) + required_time; });

    return rng::max(required_times);
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const* const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);
    std::cout << CallEveryone(Read<Tree>(std::cin), i32()) << std::endl;
    return EXIT_SUCCESS;
}
