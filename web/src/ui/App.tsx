import React, { useMemo, useState } from "react";
import { createOrder, getOrder, getOrders, listProducts, login } from "./api";

type Session = { token: string; email: string; roles: string[] };

function cents(n: number) { return `$${(n / 100).toFixed(2)}`; }

export default function App() {
  const [email, setEmail] = useState("user@demo.com");
  const [password, setPassword] = useState("User@123");
  const [session, setSession] = useState<Session | null>(null);
  const [error, setError] = useState<string | null>(null);

  const [products, setProducts] = useState<Array<any>>([]);
  const [cart, setCart] = useState<Record<string, number>>({});
  const [orders, setOrders] = useState<Array<any>>([]);
  const [selectedOrder, setSelectedOrder] = useState<any>(null);

  const total = useMemo(() => {
    const map = new Map(products.map((p) => [p.id, p]));
    let t = 0;
    for (const [pid, q] of Object.entries(cart)) {
      const p = map.get(pid);
      if (p) t += p.priceCents * q;
    }
    return t;
  }, [cart, products]);

  async function doLogin() {
    setError(null);
    try {
      const r = await login(email, password);
      setSession({ token: r.token, email: r.email, roles: r.roles });
      setProducts(await listProducts(r.token));
      setOrders(await getOrders(r.token));
    } catch (e: any) {
      setError(e?.message ?? String(e));
    }
  }

  async function refreshOrders() {
    if (!session) return;
    setOrders(await getOrders(session.token));
  }

  async function placeOrder() {
    if (!session) return;
    setError(null);
    try {
      const items = Object.entries(cart)
        .filter(([, q]) => q > 0)
        .map(([productId, quantity]) => ({ productId, quantity }));
      const r = await createOrder(session.token, items);
      setCart({});
      await refreshOrders();
      setSelectedOrder(await getOrder(session.token, r.orderId));
    } catch (e: any) {
      setError(e?.message ?? String(e));
    }
  }

  async function viewOrder(id: string) {
    if (!session) return;
    setSelectedOrder(await getOrder(session.token, id));
  }

  return (
    <div style={{ fontFamily: "system-ui, sans-serif", margin: 24, maxWidth: 980 }}>
      <h1>Enterprise Order Management</h1>
      <p style={{ color: "#444" }}>
        Event-driven microservices demo (Kafka + Spring Boot) • JWT/RBAC • Postgres • Redis • React
      </p>

      {!session ? (
        <div style={{ border: "1px solid #ddd", padding: 16, borderRadius: 12 }}>
          <h2>Login</h2>
          <p>Try: <code>user@demo.com / User@123</code> or <code>admin@demo.com / Admin@123</code></p>
          <div style={{ display: "flex", gap: 12, flexWrap: "wrap" }}>
            <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="email" />
            <input value={password} onChange={(e) => setPassword(e.target.value)} placeholder="password" type="password" />
            <button onClick={doLogin}>Login</button>
          </div>
          {error && <pre style={{ color: "crimson", whiteSpace: "pre-wrap" }}>{error}</pre>}
        </div>
      ) : (
        <>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <div><b>Logged in:</b> {session.email} • roles: {session.roles.join(", ")}</div>
            <button onClick={() => setSession(null)}>Logout</button>
          </div>

          <hr style={{ margin: "16px 0" }} />

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 16 }}>
            <div style={{ border: "1px solid #ddd", padding: 16, borderRadius: 12 }}>
              <h2>Products</h2>
              <table width="100%" cellPadding={6}>
                <thead>
                  <tr>
                    <th align="left">Name</th>
                    <th align="right">Price</th>
                    <th align="right">Stock</th>
                    <th align="right">Qty</th>
                  </tr>
                </thead>
                <tbody>
                  {products.map((p) => (
                    <tr key={p.id}>
                      <td>{p.name}</td>
                      <td align="right">{cents(p.priceCents)}</td>
                      <td align="right">{p.stock}</td>
                      <td align="right">
                        <input
                          style={{ width: 60 }}
                          type="number"
                          min={0}
                          value={cart[p.id] ?? 0}
                          onChange={(e) => setCart({ ...cart, [p.id]: Number(e.target.value) })}
                        />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>

              <div style={{ marginTop: 12, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <b>Total: {cents(total)}</b>
                <button onClick={placeOrder} disabled={total === 0}>Place order</button>
              </div>
            </div>

            <div style={{ border: "1px solid #ddd", padding: 16, borderRadius: 12 }}>
              <h2>Your Orders</h2>
              <button onClick={refreshOrders}>Refresh</button>
              <ul>
                {orders.map((o: any) => (
                  <li key={o.id} style={{ marginTop: 8 }}>
                    <button onClick={() => viewOrder(o.id)}>View</button>{" "}
                    <b>{o.id}</b> — {o.status} — {cents(o.totalCents)}
                  </li>
                ))}
              </ul>
            </div>
          </div>

          {selectedOrder && (
            <div style={{ marginTop: 16, border: "1px solid #ddd", padding: 16, borderRadius: 12 }}>
              <h2>Order Details</h2>
              <div><b>ID:</b> {selectedOrder.id}</div>
              <div><b>Status:</b> {selectedOrder.status}</div>
              <div><b>Total:</b> {cents(selectedOrder.totalCents)}</div>
              <h3>Items</h3>
              <ul>
                {selectedOrder.items.map((it: any, idx: number) => (
                  <li key={idx}>
                    product {it.productId} — qty {it.quantity} — unit {cents(it.priceCents)}
                  </li>
                ))}
              </ul>
              <p style={{ color: "#555" }}>Tip: status updates asynchronously (Kafka). Refresh the list/order.</p>
            </div>
          )}

          {error && <pre style={{ color: "crimson", whiteSpace: "pre-wrap" }}>{error}</pre>}
        </>
      )}
    </div>
  );
}
