const API_BASE = "http://localhost:8088/api/v1/cbook";

const API = {
  AUTH: {
    LOGIN: `${API_BASE}/auth/login`,
    REGISTER: `${API_BASE}/auth/register`,
  },
  CONTACTS: API_BASE,
  SEARCH: `${API_BASE}/search`,
  IMPORT: `${API_BASE}/import`,
  EXPORT: `${API_BASE}/export`,
};