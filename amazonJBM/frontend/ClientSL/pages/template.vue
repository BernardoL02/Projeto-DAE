<script setup>
import { defineProps } from 'vue';
import { useRouter } from 'vue-router';

const icon = '/Images/IconJBM.png';

const props = defineProps({
  currentPage: {
    type: String,
    required: true
  }
});

const router = useRouter();

// Função de logout
const logout = () => {
  sessionStorage.removeItem('token');
  router.push('/');
};
</script>

<template>
  <nav class="fundoNavBar flex justify-center items-center bg-PrimaryColor p-4 shadow-xl">
    <div class="container mx-auto flex justify-between items-center md:px-16">

      <div class="flex items-center justify-center">
        <div class="relative w-[110px] h-[110px] rounded-full bg-white p-[-2] pl-[1px]">
          <img :src="icon" class="rounded-full w-full h-full object-cover">
        </div>
      </div>

      <!-- Páginas de navegação -->
      <div class="space-x-16 hidden md:flex">
        <a href="gestao" :class="currentPage === 'gestao' ? 'highlighted' : ''"
          class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
          Gestao
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'gestao' ? 'w-full' : ''"></span>
        </a>
        <a href="PorEntregar" :class="currentPage === 'Por Entregar' ? 'highlighted' : ''"
          class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
          Por Entregar
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'Por Entregar' ? 'w-full' : ''"></span>
        </a>
        <a href="CriarEncomenda" :class="currentPage === 'CriarEncomenda' ? 'highlighted' : ''"
          class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
          Criar Encomenda
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'CriarEncomenda' ? 'w-full' : ''"></span>
        </a>
        <a href="DashBoard" :class="currentPage === 'DashBoard' ? 'highlighted' : ''"
          class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
          DashBoard
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'DashBoard' ? 'w-full' : ''"></span>
        </a>
      </div>

      <!-- Nome do sistema e botão de logout -->
      <div class="flex flex-col items-end space-y-2">
        <!-- Nome do sistema -->
        <div>
          <div class="text-white font-bold text-xl">Amazon JBM</div>
          <div class="text-white text-sm">Sistema de Logística</div>
        </div>

        <!-- Botão de logout menor -->
        <button @click="logout"
          class="flex items-center bg-red-500 text-white font-semibold px-3 py-1 text-sm rounded-full hover:bg-red-700 transition shadow-md">
          <i class="fas fa-sign-out-alt mr-2"></i> Logout
        </button>
      </div>
    </div>
  </nav>

  <div>

  </div>
</template>

<style scoped>
body {
  background-color: white;
  height: 100vh;
  margin: 0;
}

.fundoNavBar {
  background: linear-gradient(40deg, #202c38 25%, #66c981 120%);
}

button {
  cursor: pointer;
}
</style>
