// https://www.acmicpc.net/problem/1559

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <queue>
#include <tuple>

using namespace std;

using u16 = uint16_t;
using i32 = int32_t;

u16 maze[100][100];
bool visited[100][100][1 << 8][4];
i32 const dy[] = {-1, 0, 1, 0};
i32 const dx[] = {0, 1, 0, -1};

int main(int argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    for (;;) {
        i32 H, W;
        cin >> H >> W;
        if (H + W == 0) {
            break;
        }

        for (i32 y = 0; y < H; ++y) {
            for (i32 x = 0; x < W; ++x) {
                char c;
                cin >> c;
                switch (c) {
                case 'N':
                    maze[y][x] = 0b00;
                    break;
                case 'E':
                    maze[y][x] = 0b01;
                    break;
                case 'S':
                    maze[y][x] = 0b10;
                    break;
                case 'W':
                    maze[y][x] = 0b11;
                    break;
                }
            }
        }

        i32 K;
        cin >> K;
        for (int i = 0; i < K; ++i) {
            i32 y, x;
            cin >> y >> x;
            maze[y - 1][x - 1] |= 0b100 << i;
        }

        for (i32 y = 0; y < H; ++y)
            for (i32 x = 0; x < W; ++x)
                for (i32 t = 0; t < (1 << K); ++t)
                    fill(visited[y][x][t], visited[y][x][t + 1], false);

        auto now = -1;
        auto pendings = deque<tuple<i32, i32, u16>>{};
        visited[0][0][0][0] = true;
        pendings.emplace_back(0, 0, 0);
        while (0 < pendings.size()) {
            ++now;
            i32 step = pendings.size();
            while (0 < step--) {
                auto [y, x, tmask] = pendings.front();
                pendings.pop_front();

                if (y == H - 1 && x == W - 1) {
                    if (tmask == (1 << K) - 1) {
                        pendings.clear();
                        break;
                    }
                }

                if (!visited[y][x][tmask][(now + 1) % 4]) {
                    visited[y][x][tmask][(now + 1) % 4] = true;
                    pendings.emplace_back(y, x, tmask);
                }

                auto ny = y + dy[((maze[y][x] & 0b11) + now) % 4];
                auto nx = x + dx[((maze[y][x] & 0b11) + now) % 4];
                if (ny < 0 || H <= ny || nx < 0 || W <= nx) {
                    continue;
                }

                tmask |= maze[ny][nx] >> 2;
                if (!visited[ny][nx][tmask][(now + 1) % 4]) {
                    visited[ny][nx][tmask][(now + 1) % 4] = true;
                    pendings.emplace_back(ny, nx, tmask);
                }
            }
        }

        cout << now << '\n';
    }

    cout.flush();
    return 0;
}
