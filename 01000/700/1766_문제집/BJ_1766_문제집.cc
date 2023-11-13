// https://www.acmicpc.net/problem/1766

#include <cstdint>
#include <functional>
#include <iostream>
#include <iterator>
#include <queue>
#include <vector>

using sz = std::size_t;

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto in = std::istream_iterator<int>(std::cin);
    auto const n = std::size_t(*in);
    auto m = *++in;
    auto following_problems = std::vector<std::vector<sz>>(n);
    auto preceding_problems = std::vector<int>(n);
    while (0 < m--) {
        auto const a = *++in - 1;
        auto const b = *++in - 1;
        following_problems[a].emplace_back(b);
        ++preceding_problems[b];
    }

    auto clearable_problems = std::priority_queue<sz, std::vector<sz>, std::greater<sz>>();
    for (auto i = sz(); i < n; ++i) {
        if (preceding_problems[i] == 0) {
            clearable_problems.emplace(i);
        }
    }
    while (!clearable_problems.empty()) {
        auto const problem = clearable_problems.top();
        clearable_problems.pop();
        std::cout << problem + 1 << ' ';
        for (auto const next_problem : following_problems[problem]) {
            if (--preceding_problems[next_problem] == 0) {
                clearable_problems.emplace(next_problem);
            }
        }
    }
    std::cout << std::endl;
    return 0;
}
