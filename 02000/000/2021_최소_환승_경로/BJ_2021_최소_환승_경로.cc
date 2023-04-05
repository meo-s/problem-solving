// https://www.acmicpc.net/problem/2021

#include <deque>
#include <iostream>
#include <iterator>
#include <vector>

using namespace std;
using i32 = int;

int main(int argc, char const* argv[]) {
    auto in = istream_iterator<i32>{cin};
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto const N = *in;
    auto const L = *++in;
    auto stations = vector<vector<i32>>(N);
    auto routes = vector<vector<i32>>(L);
    for (i32 u = 0; u < L; ++u) {
        for (;;) {
            if (auto const v = *++in - 1; v < 0) break;
            else {
                stations[v].emplace_back(u);
                routes[u].emplace_back(v);
            }
        }
    }

    auto const s = *++in - 1;
    auto const g = *++in - 1;

    auto dists = vector<i32>(N, -1);
    auto visited = vector<bool>(L); // 노선 사용 여부
    auto waypoints = deque<pair<i32, i32>>{{s, -1}};
    while (dists[g] < 0 && 0 < waypoints.size()) {
        auto const [u, dist] = waypoints.front();
        waypoints.pop_front();

        for (auto const route_index : stations[u]) {
            if (!visited[route_index]) {
                visited[route_index] = true;
                for (auto const v : routes[route_index]) {
                    if (dists[v] == -1) {
                        dists[v] = dist + 1;
                        waypoints.emplace_back(v, dists[v]);
                    }
                }
            }
        }
    }

    cout << dists[g] << endl;
    return 0;
}
