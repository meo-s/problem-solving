// https://www.acmicpc.net/problem/14499

#include <algorithm>
#include <array>
#include <cstdint>
#include <cstdlib>
#include <iostream>
#include <iterator>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;
using Board = std::vector<std::vector<i32>>;

struct Vec2
{
    i32 x;
    i32 y;

    bool In(Vec2 const& lt, Vec2 const& rb) const noexcept
    {
        return lt.x <= x && x <= rb.x && lt.y <= y && y <= rb.y;
    }
};

struct Dice
{
    Vec2 pos{};
    std::array<i32, 6> state{};

    [[nodiscard]] i32 GetX() const noexcept
    {
        return pos.x;
    }

    [[nodiscard]] i32 GetY() const noexcept
    {
        return pos.y;
    }

    [[nodiscard]] i32& Top() noexcept
    {
        return state.front();
    }

    [[nodiscard]] i32& Bottom() noexcept
    {
        return state.back();
    }

    [[nodiscard]] bool RollUp(i32 const bh, [[maybe_unused]] i32 const bw) noexcept
    {
        if (pos.y - i32(1) < i32())
        {
            return false;
        }

        --pos.y;
        auto const prev(state);
        state[0] = prev[4];
        state[3] = prev[0];
        state[4] = prev[5];
        state[5] = prev[3];
        return true;
    }

    [[nodiscard]] bool RollDown(i32 const bh, [[maybe_unused]] i32 const bw) noexcept
    {
        if (bh <= pos.y + i32(1))
        {
            return false;
        }

        ++pos.y;
        auto const prev(state);
        state[0] = prev[3];
        state[3] = prev[5];
        state[4] = prev[0];
        state[5] = prev[4];
        return true;
    }

    [[nodiscard]] bool RollLeft([[maybe_unused]] i32 const bh, i32 const bw) noexcept
    {
        if (pos.x - i32(1) < i32())
        {
            return false;
        }

        --pos.x;
        auto const prev(state);
        state[0] = prev[1];
        state[1] = prev[5];
        state[2] = prev[0];
        state[5] = prev[2];
        return true;
    }

    [[nodiscard]] bool RollRight([[maybe_unused]] i32 const bh, i32 const bw) noexcept
    {
        if (bw <= pos.x + i32(1))
        {
            return false;
        }

        ++pos.x;
        auto const prev(state);
        state[0] = prev[2];
        state[1] = prev[0];
        state[2] = prev[5];
        state[5] = prev[1];
        return true;
    }
};

std::istream& operator>>(std::istream& is, Vec2& v)
{
    return is >> v.y >> v.x;
}

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

    auto const h = Read<i32>(std::cin);
    auto const w = Read<i32>(std::cin);
    auto dice = Dice();
    dice.pos = Read<Vec2>(std::cin);
    auto k = Read<i32>(std::cin);
    auto board = Board(static_cast<sz>(h));
    for (auto& row : board)
    {
        row.resize(w);
        std::copy_n(std::istream_iterator<i32>(std::cin), row.size(), std::begin(row));
    }

    auto const HANDLERS = std::array{
        &Dice::RollRight,
        &Dice::RollLeft,
        &Dice::RollUp,
        &Dice::RollDown,
    };
    while (i32() <= --k)
    {
        if ((dice.*HANDLERS[Read<i32>(std::cin) - 1])(h, w))
        {
            if (board[dice.GetY()][dice.GetX()] == i32())
            {
                board[dice.GetY()][dice.GetX()] = dice.Bottom();
            }
            else
            {
                dice.Bottom() = board[dice.GetY()][dice.GetX()];
                board[dice.GetY()][dice.GetX()] = i32();
            }
            std::cout << dice.Top() << '\n';
        }
    }

    return EXIT_SUCCESS;
}
