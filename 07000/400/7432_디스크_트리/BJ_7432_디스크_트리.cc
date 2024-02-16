// https://www.acmicpc.net/problem/7432

#include <algorithm>
#include <cstdlib>
#include <iostream>
#include <iterator>
#include <map>
#include <memory>
#include <ranges>

#define _R ::std::ranges::

struct FsNode
{
    std::map<std::string, std::unique_ptr<FsNode>> children;
};

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

    auto root = FsNode();
    for ([[maybe_unused]] auto const _ : _R views::iota(0, Read<int>(std::cin)))
    {
        auto const path = Read<std::string>(std::cin);
        auto node = &root;
        for (auto offset = _R cbegin(path); offset != _R cend(path);)
        {
            auto const beg = offset;
            auto const end = _R find(beg, _R cend(path), '\\');
            offset = _R next(end, 1, _R cend(path));

            thread_local static auto buf = std::string(81, '\0');
            buf.assign(beg, end);
            if (auto const it = node->children.find(buf); it != _R end(node->children))
            {
                node = it->second.get();
            }
            else
            {
                node = (node->children[buf] = std::make_unique<FsNode>()).get();
            }
        }
    }

    auto ls = [](auto&& callee, FsNode const& node, int const depth = 0) -> void {
        for (auto const& [name, child] : node.children)
        {
            _R fill_n(std::ostream_iterator<char>(std::cout), depth, ' ');
            std::cout << name << '\n';
            callee(std::forward<decltype(callee)>(callee), *child, depth + 1);
        }
    };
    ls(ls, root);

    return EXIT_SUCCESS;
}
