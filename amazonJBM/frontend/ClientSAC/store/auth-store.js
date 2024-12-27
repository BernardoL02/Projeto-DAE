import { defineStore } from "pinia";

export const useAuthStore = defineStore("authStore", () => {
  const config = useRuntimeConfig();
  const api = config.public.API_URL;

  const token = ref(null);
  const user = ref(null);
  const messages = ref([]);

  const loginFormData = reactive({
    username: "Bernardo",
    password: "123",
  });

  function logout() {
    token.value = null;
    user.value = null;
  }

  async function login() {
    try {
      await $fetch(`${api}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        body: loginFormData,
        onResponse({ request, response, options }) {
          messages.value.push({
            method: options.method,
            request: request,
            status: response.status,
            statusText: response.statusText,
            payload: response._data,
          });
          if (response.status == 200) {
            token.value = response._data;
            getUserInfo();
          }
        },
      });
    } catch (e) {
      console.error("login request failed: ", e);
    }
  }

  async function getUserInfo() {
    try {
      await $fetch(`${api}/auth/user`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization: `Bearer ${token.value}`,
        },
        onResponse({ request, response, options }) {
          messages.value.push({
            method: options.method,
            request: request,
            status: response.status,
            statusText: response.statusText,
            payload: response._data,
          });
          if (response.status == 200) {
            user.value = response._data;
          }
        },
      });
    } catch (e) {
      console.error("user info request failed: ", e);
    }
  }

  return { token, user, logout, login, loginFormData };
});
