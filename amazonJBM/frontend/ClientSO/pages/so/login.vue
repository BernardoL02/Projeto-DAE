<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const icon = "/Images/IconJBM.png";
const title = "Sistema Operacional";

const username = ref("");
const password = ref("");

const config = useRuntimeConfig();
const api = config.public.API_URL;
const router = useRouter();

const errorMessages = ref([]);
const showError = (message) => {
  errorMessages.value.push(message);

  setTimeout(() => {
    errorMessages.value.shift();
  }, 5000);
};

const login = async () => {
  try {
    errorMessages.value.shift();

    const response = await fetch(`${api}/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify({
        username: username.value,
        password: password.value,
      }),
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const token = await response.text();

    // Armazena o token no localStorage
    sessionStorage.setItem("token", token);

    // Navega para a página de gestão
    router.push("/so/EmProcessamento");
  } catch (error) {
    showError("Nome de utilizador ou password incorretos");
  }
};
</script>

<template>
  <!-- Mensagens de erro estilizadas -->
  <div
    v-if="errorMessages.length"
    class="fixed bottom-4 right-4 space-y-2 z-[100]"
  >
    <div
      v-for="(error, index) in errorMessages"
      :key="index"
      class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96"
    >
      <h3 class="font-semibold text-lg mb-2">Erro</h3>
      <p>{{ error }}</p>
    </div>
  </div>

  <div class="login-container h-screen flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-3xl p-8 w-96">
      <div class="flex items-center justify-center mb-6">
        <img :src="icon" alt="Logo do Site" width="200px" height="200px" />
      </div>

      <h2 class="text-2xl font-bold text-center mb-6">{{ title }}</h2>

      <form @submit.prevent="login">
        <div class="mb-4">
          <input
            v-model="username"
            type="text"
            placeholder="Username"
            class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-SecundaryColor"
          />
        </div>

        <div class="mb-6">
          <input
            v-model="password"
            type="password"
            placeholder="Password"
            class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-SecundaryColor"
          />
        </div>

        <div
          class="flex justify-center items-center text-sm mb-4 space-x-28 mt-6"
        >
          <button
            type="submit"
            class="bg-PrimaryColor hover:bg-SecundaryColor text-white font-semibold py-2 px-8 rounded-full transition duration-300 ease-in-out shadow-md focus:outline-none focus:ring-2 focus:ring-SecundaryColor"
          >
            Login
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(40deg, #202c38 10%, #66c981 130%);
}
</style>
