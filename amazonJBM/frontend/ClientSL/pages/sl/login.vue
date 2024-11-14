<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const icon = '/Images/IconJBM.png';
const title = "Sistema de Logistica";

const username = ref('');
const password = ref('');
const errorMessage = ref(''); // Variável para armazenar mensagem de erro

const config = useRuntimeConfig();
const api = config.public.API_URL;
const router = useRouter();

const login = async () => {
  try {
    errorMessage.value = ''; // Limpa a mensagem de erro antes de tentar login

    const response = await fetch(`${api}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      body: JSON.stringify({
        username: username.value,
        password: password.value
      })
    });

    if (!response.ok) {
      throw new Error('Nome de usuário ou senha incorretos');
    }

    const token = await response.text();

    // Armazena o token no localStorage
    sessionStorage.setItem('token', token);

    // Navega para a página de gestão
    router.push('/sl/gestao');
  } catch (error) {
    errorMessage.value = error.message || 'Erro ao fazer login';
    console.error('Erro ao fazer login:', error);
  }
};
</script>

<template>
  <div class="login-container h-screen flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-3xl p-8 w-96">
      <div class="flex items-center justify-center mb-6">
        <img :src="icon" alt="Logo do Site" width="200px" height="200px"> 
      </div>
      
      <h2 class="text-2xl font-bold text-center mb-6"> {{ title }}</h2>

      <form @submit.prevent="login">
        <div class="mb-4">
          <input v-model="username" type="text" placeholder="Username" class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-SecundaryColor"/>
        </div>

        <div class="mb-6">
          <input v-model="password" type="password" placeholder="Password" class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-SecundaryColor"/>
        </div>

        <div class="flex justify-center items-center text-sm mb-4 space-x-28 mt-6">
          <button type="submit" class="bg-PrimaryColor hover:bg-SecundaryColor text-white font-semibold py-2 px-8 rounded-full transition duration-300 ease-in-out shadow-md focus:outline-none focus:ring-2 focus:ring-SecundaryColor">
            Login
          </button>
        </div>

        <!-- Exibe a mensagem de erro se houver -->
        <div v-if="errorMessage" class="text-red-500 text-center mt-4">
          {{ errorMessage }}
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
