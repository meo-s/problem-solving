// https://www.acmicpc.net/problem/16434

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <limits>
#include <ranges>
#include <vector>

namespace rng = std::ranges;

using sz = std::size_t;
using i32 = std::int32_t;
using i64 = std::int64_t;

struct Player {
  i64 atk;
  i64 max_hp;
  i64 cur_hp;
};

struct Room {
  i32 a;
  i32 h;
  void (*Enter)(Room const&, Player&);
};

constexpr void MonsterRoom(Room const& room, Player& player) noexcept {
  auto const atk_count =
      room.h / player.atk + (room.h % player.atk != 0 ? 1 : 0);
  player.cur_hp -= (atk_count - 1) * room.a;
}

constexpr void PotionRoom(Room const& room, Player& player) noexcept {
  player.atk += room.a;
  player.cur_hp = std::min(player.max_hp, player.cur_hp + room.h);
}

bool Simulate(std::vector<Room> const& rooms, i64 const atk, i64 const max_hp) {
  auto player = Player{atk, max_hp, max_hp};
  for (auto const& room : rooms) {
    room.Enter(room, player);
    if (player.cur_hp <= 0) {
      return false;
    }
  }
  return true;
}

i64 FindOptimalMaxHp(std::vector<Room> const& rooms, i64 const atk) {
  auto lo = i64(1);
  auto hi = i64(1'000'000) * 999'999 * 123'456 + 1;
  auto optimal = hi;
  while (lo < hi) {
    auto const mid = (lo + hi) / 2;
    if (Simulate(rooms, atk, mid)) {
      optimal = mid;
      hi = mid;
    } else {
      lo = mid + 1;
    }
  }
  return optimal;
}

template <typename T>
T Read(std::istream& is) {
  auto v = T();
  is >> v;
  return v;
}

int main([[maybe_unused]] int const argc,
         [[maybe_unused]] char const* const argv[]) {
  std::ios::sync_with_stdio(false);
  std::cin.tie(nullptr);

  auto const n = Read<sz>(std::cin);
  auto const atk = Read<i64>(std::cin);
  auto rooms = std::vector<Room>(n);
  rng::for_each(rooms, [](auto& room) {
    room.Enter = (Read<i32>(std::cin) == 1 ? MonsterRoom : PotionRoom);
    room.a = Read<i32>(std::cin);
    room.h = Read<i32>(std::cin);
  });

  std::cout << FindOptimalMaxHp(rooms, atk) << std::endl;
  return 0;
}
