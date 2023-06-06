// https://www.acmicpc.net/problem/16977

#include <algorithm>
#include <cstdint>
#include <iostream>
#include <memory>
#include <unordered_map>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;

constexpr sz MAX_QUERIES = 100000;
constexpr sz MAX_NODES = MAX_QUERIES * 22;

struct RangeInfo {
	i32 total;
	i32 rcont;
	i32 lcont;
	i32 ncont;

	RangeInfo() : total{}, rcont{}, lcont{}, ncont{} {}

	RangeInfo(RangeInfo const& other)
		: total{other.total}
		, rcont{other.rcont}
		, lcont{other.lcont}
		, ncont{other.ncont} {}

	RangeInfo* merge(RangeInfo const* l, RangeInfo const* r) {
		total = l->total + r->total;
		lcont = l->total == l->lcont ? l->ncont + r->lcont : l->lcont;
		rcont = r->total == r->ncont ? r->ncont + l->rcont : r->rcont;
		ncont = max(max(l->ncont, r->ncont), l->rcont + r->lcont);
		return this;
	}

	void copy(RangeInfo const* other) {
		total = other->total;
		rcont = other->rcont;
		lcont = other->lcont;
		ncont = other->ncont;
	}

	void fill(i32 const value) {
		lcont = rcont = ncont = value;
	}
};

struct Node : public RangeInfo {
	Node* l;
	Node* r;

	using RangeInfo::copy;

	Node* copy(Node const* other) {
		l = other->l;
		r = other->r;
		copy(static_cast<RangeInfo const*>(other));
		return this;
	}

	static Node* allocate() {
		static Node nodes[MAX_NODES];
		static sz index = 0;
		return &nodes[index++];
	}
};

class PersistentSegmentTree {

	i32 size;
	vector<Node*> roots;

	void init(Node* node, i32 const left, i32 const right) {
		node->total = right - left;
		node->fill(0);
		if (1 < right - left) {
			i32 const mid = (left + right) / 2;
			init(node->l = Node::allocate(), left, mid);
			init(node->r = Node::allocate(), mid, right);
		}
	}

	void update(Node* node, i32 const left, i32 const right, i32 const index) {
		if (right - left == 1) {
			node->fill(1);
		}
		else {
			i32 const mid = (left + right) / 2;
			if (index < mid) {
				update(node->l = Node::allocate()->copy(node->l), left, mid, index);
			}
			else {
				update(node->r = Node::allocate()->copy(node->r), mid, right, index);
			}
			node->merge(node->l, node->r);
		}
	}

	RangeInfo query(Node const* node, i32 const left, i32 const right, i32 const beg, i32 const end) {
		if (right <= beg || end <= left) {
			return RangeInfo{};
		}

		if (beg <= left && right <= end) {
			return RangeInfo{*node};
		}
		else {
			i32 const mid = (left + right) / 2;
			auto const lr = query(node->l, left, mid, beg, end);
			auto const rr = query(node->r, mid, right, beg, end);
			return *(RangeInfo{}.merge(&lr, &rr));
		}
	}

public:

	PersistentSegmentTree(i32 const size) : size{size}, roots(0) {
		roots.reserve(MAX_QUERIES + 1);
		roots.push_back(Node::allocate());
		init(roots[0], 1, size + 1);
	}

	void update(i32 const index) {
		roots.push_back(Node::allocate()->copy(roots.back()));
		update(roots.back(), 1, size + 1, index);
	}

	i32 query(i32 const k, i32 const beg, i32 const end) {
		return query(roots[k], 1, size + 1, beg, end).ncont;
	}

};

unique_ptr<PersistentSegmentTree> pst;
unordered_map<i32, i32> h2i;
unordered_map<i32, i32> i2h;
vector<i32> checkpoints;

void init() {
	i32 N; cin >> N;
	vector<pair<i32, i32>> a(0);
	a.reserve(N);
	for (i32 i = 0; i < N; ++i) {
		i32 h; cin >> h;
		a.emplace_back(i + 1, h);
	}

	pst.reset(new PersistentSegmentTree(N));
	checkpoints.reserve(MAX_QUERIES);

	sort(a.begin(), a.end(), [](auto& lhs, auto& rhs) { return rhs.second < lhs.second; });
	for (decltype(a)::size_type i = 0; i < a.size();) {
		i32 const h0 = a[i].second;
		while (i < a.size() && a[i].second == h0) {
			pst->update(a[i++].first);
		}

		i2h[i32(h2i.size())] = h0;
		h2i[h0] = i32(h2i.size());
		checkpoints.emplace_back(i32(i));
	}
}

void execute_queries() {
	i32 Q; cin >> Q;
	while (0 <= --Q) {
		i32 ans = -1;
		i32 beg, end, w;
		cin >> beg >> end >> w;
		i32 lb = 0;
		i32 ub = h2i.size();
		while (lb < ub) {
			i32 const mid = (lb + ub) / 2;
			if (w <= pst->query(checkpoints[mid], beg, end + 1)) {
				ub = ans = mid;
			}
			else {
				lb = mid + 1;
			}
		}

		cout << i2h[ans] << '\n';
	}
}

int main(int argc, char const* argv[]) {
	ios::sync_with_stdio(false);
	cin.tie(nullptr);

	init();
	execute_queries();

	return 0;
}
