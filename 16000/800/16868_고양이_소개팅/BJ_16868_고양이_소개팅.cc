// https://www.acmicpc.net/problem/16858

#include <algorithm>
#include <cstdint>
#include <deque>
#include <iostream>
#include <iterator>
#include <map>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using i64 = std::int64_t;

struct CatTowerFloor
{
    i64 depth;
    i32 num_cats;
    i32 jump_threshold;
    std::vector<std::pair<i32, i32>> children;
};

using CatTower = std::vector<CatTowerFloor>;

void MeasureDepth(CatTower &cat_tower, i32 const u = 0)
{
    if (u == 0)
    {
        cat_tower[u].depth = 0;
    }
    for (auto const [v, length] : cat_tower[u].children)
    {
        cat_tower[v].depth = cat_tower[u].depth + length;
        MeasureDepth(cat_tower, v);
    }
}

i64 MatchCats(CatTower &cat_tower)
{
    auto num_matches = i64();
    auto queens = std::vector<std::multimap<i64, i32, std::greater<i64>>>(cat_tower.size());
    auto do_match = [&](auto &&callee, auto const u) mutable -> void {
        for (auto const [v, _] : cat_tower[u].children)
        {
            callee(std::forward<decltype(callee)>(callee), v);
            if (queens[u].size() < queens[v].size())
            {
                std::swap(queens[u], queens[v]);
            }
            queens[u].merge(queens[v]);
        }

        if (cat_tower[u].jump_threshold < 0)
        {
            queens[u].emplace(cat_tower[u].depth, cat_tower[u].num_cats);
        }
        else
        {
            auto it = queens[u].lower_bound(cat_tower[u].depth + cat_tower[u].jump_threshold);
            while (it != queens[u].end())
            {
                auto const num_jumps = std::min(it->second, cat_tower[u].num_cats);
                num_matches += num_jumps;
                if ((cat_tower[u].num_cats -= num_jumps, it->second -= num_jumps) == 0)
                {
                    it = std::next(it);
                    queens[u].erase(std::prev(it));
                }
                else
                {
                    break;
                }
            }
        }
    };
    do_match(do_match, i32());
    return num_matches;
}

template <typename T>
T Read(std::istream &is)
{
    auto v = T();
    is >> v;
    return v;
}

template <>
CatTower Read(std::istream &is)
{
    auto const n = Read<i32>(std::cin);
    auto cat_tower = CatTower(static_cast<sz>(n));
    for (auto const i : rng::views::iota(i32(), n))
    {
        cat_tower[i].num_cats = Read<i32>(std::cin);
    }
    for (auto const i : rng::views::iota(i32(), n))
    {
        cat_tower[i].jump_threshold = Read<i32>(std::cin);
    }
    {
        auto parents = std::vector<i32>(static_cast<sz>(n));
        std::copy_n(std::istream_iterator<i32>(std::cin), n - 1, std::next(std::begin(parents)));
        auto lengths = std::vector<i32>(static_cast<sz>(n));
        std::copy_n(std::istream_iterator<i32>(std::cin), n - 1, std::next(std::begin(lengths)));
        for (auto const i : rng::views::iota(i32(1), n))
        {
            cat_tower[parents[i] - 1].children.emplace_back(i, lengths[i]);
        }
    }
    return cat_tower;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const *const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto cat_tower = Read<CatTower>(std::cin);
    MeasureDepth(cat_tower);

    std::cout << MatchCats(cat_tower) << std::endl;
    return 0;
}
