// https://www.acmicpc.net/problem/17952

#include <cstdint>
#include <cstdlib>
#include <deque>
#include <iostream>
#include <stack>
#include <utility>

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

    using Task = std::pair<i32, i32>;
    auto tasks = std::stack<Task, std::deque<Task>>();
    auto score = i32();

    auto do_task = [&]() {
        if (!tasks.empty())
        {
            if (--tasks.top().second == i32())
            {
                score += tasks.top().first;
                tasks.pop();
            }
        }
    };

    auto pull_task = [&](i32 const now) {
        if (i32() < now && Read<i32>(std::cin) != i32())
        {
            auto const a = Read<i32>(std::cin);
            auto const t = Read<i32>(std::cin);
            tasks.emplace(a, t);
        }
    };

    for (auto time = Read<i32>(std::cin); i32() <= time; --time)
    {
        do_task();
        pull_task(time);
    }

    std::cout << score << std::endl;
    return EXIT_SUCCESS;
}
