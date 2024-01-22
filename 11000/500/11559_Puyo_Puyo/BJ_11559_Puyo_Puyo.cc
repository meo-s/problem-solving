// https://www.acmicpc.net/problem/11559

#include <algorithm>
#include <array>
#include <cstdint>
#include <deque>
#include <iostream>
#include <ranges>
#include <utility>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;

constexpr static auto DIRECTIONS =
    std::array<std::pair<i32, i32>, 4>{{{0, -1}, {1, 0}, {0, 1}, {-1, 0}}};

constexpr static auto BLANK = '.';
constexpr static auto BOARD_HEIGHT = i32(12);
constexpr static auto BOARD_WIDTH = i32(6);
template <typename T>
using Board = std::array<std::array<T, BOARD_HEIGHT>, BOARD_WIDTH>;

void ApplyGravity(Board<char> &board)
{
    rng::for_each(board, [](auto &col) {
        for (auto y = col.size(), ny = col.size(); 1 < y; --y)
        {
            if (col[y - 1] == BLANK)
            {
                ny = std::min(ny, y) - 1;
                while (0 < ny && col[ny - 1] == BLANK)
                {
                    --ny;
                }
                if (ny == 0)
                {
                    break;
                }
                col[y - 1] = std::exchange(col[ny - 1], BLANK);
            }
        }
    });
}

bool PuyoPuyo(Board<char> &board)
{
    thread_local static auto visited = Board<bool>();

    thread_local static auto bfs = [](Board<char> &board, i32 const sy, i32 const sx,
                                      auto &&callable) mutable {
        thread_local static auto coords = std::deque<std::pair<i32, i32>>();
        coords.emplace_back(sy, sx);
        while (!coords.empty())
        {
            auto const &[y, x] = coords.front();
            for (auto const &[dy, dx] : DIRECTIONS)
            {
                auto const ny = y + dy;
                auto const nx = x + dx;
                if (ny < 0 || BOARD_HEIGHT <= ny || nx < 0 || BOARD_WIDTH <= nx)
                {
                    continue;
                }
                if (!visited[nx][ny] && board[nx][ny] == board[sx][sy])
                {
                    visited[nx][ny] = true;
                    coords.emplace_back(ny, nx);
                }
            }
            callable(y, x);
            coords.pop_front();
        }
    };

    auto pop_at_least_once = false;
    thread_local static auto targets = std::deque<std::pair<i32, i32>>();
    rng::for_each(visited, [](auto &row) mutable { rng::fill(row, false); });
    for (auto const y : rng::views::iota(i32(), BOARD_HEIGHT))
    {
        for (auto const x : rng::views::iota(i32(), BOARD_WIDTH))
        {
            if (board[x][y] != BLANK && !visited[x][y])
            {
                targets.clear();
                bfs(board, y, x, [](auto const y, auto const x) { targets.emplace_back(y, x); });
                if (4 < targets.size())
                {
                    pop_at_least_once = true;
                    while (!targets.empty())
                    {
                        auto const &[y, x] = targets.front();
                        board[x][y] = BLANK;
                        targets.pop_front();
                    }
                }
            }
        }
    }
    return pop_at_least_once;
}

template <typename T>
T Read(std::istream &is)
{
    auto v = T();
    is >> v;
    return v;
}

template <>
Board<char> Read(std::istream &is)
{
    auto board = Board<char>();
    for (auto const y : rng::views::iota(i32(), BOARD_HEIGHT))
    {
        for (auto const x : rng::views::iota(i32(), BOARD_WIDTH))
        {
            board[x][y] = Read<char>(is);
        }
    }
    return board;
}

int main([[maybe_unused]] int const argc, [[maybe_unused]] char const *const argv[])
{
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto board = Read<Board<char>>(std::cin);
    auto chain = i32(-1);
    do
    {
        ++chain;
        ApplyGravity(board);
    } while (PuyoPuyo(board));
    std::cout << chain << std::endl;
    return 0;
}
