// https://www.acmicpc.net/problem/16724

#include <array>
#include <iostream>
#include <unordered_set>
#include <utility>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;

constexpr auto DELTAS = std::array<std::pair<i32, i32>, 4> {
    {
        {-1, 0},
        {0, 1},
        {1, 0},
        {0, -1},
    },
};

class DisjointSet {
    mutable std::vector<i32> parents;

public:
    DisjointSet(sz const size)
        : parents(size) {
        Clear();
    }

    sz Size() const {
        return parents.size();
    }

    void Clear() {
        for (i32 i = 0; i < i32(Size()); ++i) {
            parents[i] = i;
        }
    }

    i32 Find(i32 const u) const {
        return parents[u] != u ? (parents[u] = Find(parents[u])) : u;
    }

    bool Merge(i32 const u, i32 const v) {
        auto const up = Find(u);
        auto const vp = Find(v);
        if (up != vp) {
            parents[vp] = up;
        }
        return up != vp;
    }
};

template <typename T>
T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const h = Read<i32>(std::cin);
    auto const w = Read<i32>(std::cin);
    auto disjointSet = DisjointSet(h * w);
    for (auto y = i32(); y < h; ++y) {
        for (auto x = i32(); x < w; ++x) {
            auto delta = static_cast<std::pair<i32, i32> const*>(nullptr);
            switch (Read<char>(std::cin)) {
            case 'U':
                delta = &DELTAS[0];
                break;
            case 'R':
                delta = &DELTAS[1];
                break;
            case 'D':
                delta = &DELTAS[2];
                break;
            case 'L':
                delta = &DELTAS[3];
                break;
            }

            auto const ny = y + delta->first;
            auto const nx = x + delta->second;
            disjointSet.Merge(ny * w + nx, y * w + x);
        }
    }

    auto independentSets = std::unordered_set<i32>();
    for (auto y = i32(); y < h; ++y) {
        for (auto x = i32(); x < w; ++x) {
            independentSets.emplace(disjointSet.Find(y * w + x));
        }
    }
    std::cout << independentSets.size() << std::endl;
    return 0;
}

