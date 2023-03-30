// https://www.acmicpc.net/problem/2239

#include <array>
#include <cstdint>
#include <iostream>
#include <iterator>

using namespace std;
using i32 = int32_t;
using Board = array<array<i32, 9>, 9>;
using Bitmasks = array<i32, 9>;

bool dfs(Board& board, Bitmasks& rmask, Bitmasks& cmask, Bitmasks& smask, i32 const y, i32 const x) {
    if (y == 9) return true;
    if (x == 9) return dfs(board, rmask, cmask, smask, y + 1, 0);
    if (board[y][x] != 0) return dfs(board, rmask, cmask, smask, y, x + 1);

    auto const bitmask = rmask[y] | cmask[x] | smask[(y / 3) * 3 + x / 3];
    for (i32 n = 1; n <= 9; ++n) {
        if ((bitmask & (1 << n)) == 0) {
            board[y][x] = n;
            rmask[y] |= 1 << n;
            cmask[x] |= 1 << n;
            smask[(y / 3) * 3 + x / 3] |= 1 << n;
            if (dfs(board, rmask, cmask, smask, y, x + 1)) return true;
            rmask[y] ^= 1 << n;
            cmask[x] ^= 1 << n;
            smask[(y / 3) * 3 + x / 3] ^= 1 << n;
        }
    }

    board[y][x] = 0;
    return false;
}

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto board = Board{};
    for (i32 y = 0; y < 9; ++y) {
        for (i32 x = 0; x < 9; ++x) {
            board[y][x] = cin.get() - '0';
        } cin.get();
    }

    auto rmask = Bitmasks{};
    auto cmask = Bitmasks{};
    auto smask = Bitmasks{};
    for (i32 y = 0; y < 9; ++y) {
        for (i32 x = 0; x < 9; ++x) {
            if (board[y][x] != 0) {
                rmask[y] |= 1 << board[y][x];
                cmask[x] |= 1 << board[y][x];
                smask[(y / 3) * 3 + x / 3] |= 1 << board[y][x];
            }
        }
    }

    dfs(board, rmask, cmask, smask, 0, 0);
    for (auto const& row : board) copy(row.begin(), row.end(), ostream_iterator<i32>{cout}), cout << '\n';
    cout.flush();

    return 0;
}
