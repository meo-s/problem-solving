// https://www.acmicpc.net/problem/1092

#include <algorithm>
#include <cstdint>
#include <functional>
#include <iostream>
#include <iterator>
#include <queue>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;

int main(int const argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    auto in = istream_iterator<i32>{cin};
    auto cranes = vector<i32>(sz(*in));
    for (auto& crane : cranes) {
        crane = *++in;
    }
    auto cargos = priority_queue<i32>{};
    for (i32 i = *++in; 0 < i; --i) {
        cargos.emplace(*++in);
    }

    auto jobs = vector<i32>(cranes.size());
    sort(cranes.begin(), cranes.end(), greater<i32>());
    while (!cargos.empty()) {
        i32 optimal_crane = -1;
        for (i32 i = 0; i < cranes.size() && cargos.top() <= cranes[i]; ++i) {
            if (optimal_crane == -1 || jobs[i] < jobs[optimal_crane]) {
                optimal_crane = i;
            }
        }
        if (optimal_crane != -1) {
            jobs[optimal_crane]++;
            cargos.pop();
        } else {
            break;
        }
    }

    cout << (cargos.empty() ? *max_element(jobs.begin(), jobs.end()) : -1) << endl;
    return 0;
}
