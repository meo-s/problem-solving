// https://www.acmicpc.net/problem/9328

#include <algorithm>
#include <array>
#include <cstdint>
#include <deque>
#include <iostream>
#include <iterator>
#include <string>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;
using u32 = std::uint32_t;
constexpr auto MAX_X = sz(100);
constexpr auto MAX_Y = sz(100);

constexpr auto DELTAS = std::array<std::pair<i32, i32>, 4>{{{-1, 0}, {0, 1}, {1, 0}, {0, -1}}};

template <typename T> T Read(std::istream& is) {
    auto v = T();
    std::cin >> v;
    return v;
}

bool IsKey(char const c) { return 'a' <= c && c <= 'z'; }

bool IsDoor(char const c) { return 'A' <= c && c <= 'Z'; }

bool IsDocument(char const c) { return c == '$'; }

bool Blocked(char const c, u32 const keys) {
    if (c == '*') {
        return true;
    }
    if (IsDoor(c) && (keys & (1 << (c - 'A'))) == 0) {
        return true;
    }
    return false;
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto maze = std::vector<std::vector<char>>(0);
    auto visited = std::vector<std::vector<bool>>(0);
    maze.reserve(MAX_Y);
    visited.reserve(MAX_Y);
    for (auto i = sz(); i < MAX_Y; ++i) {
        maze.emplace_back(MAX_X);
        visited.emplace_back(MAX_Y);
    }

    for (auto i = Read<i32>(std::cin); 0 < i; --i) {
        auto const h = Read<i32>(std::cin);
        auto const w = Read<i32>(std::cin);
        maze.resize(h);
        visited.resize(h);
        for (auto y = i32(); y < h; ++y) {
            maze[y].resize(w);
            visited[y].resize(w);
            std::copy_n(std::istream_iterator<char>(std::cin), w, std::begin(maze[y]));
            std::fill(std::begin(visited[y]), std::end(visited[y]), false);
        }

        auto keys = u32();
        for (auto key : Read<std::string>(std::cin)) {
            if (key != '0') {
                keys |= 1 << (key - 'a');
            }
        }

        auto ans = 0;
        auto waypoints = std::deque<std::pair<i32, i32>>();
        auto pendings = std::deque<std::pair<i32, i32>>();
        for (auto y = i32(); y < h; ++y) {
            if (maze[y][0] != '*') {
                pendings.emplace_back(y, 0);
                visited[y][0] = true;
            }
            if (maze[y][w - 1] != '*') {
                pendings.emplace_back(y, w - 1);
                visited[y][w - 1] = true;
            }
            if (y == 0 || y == h - 1) {
                for (auto x = i32(1); x < w - 1; ++x) {
                    if (maze[y][x] != '*') {
                        pendings.emplace_back(y, x);
                        visited[y][x] = true;
                    }
                }
            }
        }
        for (;;) {
            for (auto i = pendings.size(); 0 < i; --i) {
                auto const [y, x] = pendings.front();
                pendings.pop_front();
                (Blocked(maze[y][x], keys) ? pendings : waypoints).emplace_back(y, x);
            }
            if (waypoints.empty()) {
                break;
            }
            while (!waypoints.empty()) {
                auto const [y, x] = waypoints.front();
                waypoints.pop_front();

                if (IsDocument(maze[y][x])) {
                    ++ans;
                }
                if (IsKey(maze[y][x])) {
                    keys |= 1 << (maze[y][x] - 'a');
                }

                for (auto const& [dy, dx] : DELTAS) {
                    auto const ny = y + dy;
                    auto const nx = x + dx;
                    if (0 <= ny && ny < h && 0 <= nx && nx < w && !visited[ny][nx]) {
                        visited[ny][nx] = true;
                        if (!Blocked(maze[ny][nx], keys)) {
                            waypoints.emplace_back(ny, nx);
                        } else if (IsDoor(maze[ny][nx])) {
                            pendings.emplace_back(ny, nx);
                        }
                    }
                }
            }
        }

        std::cout << ans << '\n';
    }

    std::cout.flush();
    return 0;
}
