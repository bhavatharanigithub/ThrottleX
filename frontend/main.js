const API_BASE = 'http://localhost:8080/api';

function pretty(obj) {
  try {
    if (typeof obj === 'string') {
      return JSON.stringify(JSON.parse(obj), null, 2);
    }
    return JSON.stringify(obj, null, 2);
  } catch {
    return String(obj);
  }
}

async function generateToken() {
  const role = document.getElementById('role').value;
  const out = document.getElementById('tokenOutput');
  out.value = 'Loading...';
  try {
    const res = await fetch(`${API_BASE}/generate-token?role=${encodeURIComponent(role)}`);
    const data = await res.json();
    out.value = pretty(data);
    if (data.token) {
      document.getElementById('tokenInput').value = data.token;
    }
  } catch (e) {
    out.value = `Error: ${e.message}`;
  }
}

async function callProtected() {
  const token = document.getElementById('tokenInput').value.trim();
  const out = document.getElementById('protectedOutput');
  out.value = 'Loading...';
  try {
    const res = await fetch(`${API_BASE}/protected`, {
      headers: token ? { Authorization: `Bearer ${token}` } : {}
    });
    const text = await res.text();
    out.value = pretty(text);
  } catch (e) {
    out.value = `Error: ${e.message}`;
  }
}

async function proxyRequest() {
  const targetUrl = document.getElementById('targetUrl').value.trim();
  const method = document.getElementById('proxyMethod').value;
  const bodyText = document.getElementById('proxyBody').value.trim();
  const out = document.getElementById('proxyOutput');
  out.value = 'Loading...';

  if (!targetUrl) {
    out.value = 'Please enter a target URL.';
    return;
  }

  const opts = {
    method,
    headers: {
      'X-Target-Url': targetUrl
    }
  };

  if (bodyText && method !== 'GET' && method !== 'HEAD') {
    opts.headers['Content-Type'] = 'application/json';
    opts.body = bodyText;
  }

  try {
    const res = await fetch(`${API_BASE}/proxy`, opts);
    const text = await res.text();
    out.value = pretty(text);
  } catch (e) {
    out.value = `Error: ${e.message}`;
  }
}

document.getElementById('generateTokenBtn').addEventListener('click', generateToken);
document.getElementById('callProtectedBtn').addEventListener('click', callProtected);
document.getElementById('proxyBtn').addEventListener('click', proxyRequest);

