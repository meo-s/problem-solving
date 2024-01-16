// https://www.acmicpc.net/problem/17837

#include <vcruntime.h>
#include <algorithm>
#include <array>
#include <cstdint>
#include <cstdlib>
#include <deque>
#include <iostream>
#include <iterator>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using i32 = std::int32_t;

constexpr static i32 RED_CELL = 1;
constexpr static i32 BLUE_CELL = 2;

constexpr static i32 DIR_RIGHT = 1;
constexpr static i32 DIR_LEFT = 2;
constexpr static i32 DIR_UP = 3;
constexpr static i32 DIR_DOWN = 4;

struct Vec2 {
  i32 y;
  i32 x;

  bool operator==(Vec2 const& rhs) const noexcept {
    return y == rhs.y && x == rhs.x;
  }

  Vec2 operator+(Vec2 const& rhs) const noexcept {
    return {y + rhs.y, x + rhs.x};
  }
};

struct ChessPiece {
  Vec2 pos;
  i32 dir;
  i32 top;
};

constexpr static auto DIRECTIONS = std::array{
    Vec2{i32(), i32()},   Vec2{i32(), i32(1)}, Vec2{i32(), i32(-1)},
    Vec2{i32(-1), i32()}, Vec2{i32(1), i32()},
};

[[nodiscard]] constexpr static bool InRange(i32 const value,
                                            i32 const lo,
                                            i32 const hi) noexcept {
  return lo <= value && value <= hi;
}

[[nodiscard]] constexpr static i32 ReverseDirection(i32 const dir) noexcept {
  switch (dir) {
    case DIR_LEFT:
      return DIR_RIGHT;
    case DIR_RIGHT:
      return DIR_LEFT;
    case DIR_UP:
      return DIR_DOWN;
    case DIR_DOWN:
      return DIR_UP;
    default:
      return i32();
  }
}

[[nodiscard]] static bool HasNextChessPiece(
    std::vector<ChessPiece> const& chess_pieces,
    i32 const index) noexcept {
  return chess_pieces[index].pos == chess_pieces[chess_pieces[index].top].pos;
}

[[nodiscard]] static i32 GetHighestChessPiece(
    std::vector<ChessPiece> const& chess_pieces,
    Vec2 const& where) noexcept {
  auto highest = i32();
  for (;;) {
    if (++highest == static_cast<i32>(chess_pieces.size())) {
      return i32(-1);
    }
    if (chess_pieces[highest].pos == where) {
      break;
    }
  }
  while (HasNextChessPiece(chess_pieces, highest)) {
    highest = chess_pieces[highest].top;
  }
  return highest;
}

void MoveChessPieceTo(std::vector<std::vector<i32>>& counter,
                      std::vector<ChessPiece>& chess_pieces,
                      i32 const index,
                      Vec2 const where,
                      bool const reverse) {
  thread_local static auto pending = std::deque<i32>();
  for (auto next = index;;) {
    if (reverse) {
      pending.emplace_front(next);
    } else {
      pending.emplace_back(next);
    }
    if (HasNextChessPiece(chess_pieces, next)) {
      next = chess_pieces[next].top;
    } else {
      break;
    }
  }

  if (auto const highest = GetHighestChessPiece(chess_pieces, where);
      i32() < highest) {
    chess_pieces[highest].top = pending.front();
  }
  while (!pending.empty()) {
    auto const index = pending.front();
    pending.pop_front();
    --counter[chess_pieces[index].pos.y][chess_pieces[index].pos.x];
    chess_pieces[index].pos = where;
    chess_pieces[index].top = (pending.empty() ? i32() : pending.front());
    ++counter[chess_pieces[index].pos.y][chess_pieces[index].pos.x];
  }
}

[[nodiscard]] bool UpdateChessBoard(std::vector<std::vector<i32>> const& board,
                                    std::vector<std::vector<i32>>& counter,
                                    std::vector<ChessPiece>& chess_pieces) {
  auto const n = static_cast<i32>(board.size());
  for (auto i = i32(1); i < static_cast<i32>(chess_pieces.size()); ++i) {
    auto next_pos = chess_pieces[i].pos + DIRECTIONS[chess_pieces[i].dir];
    if (!InRange(next_pos.y, i32(), n - 1) ||
        !InRange(next_pos.x, i32(), n - 1) ||
        board[next_pos.y][next_pos.x] == BLUE_CELL) {
      chess_pieces[i].dir = ReverseDirection(chess_pieces[i].dir);
      next_pos = chess_pieces[i].pos + DIRECTIONS[chess_pieces[i].dir];
      if (!InRange(next_pos.y, i32(), n - 1) ||
          !InRange(next_pos.x, i32(), n - 1) ||
          board[next_pos.y][next_pos.x] == BLUE_CELL) {
        continue;
      }
    }

    MoveChessPieceTo(counter, chess_pieces, i, next_pos,
                     board[next_pos.y][next_pos.x] == RED_CELL);
    if (i32(4) <= counter[next_pos.y][next_pos.x]) {
      return false;
    }
  }

  return true;
}

template <typename T>
static T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const n = Read<i32>(std::cin);
  auto const k = Read<i32>(std::cin);

  auto board = std::vector<std::vector<i32>>(n);
  auto counter = std::vector<std::vector<i32>>(n);
  rng::for_each(rng::views::iota(i32(), n), [&](auto const i) mutable {
    board[i].resize(n);
    counter[i].resize(n);
    std::copy_n(std::istream_iterator<i32>(std::cin), n, std::begin(board[i]));
  });

  auto chess_pieces = std::vector<ChessPiece>(k + 1);
  chess_pieces[0] = {{i32(-1), i32(-1)}, i32(0)};
  rng::for_each(chess_pieces | rng::views::drop(1),
                [&counter](auto& chess_piece) mutable {
                  chess_piece.pos.y = Read<i32>(std::cin) - 1;
                  chess_piece.pos.x = Read<i32>(std::cin) - 1;
                  chess_piece.dir = Read<i32>(std::cin);
                  ++counter[chess_piece.pos.y][chess_piece.pos.x];
                });

  auto turn = i32(1);
  while (turn <= i32(1000)) {
    if (!UpdateChessBoard(board, counter, chess_pieces)) {
      std::cout << turn;
      break;
    }
    ++turn;
  }
  if (i32(1000) < turn) {
    std::cout << -1;
  }
  std::cout << std::endl;
  return EXIT_SUCCESS;
}
