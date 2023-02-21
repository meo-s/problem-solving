// https://www.acmicpc.net/problem/20188

#include <iostream>
#include <iterator>
#include <list>
#include <vector>

using namespace std;

using sz = size_t;
using tree_t = vector<list<sz>>;
using tree_info_t = pair<sz, sz>;
using dfs_result_t = pair<sz, tree_info_t>;

dfs_result_t dfs(tree_t const& tree, vector<bool>& visited, sz const u = 1, sz const depth = 0) {
    visited[u] = true;
    auto varieties = depth;

    sz n_children = 1, weight = 1;
    auto local_tree_infos = list<tree_info_t>{};
    for (auto const v : tree[u]) {
        if (!visited[v]) {
            auto&& [local_varieties, local_tree_info] = dfs(tree, visited, v, depth + 1);
            local_tree_infos.emplace_back(local_tree_info);
            varieties += local_varieties;

            auto const& [ci, wi] = local_tree_info;
            if (0 < depth) {
                varieties += ci * depth + wi;
            }
            n_children += ci;
            weight += ci + wi;
        }
    }

    if (0 < local_tree_infos.size()) {
        auto [ci, wi] = local_tree_infos.front();
        for (auto tj = next(local_tree_infos.begin()); tj != local_tree_infos.end(); ++tj) {
            auto const& [cj, wj] = *tj;
            varieties += (ci * cj * depth) + (cj * wi + ci * wj);

            ci += cj;
            wi += wj;
        }
    }

    return {varieties, {n_children, weight}};
}

int main(int argc, char* argv[]) {
    cin.tie(nullptr);
    ios::sync_with_stdio(false);
    auto in = istream_iterator<int>{cin};

    auto const V = sz(*in);
    auto tree = tree_t(V + 1);
    for (auto i = sz(0); i < V - 1; ++i) {
        auto const u = *(++in), v = *(++in);
        tree[u].emplace_back(v);
        tree[v].emplace_back(u);
    }

    auto visited = vector<bool>(V + 1);
    auto&& [varieties, _] = dfs(tree, visited);
    cout << varieties << endl;

    return 0;
}
