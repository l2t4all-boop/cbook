let allContacts = [];
let currentPage = 1;
let pageSize = 8;
let filteredContacts = [];
const pageSizeOptions = [5, 8, 10, 20, 50];

async function loadContacts() {
  const spinner = document.getElementById("loading-spinner");
  const tableWrapper = document.getElementById("table-wrapper");
  const emptyState = document.getElementById("empty-state");

  if (spinner) spinner.classList.remove("d-none");
  if (tableWrapper) tableWrapper.classList.add("d-none");
  if (emptyState) emptyState.classList.add("d-none");

  try {
    const res = await fetch(API.CONTACTS, { headers: authHeaders() });
    const data = await res.json();
    if (res.status === 401 || res.status === 403) {
      clearAuth();
      window.location.href = "login.html";
      return;
    }
    if (data.success) {
      allContacts = data.data || [];
      filteredContacts = allContacts;
      currentPage = 1;
      renderPage();
    } else {
      showToast(data.message || "Failed to load contacts", "danger");
    }
  } catch (err) {
    showToast("Connection error. Is the server running?", "danger");
  } finally {
    if (spinner) spinner.classList.add("d-none");
  }
}

function renderPage() {
  const totalPages = Math.max(1, Math.ceil(filteredContacts.length / pageSize));
  if (currentPage > totalPages) currentPage = totalPages;

  const start = (currentPage - 1) * pageSize;
  const pageContacts = filteredContacts.slice(start, start + pageSize);

  renderContacts(pageContacts, filteredContacts.length);
  renderPagination(totalPages);
}

function renderContacts(contacts, totalCount) {
  const tbody = document.getElementById("contacts-tbody");
  const emptyState = document.getElementById("empty-state");
  const tableWrapper = document.getElementById("table-wrapper");

  if (totalCount === 0) {
    if (tableWrapper) tableWrapper.classList.add("d-none");
    if (emptyState) emptyState.classList.remove("d-none");
    document.getElementById("pagination-wrapper").classList.add("d-none");
    return;
  }

  if (emptyState) emptyState.classList.add("d-none");
  if (tableWrapper) tableWrapper.classList.remove("d-none");

  tbody.innerHTML = contacts
    .map(
      (c) => `
    <tr>
      <td>
        <div class="d-flex align-items-center">
          <div class="avatar-sm me-3">${getInitials(c.name)}</div>
          <div>
            <div class="fw-semibold">${escapeHtml(c.name)}</div>
          </div>
        </div>
      </td>
      <td><i class="bi bi-envelope me-1 text-muted"></i>${escapeHtml(c.email)}</td>
      <td><i class="bi bi-phone me-1 text-muted"></i>${escapeHtml(c.mobile)}</td>
      <td>${c.dob ? formatDate(c.dob) : '<span class="text-muted">-</span>'}</td>
      <td>
        <div class="btn-group btn-group-sm">
          <button class="btn btn-outline-primary" onclick="openEditModal('${c.id}')" title="Edit">
            <i class="bi bi-pencil"></i>
          </button>
          <button class="btn btn-outline-danger" onclick="confirmDelete('${c.id}', '${escapeHtml(c.name)}')" title="Delete">
            <i class="bi bi-trash"></i>
          </button>
        </div>
      </td>
    </tr>`
    )
    .join("");
}

function renderPagination(totalPages) {
  const wrapper = document.getElementById("pagination-wrapper");
  if (!wrapper) return;

  if (filteredContacts.length === 0) {
    wrapper.classList.add("d-none");
    return;
  }

  wrapper.classList.remove("d-none");

  const start = (currentPage - 1) * pageSize + 1;
  const end = Math.min(currentPage * pageSize, filteredContacts.length);

  let pagesHtml = "";

  // Previous
  pagesHtml += `<li class="page-item ${currentPage === 1 ? "disabled" : ""}">
    <a class="page-link" href="#" onclick="goToPage(${currentPage - 1}); return false;">
      <i class="bi bi-chevron-left"></i>
    </a>
  </li>`;

  // Page numbers
  const maxVisible = 5;
  let startPage = Math.max(1, currentPage - Math.floor(maxVisible / 2));
  let endPage = Math.min(totalPages, startPage + maxVisible - 1);
  if (endPage - startPage < maxVisible - 1) {
    startPage = Math.max(1, endPage - maxVisible + 1);
  }

  if (startPage > 1) {
    pagesHtml += `<li class="page-item"><a class="page-link" href="#" onclick="goToPage(1); return false;">1</a></li>`;
    if (startPage > 2) {
      pagesHtml += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
    }
  }

  for (let i = startPage; i <= endPage; i++) {
    pagesHtml += `<li class="page-item ${i === currentPage ? "active" : ""}">
      <a class="page-link" href="#" onclick="goToPage(${i}); return false;">${i}</a>
    </li>`;
  }

  if (endPage < totalPages) {
    if (endPage < totalPages - 1) {
      pagesHtml += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
    }
    pagesHtml += `<li class="page-item"><a class="page-link" href="#" onclick="goToPage(${totalPages}); return false;">${totalPages}</a></li>`;
  }

  // Next
  pagesHtml += `<li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
    <a class="page-link" href="#" onclick="goToPage(${currentPage + 1}); return false;">
      <i class="bi bi-chevron-right"></i>
    </a>
  </li>`;

  const sizeOptions = pageSizeOptions
    .map((s) => `<option value="${s}" ${s === pageSize ? "selected" : ""}>${s}</option>`)
    .join("");

  wrapper.innerHTML = `
    <div class="d-flex align-items-center justify-content-between flex-wrap gap-2 px-3 py-3">
      <div class="d-flex align-items-center gap-2">
        <span class="text-muted small">Showing ${start}-${end} of ${filteredContacts.length}</span>
        <select class="form-select form-select-sm" style="width: auto" onchange="changePageSize(this.value)">
          ${sizeOptions}
        </select>
        <span class="text-muted small">per page</span>
      </div>
      <nav><ul class="pagination pagination-sm mb-0">${pagesHtml}</ul></nav>
    </div>`;
}

function goToPage(page) {
  const totalPages = Math.ceil(filteredContacts.length / pageSize);
  if (page < 1 || page > totalPages) return;
  currentPage = page;
  renderPage();
}

function changePageSize(value) {
  pageSize = parseInt(value);
  currentPage = 1;
  renderPage();
}

function getInitials(name) {
  return name
    .split(" ")
    .map((w) => w[0])
    .join("")
    .substring(0, 2)
    .toUpperCase();
}

function escapeHtml(str) {
  if (!str) return "";
  const div = document.createElement("div");
  div.textContent = str;
  return div.innerHTML;
}

function formatDate(dateStr) {
  if (!dateStr) return "";
  const d = new Date(dateStr + "T00:00:00");
  return d.toLocaleDateString("en-US", { year: "numeric", month: "short", day: "numeric" });
}

async function createContact(e) {
  e.preventDefault();
  const btn = e.target.querySelector('button[type="submit"]');
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Saving...';

  const contact = {
    name: document.getElementById("c-name").value.trim(),
    email: document.getElementById("c-email").value.trim(),
    mobile: document.getElementById("c-mobile").value.trim(),
    dob: document.getElementById("c-dob").value || null,
  };

  try {
    const res = await fetch(API.CONTACTS, {
      method: "POST",
      headers: authHeaders(),
      body: JSON.stringify(contact),
    });
    const data = await res.json();
    if (data.success) {
      showToast("Contact created successfully!", "success");
      bootstrap.Modal.getInstance(document.getElementById("addContactModal")).hide();
      e.target.reset();
      loadContacts();
    } else {
      showToast(data.message || "Failed to create contact", "danger");
    }
  } catch (err) {
    showToast("Connection error", "danger");
  } finally {
    btn.disabled = false;
    btn.innerHTML = '<i class="bi bi-plus-circle me-1"></i>Save Contact';
  }
}

function openEditModal(id) {
  const contact = allContacts.find((c) => c.id === id);
  if (!contact) return;

  document.getElementById("e-id").value = contact.id;
  document.getElementById("e-name").value = contact.name;
  document.getElementById("e-email").value = contact.email;
  document.getElementById("e-mobile").value = contact.mobile;
  document.getElementById("e-dob").value = contact.dob || "";

  new bootstrap.Modal(document.getElementById("editContactModal")).show();
}

async function updateContact(e) {
  e.preventDefault();
  const id = document.getElementById("e-id").value;
  const btn = e.target.querySelector('button[type="submit"]');
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Updating...';

  const contact = {
    name: document.getElementById("e-name").value.trim(),
    email: document.getElementById("e-email").value.trim(),
    mobile: document.getElementById("e-mobile").value.trim(),
    dob: document.getElementById("e-dob").value || null,
  };

  try {
    const res = await fetch(`${API.CONTACTS}/${id}`, {
      method: "PUT",
      headers: authHeaders(),
      body: JSON.stringify(contact),
    });
    const data = await res.json();
    if (data.success) {
      showToast("Contact updated successfully!", "success");
      bootstrap.Modal.getInstance(document.getElementById("editContactModal")).hide();
      loadContacts();
    } else {
      showToast(data.message || "Failed to update contact", "danger");
    }
  } catch (err) {
    showToast("Connection error", "danger");
  } finally {
    btn.disabled = false;
    btn.innerHTML = '<i class="bi bi-check-circle me-1"></i>Update Contact';
  }
}

function confirmDelete(id, name) {
  document.getElementById("delete-contact-name").textContent = name;
  document.getElementById("confirm-delete-btn").onclick = () => deleteContact(id);
  new bootstrap.Modal(document.getElementById("deleteConfirmModal")).show();
}

async function deleteContact(id) {
  const btn = document.getElementById("confirm-delete-btn");
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Deleting...';

  try {
    const res = await fetch(`${API.CONTACTS}/${id}`, {
      method: "DELETE",
      headers: authHeaders(),
    });
    const data = await res.json();
    if (data.success) {
      showToast("Contact deleted", "success");
      bootstrap.Modal.getInstance(document.getElementById("deleteConfirmModal")).hide();
      loadContacts();
    } else {
      showToast(data.message || "Failed to delete contact", "danger");
    }
  } catch (err) {
    showToast("Connection error", "danger");
  } finally {
    btn.disabled = false;
    btn.innerHTML = '<i class="bi bi-trash me-1"></i>Delete';
  }
}

async function searchContacts() {
  const keyword = document.getElementById("search-input").value.trim();
  if (!keyword) {
    filteredContacts = allContacts;
    currentPage = 1;
    renderPage();
    return;
  }

  const spinner = document.getElementById("loading-spinner");
  if (spinner) spinner.classList.remove("d-none");

  try {
    const res = await fetch(`${API.SEARCH}?keyword=${encodeURIComponent(keyword)}`, {
      headers: authHeaders(),
    });
    const data = await res.json();
    if (data.success) {
      filteredContacts = data.data || [];
      currentPage = 1;
      renderPage();
    } else {
      showToast(data.message || "Search failed", "danger");
    }
  } catch (err) {
    showToast("Connection error", "danger");
  } finally {
    if (spinner) spinner.classList.add("d-none");
  }
}

async function importContacts(e) {
  e.preventDefault();
  const fileInput = document.getElementById("import-file");
  if (!fileInput.files.length) {
    showToast("Please select a file", "warning");
    return;
  }

  const btn = e.target.querySelector('button[type="submit"]');
  btn.disabled = true;
  btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Importing...';

  const formData = new FormData();
  formData.append("file", fileInput.files[0]);

  try {
    const res = await fetch(API.IMPORT, {
      method: "POST",
      headers: authHeadersNoContent(),
      body: formData,
    });
    const data = await res.json();
    if (data.success) {
      const count = data.data ? data.data.length : 0;
      showToast(`Imported ${count} contacts!`, "success");
      bootstrap.Modal.getInstance(document.getElementById("importModal")).hide();
      e.target.reset();
      loadContacts();
    } else {
      showToast(data.message || "Import failed", "danger");
    }
  } catch (err) {
    showToast("Connection error", "danger");
  } finally {
    btn.disabled = false;
    btn.innerHTML = '<i class="bi bi-upload me-1"></i>Import';
  }
}

async function exportContacts(contentType) {
  try {
    const res = await fetch(`${API.EXPORT}?contentType=${encodeURIComponent(contentType)}`, {
      headers: authHeaders(),
    });

    if (res.headers.get("content-type")?.includes("application/json")) {
      const data = await res.json();
      if (!data.success) {
        showToast(data.message || "Export failed", "danger");
        return;
      }
    }

    const blob = await res.blob();
    const ext = contentType === "text/csv" ? "csv" : "xlsx";
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `contacts.${ext}`;
    a.click();
    URL.revokeObjectURL(url);
    showToast("Export started!", "success");
  } catch (err) {
    showToast("Export failed", "danger");
  }
}

let searchTimeout;
function onSearchInput() {
  clearTimeout(searchTimeout);
  searchTimeout = setTimeout(searchContacts, 400);
}