// https://www.acmicpc.net/problem/4002

#include <cmath>
#include <cstdint>
#include <functional>
#include <iostream>
#include <queue>
#include <vector>

using sz = std::size_t;
using i32 = std::int32_t;
using i64 = std::int64_t;
template <typename T, typename Container = std::vector<T>>
using MaxHeap = std::priority_queue<T, Container, std::less<T>>;

struct Ninja {
    sz b;
    i32 c;
    i32 l;
};

template <typename T>
T Read(std::istream& is) {
    auto v = T();
    is >> v;
    return v;
}

void Deploy(i64 const m, MaxHeap<i32>& deployment, i64& cost, i32 const c) {
    while (m < cost + c && (!deployment.empty() && c < deployment.top())) {
        cost -= deployment.top();
        deployment.pop();
    }
    if (cost + c <= m) {
        deployment.emplace(c);
        cost += c;
    }
}

i64 Deploy(std::vector<Ninja> const& ninjas, std::vector<std::vector<sz>> const& underlings, i64 const m,
           std::vector<MaxHeap<i32>>& deployments, std::vector<i64>& costs, sz const u) {

    auto max_satisfaction = i64();
    for (auto const& underling : underlings[u]) {
        max_satisfaction = std::max(max_satisfaction, Deploy(ninjas, underlings, m, deployments, costs, underling));
        if (deployments[u].size() < deployments[underling].size()) {
            deployments[u].swap(deployments[underling]);
            std::swap(costs[u], costs[underling]);
        }
        while (!deployments[underling].empty()) {
            auto const c = deployments[underling].top();
            deployments[underling].pop();
            Deploy(m, deployments[u], costs[u], c);
        }
    }

    Deploy(m, deployments[u], costs[u], ninjas[u].c);
    return std::max(max_satisfaction, i64(deployments[u].size()) * ninjas[u].l);
}

i64 Deploy(std::vector<Ninja> const& ninjas, std::vector<std::vector<sz>> const& underlings, i64 const m, sz const u) {
    auto deployments = std::vector<MaxHeap<i32>>(ninjas.size());
    auto costs = std::vector<i64>(ninjas.size());
    return Deploy(ninjas, underlings, m, deployments, costs, u);
}

int main(int const argc, char const* const argv[]) {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    auto const n = Read<sz>(std::cin);
    auto const m = Read<i64>(std::cin);
    auto master_ninja = sz();
    auto ninjas = std::vector<Ninja>(n + 1);
    auto underlings = std::vector<std::vector<sz>>(n + 1);
    for (auto i = sz(1); i <= n; ++i) {
        std::cin >> ninjas[i].b >> ninjas[i].c >> ninjas[i].l;
        if (ninjas[i].b == 0) {
            master_ninja = i;
        } else {
            underlings[ninjas[i].b].emplace_back(i);
        }
    }

    std::cout << Deploy(ninjas, underlings, m, master_ninja) << std::endl;
    return 0;
}
