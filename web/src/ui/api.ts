const API_BASE = import.meta.env.VITE_API_BASE ?? "http://localhost:8080";

export type AuthResponse = { token: string; userId: string; email: string; roles: string[] };

export async function login(email: string, password: string): Promise<AuthResponse> {
  const res = await fetch(`${API_BASE}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password })
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function listProducts(token: string) {
  const res = await fetch(`${API_BASE}/products`, { headers: { Authorization: `Bearer ${token}` } });
  if (!res.ok) throw new Error(await res.text());
  return res.json() as Promise<Array<{ id: string; name: string; priceCents: number; stock: number }>>;
}

export async function createOrder(token: string, items: Array<{ productId: string; quantity: number }>) {
  const res = await fetch(`${API_BASE}/orders`, {
    method: "POST",
    headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
    body: JSON.stringify({ items })
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json() as Promise<{ orderId: string }>;
}

export async function getOrders(token: string) {
  const res = await fetch(`${API_BASE}/orders`, { headers: { Authorization: `Bearer ${token}` } });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getOrder(token: string, id: string) {
  const res = await fetch(`${API_BASE}/orders/${id}`, { headers: { Authorization: `Bearer ${token}` } });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
