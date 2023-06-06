// https://www.acmicpc.net/problem/16978

#include <cstdint>
#include <iostream>
#include <memory>
#include <vector>

using namespace std;

using sz = size_t;
using i32 = int32_t;
using i64 = int64_t;

constexpr sz MAX_QUERIES = 100000;
constexpr sz MAX_NODES = MAX_QUERIES * 20;

struct Node {
	Node* l;
	Node* r;
	i64 v;

	Node* copy(Node const* other) {
		l = other->l;
		r = other->r;
		v = other->v;
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

	void init(Node* node, i32 const left, i32 const right, vector<i32> const& a) {
		if (right - left == 1) {
			node->v = a[left - 1];
		}
		else {
			i32 const mid = (left + right) / 2;
			init(node->l = Node::allocate(), left, mid, a);
			init(node->r = Node::allocate(), mid, right, a);
			node->v = node->l->v + node->r->v;
		}
	}

	void update(Node* node, i32 const left, i32 const right, i32 const index, i32 const value) {
		if (right - left == 1) {
			node->v = value;
		}
		else {
			i32 const mid = (left + right) / 2;
			if (index < mid) {
				update(node->l = Node::allocate()->copy(node->l), left, mid, index, value);
			}
			else {
				update(node->r = Node::allocate()->copy(node->r), mid, right, index, value);
			}
			node->v = node->l->v + node->r->v;
		}
	}

	i64 query(Node const* node, i32 const left, i32 const right, i32 const beg, i32 const end) {
		if (right <= beg || end <= left) {
			return 0LL;
		}

		if (beg <= left && right <= end) {
			return node->v;
		}
		else {
			i32 const mid = (left + right) / 2;
			return query(node->l, left, mid, beg, end) + query(node->r, mid, right, beg, end);
		}
	}

public:

	PersistentSegmentTree(vector<i32> const& a) : size{ i32(a.size()) }, roots(0) {
		roots.reserve(MAX_QUERIES + 1);
		roots.push_back(Node::allocate());
		init(roots[0], 1, size + 1, a);
	}

	void update(i32 const index, i32 const value) {
		roots.push_back(Node::allocate()->copy(roots.back()));
		update(roots.back(), 1, size + 1, index, value);
	}

	i64 query(i32 const k, i32 const beg, i32 const end) {
		return query(roots[k], 1, size + 1, beg, end);
	}

};

unique_ptr<PersistentSegmentTree> pst;

void init() {
	i32 N; cin >> N;
	vector<i32> a(N);
	for (i32 i = 0; i < N; ++i) {
		cin >> a[i];
	}

	pst.reset(new PersistentSegmentTree{ a });
}

void execute_queries() {
	i32 Q; cin >> Q;
	while (0 <= --Q) {
		i32 cmd; cin >> cmd;
		if (cmd == 1) {
			i32 index; cin >> index;
			i32 value; cin >> value;
			pst->update(index, value);
		}
		else {
			i32 k; cin >> k;
			i32 beg; cin >> beg;
			i32 end; cin >> end;
			cout << pst->query(k, beg, end + 1) << '\n';
		}
	}

	cout.flush();
}

int main(int argc, char const* argv[]) {
	ios::sync_with_stdio(false);
	cin.tie(nullptr);

	init();
	execute_queries();

	return 0;
}

