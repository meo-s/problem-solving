// https://www.acmicpc.net/problem/23050

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <string>
#include <tuple>
#include <unordered_map>
#include <utility>
#include <vector>

using namespace std;
using namespace std::string_literals;
using i32 = int32_t;
using Tree = vector<vector<i32>>;

i32 centroids_of(Tree const& tree, vector<i32>& centroids, i32 const u = 0, i32 const p = -1) {
    auto tree_size = 1;
    auto is_centroid = true;
    for (auto const v : tree[u]) {
        if (v != p) {
            auto const subtree_size = centroids_of(tree, centroids, v, u);
            tree_size += subtree_size;
            is_centroid = is_centroid && subtree_size <= tree.size() / 2;
        }
    }

    if (is_centroid && tree.size() / 2 + tree.size() % 2 <= tree_size) {
        centroids.emplace_back(u);
    }

    return tree_size;
}

tuple<bool, i32> hash_tree(Tree const& tree, unordered_map<string, i32>& uniques, vector<i32>& indices,
                           vector<vector<pair<i32, i32>>>& subtree_indices, i32 const u, i32 const p = -1) {
    auto subtrees = unordered_map<i32, pair<bool, vector<i32>>>{};
    for (auto const v : tree[u]) {
        if (v != p) {
            auto const [is_symmetric, subtree_index] = hash_tree(tree, uniques, indices, subtree_indices, v, u);

            auto iter = subtrees.find(subtree_index);
            if (iter == subtrees.end()) {
                subtrees[subtree_index] = {is_symmetric, vector<i32>{}};
                iter = subtrees.find(subtree_index);
            }

            get<1>(iter->second).emplace_back(v);
        }
    }

    auto is_symmetric = true;
    auto num_self_symmetrics = 0;
    for (auto const& subtree : subtrees) {
        if (!is_symmetric) break;

        auto const& [is_self_symmetric, children] = get<1>(subtree);
        if (children.size() % 2 == 1) {
            is_symmetric = is_self_symmetric && num_self_symmetrics < (p == -1 ? 2 : 1);
            ++num_self_symmetrics;
        }
    }

    using SubtreeIter = decltype(subtrees)::const_iterator;
    auto subtree_iters = vector<SubtreeIter>(0);
    subtree_iters.reserve(subtrees.size());
    for (auto iter = subtrees.cbegin(); iter != subtrees.cend(); ++iter) {
        subtree_iters.emplace_back(iter);
    }

    sort(subtree_iters.begin(), subtree_iters.end(),
         [](SubtreeIter const& lhs, SubtreeIter const& rhs) { return lhs->first < rhs->first; });

    auto total_subtrees = 0;
    auto tree_hash = "R"s;
    for (auto const& iter : subtree_iters) {
        auto const subtree_hash = to_string(iter->first) + ")";
        auto const subtree_count = get<1>(iter->second).size();
        total_subtrees += subtree_count;
        for (i32 i = 0; i < subtree_count; ++i)
            tree_hash += subtree_hash;
    }

    subtree_indices[u].reserve(total_subtrees);
    for (auto const& iter : subtree_iters) {
        auto const& children = get<1>(iter->second);
        for (auto const v : children)
            subtree_indices[u].emplace_back(iter->first, v);
    }

    auto tree_index = -1;
    if (auto const iter = uniques.find(tree_hash); iter != uniques.end()) tree_index = iter->second;
    else {
        tree_index = uniques.size();
        uniques.emplace(tree_hash, tree_index);
    }

    indices[u] = tree_index;
    return {is_symmetric, tree_index};
}

void pair_trees(vector<i32>& pairs, vector<vector<pair<i32, i32>>> const& subtree_indices, pair<i32, i32> const u) {
    if (get<1>(u) != -1) {
        auto const u0 = get<0>(u);
        auto const u1 = get<1>(u);
        pairs[u0] = u1;
        pairs[u1] = u0;
        for (i32 i = 0; i < subtree_indices[u0].size(); ++i) {
            auto const v0 = get<1>(subtree_indices[u0][i]);
            auto const v1 = get<1>(subtree_indices[u1][i]);
            pair_trees(pairs, subtree_indices, {v0, v1});
        }
    } else {
        auto const u0 = get<0>(u);
        pairs[u0] = u0;
        for (i32 i = 0; i < subtree_indices[u0].size(); ++i) {
            auto const [subtree_index, v0] = subtree_indices[u0][i];
            if (i + 1 < subtree_indices[u0].size() && get<0>(subtree_indices[u0][i + 1]) == subtree_index) {
                auto const v1 = get<1>(subtree_indices[u0][++i]);
                pair_trees(pairs, subtree_indices, {v0, v1});
            } else {
                pair_trees(pairs, subtree_indices, {v0, -1});
            }
        }
    }
}

int main(int const argc, char const* argv[]) {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    i32 N;
    cin >> N;
    auto tree = Tree(N);
    for (i32 i = 0; i < N - 1; ++i) {
        i32 u, v;
        cin >> u >> v;
        tree[u - 1].emplace_back(v - 1);
        tree[v - 1].emplace_back(u - 1);
    }

    auto centroids = vector<i32>{};
    centroids_of(tree, centroids);
    if (centroids.size() == 2) {
        tree.emplace_back(2);
        tree[N][0] = centroids[0];
        tree[N][1] = centroids[1];
        for (i32 i = 0; i < 2; ++i) {
            for (auto& v : tree[centroids[i]]) {
                if (v == centroids[i ^ 1]) {
                    v = N;
                    break;
                }
            }
        }
    }

    auto unique_trees = unordered_map<string, i32>{};
    auto tree_indices = vector<i32>(tree.size());
    auto subtree_indices = vector<vector<pair<i32, i32>>>(tree.size());
    auto const centroid = 1 < centroids.size() ? N : centroids[0];
    auto const tree_info = hash_tree(tree, unique_trees, tree_indices, subtree_indices, centroid);
    if (!get<0>(tree_info)) cout << "NO";
    else {
        auto pairs = vector<i32>(subtree_indices.size());
        pair_trees(pairs, subtree_indices, {centroid, -1});

        cout << "YES\n";
        for (i32 i = 0; i < N; ++i)
            cout << pairs[i] + 1 << ' ';
    }

    cout << endl;
    return 0;
}
