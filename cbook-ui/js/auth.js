function getToken() {
  return localStorage.getItem("token");
}

function getUsername() {
  return localStorage.getItem("username");
}

function saveAuth(data) {
  localStorage.setItem("token", data.token);
  localStorage.setItem("username", data.username);
  localStorage.setItem("role", data.role);
}

function clearAuth() {
  localStorage.removeItem("token");
  localStorage.removeItem("username");
  localStorage.removeItem("role");
}

function isLoggedIn() {
  return !!getToken();
}

function authHeaders() {
  return {
    Authorization: `Bearer ${getToken()}`,
    "Content-Type": "application/json",
  };
}

function authHeadersNoContent() {
  return {
    Authorization: `Bearer ${getToken()}`,
  };
}

function updateNavbar() {
  const authNav = document.getElementById("auth-nav");
  if (!authNav) return;

  if (isLoggedIn()) {
    authNav.innerHTML = `
      <li class="nav-item">
        <span class="nav-link text-light">
          <i class="bi bi-person-circle me-1"></i>${getUsername()}
        </span>
      </li>
      <li class="nav-item">
        <button class="btn btn-outline-light btn-sm ms-2" onclick="logout()">
          <i class="bi bi-box-arrow-right me-1"></i>Logout
        </button>
      </li>`;
  } else {
    authNav.innerHTML = "";
  }
}

async function login(e) {
  e.preventDefault();
  const btn = e.target.querySelector('button[type="submit"]');
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Signing in...';

  try {
    const res = await fetch(API.AUTH.LOGIN, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username: document.getElementById("login-username").value.trim(),
        password: document.getElementById("login-password").value,
      }),
    });
    const data = await res.json();
    if (data.success) {
      saveAuth(data.data);
      showToast("Welcome back, " + data.data.username + "!", "success");
      setTimeout(() => (window.location.href = "index.html"), 500);
    } else {
      showToast(data.message || "Login failed", "danger");
    }
  } catch (err) {
    showToast("Connection error. Is the server running?", "danger");
  } finally {
    btn.disabled = false;
    btn.innerHTML = '<i class="bi bi-box-arrow-in-right me-1"></i>Sign In';
  }
}

async function register(e) {
  e.preventDefault();
  const btn = e.target.querySelector('button[type="submit"]');
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Creating account...';

  const password = document.getElementById("reg-password").value;
  const confirm = document.getElementById("reg-confirm").value;
  if (password !== confirm) {
    showToast("Passwords do not match", "danger");
    btn.disabled = false;
    btn.innerHTML = '<i class="bi bi-person-plus me-1"></i>Create Account';
    return;
  }

  try {
    const res = await fetch(API.AUTH.REGISTER, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username: document.getElementById("reg-username").value.trim(),
        email: document.getElementById("reg-email").value.trim(),
        password: password,
        mobile: document.getElementById("reg-mobile").value.trim(),
      }),
    });
    const data = await res.json();
    if (data.success) {
      saveAuth(data.data);
      showToast("Account created! Welcome, " + data.data.username + "!", "success");
      setTimeout(() => (window.location.href = "index.html"), 500);
    } else {
      showToast(data.message || "Registration failed", "danger");
    }
  } catch (err) {
    showToast("Connection error. Is the server running?", "danger");
  } finally {
    btn.disabled = false;
    btn.innerHTML = '<i class="bi bi-person-plus me-1"></i>Create Account';
  }
}

function logout() {
  clearAuth();
  showToast("Logged out successfully", "info");
  setTimeout(() => (window.location.href = "login.html"), 400);
}

function requireAuth() {
  if (!isLoggedIn()) {
    window.location.href = "login.html";
    return false;
  }
  return true;
}