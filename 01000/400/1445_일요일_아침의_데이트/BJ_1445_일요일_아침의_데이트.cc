// https://www.acmicpc.net/problem/1445

#include <algorithm>
#include <iostream>
#include <limits>
#include <queue>
#include <utility>
#include <vector>

using namespace std;
using sz = size_t;
using i32 = int32_t;

constexpr i32 INF = numeric_limits<i32>::max();
constexpr i32 dy[] = {1, -1, 0, 0};
constexpr i32 dx[] = {0, 0, 1, -1};

int main(int argc, char const* argv[]) {
    sz H, W;
    cin >> H >> W;

    auto waypoints = deque<tuple<i32, i32, i32, i32>>{};
    auto visited = vector<vector<tuple<i32, i32>>>(H);
    for (i32 y = 0; y < H; ++y) {
        visited[y].resize(W);
        for (i32 x = 0; x < W; ++x) {
            visited[y][x] = {INF, INF};
        }
    }

    auto m = vector<vector<char>>(H);
    for (i32 y = 0; y < H; ++y) {
        m[y].resize(W);
        for (i32 x = 0; x < W; ++x) {
            cin >> m[y][x];
            if (m[y][x] == 'S') {
                m[y][x] = '.';
                waypoints.emplace_back(y, x, 0, 0);
                visited[y][x] = {0, 0};
            }
        }
    }

    while (0 < waypoints.size()) {
        auto const[y, x, oc, sc] = waypoints.front();
        waypoints.pop_front();

        for (i32 i = 0; i < 4; ++i) {
            auto const ny = y + dy[i];
            auto const nx = x + dx[i];
            if (ny < 0 || H <= ny || nx < 0 || W <= nx) continue;

            if (m[ny][nx] == '.' || m[ny][nx] == 'F') {
                if (get<0>(visited[ny][nx]) < oc) continue;

                auto side_of_garbage = false;
                if (m[ny][nx] != 'F') {
                    for (i32 j = 0; j < 4; ++j) {
                        auto const nny = ny + dy[j];
                        auto const nnx = nx + dx[j];
                        if (nny < 0 || H <= nny || nnx < 0 || W <= nnx) continue;
                        if (m[nny][nnx] == 'g') {
                            side_of_garbage = true;
                            break;
                        }
                    }
                }

                auto const nsc = sc + (side_of_garbage ? 1 : 0);
                if (oc < get<0>(visited[ny][nx])) {
                    visited[ny][nx] = {oc, nsc};
                    waypoints.emplace_back(ny, nx, oc, nsc);
                }
                else if (oc == get<0>(visited[ny][nx]) && nsc < get<1>(visited[ny][nx])) {
                    get<1>(visited[ny][nx]) = nsc;
                    waypoints.emplace_back(ny, nx, oc, nsc);
                }
            }
            else if (oc + 1 < get<0>(visited[ny][nx]) || (oc + 1 == get<0>(visited[ny][nx]) && sc < get<1>(visited[ny][nx]))) {
                visited[ny][nx] = {oc + 1, sc};
                waypoints.emplace_back(ny, nx, oc + 1, sc);
            }
        }
    }

    for (i32 y = 0; y < H; ++y) {
        for (i32 x = 0; x < W; ++x) {
            if (m[y][x] == 'F') {
                cout << get<0>(visited[y][x]) << ' ' << get<1>(visited[y][x]) << endl;
                break;
            }
        }
    }

    return 0;
}
