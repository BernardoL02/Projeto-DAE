<script setup>
import { defineProps } from 'vue';
import { useRouter } from 'vue-router';
import { ref } from 'vue';

const icon = '/Images/IconJBM.png';
const showMobileMenu = ref(false);

const props = defineProps({
  currentPage: {
    type: String,
    required: true,
  },
});

const router = useRouter();

// Função de logout
const logout = () => {
  sessionStorage.removeItem('token');
  router.push('/');
};
</script>

<template>
  <nav class="fundoNavBar flex items-center justify-between bg-PrimaryColor p-4 shadow-xl">
    <!-- Logo e Botão Hambúrguer -->
    <div class="flex items-center">
      <!-- Ícone -->
      <div class="relative w-[80px] h-[80px] rounded-full bg-white mr-4">
        <img :src="icon" class="rounded-full w-full h-full object-cover">
      </div>
    </div>

    <!-- Texto (visível apenas em dispositivos móveis) -->
    <div class="flex-grow flex flex-col items-center text-white md:hidden">
      <span class="text-base font-bold">Amazon JBM</span>
      <span class="text-sm">Sistema de Logística</span>
    </div>

    <!-- Botão Hambúrguer (aparece no mobile) -->
    <button @click="showMobileMenu = !showMobileMenu"
      class="text-white focus:outline-none focus:ring-2 focus:ring-white md:hidden ml-auto">
      <i class="fas fa-bars text-2xl"></i>
    </button>

    <!-- Navegação Desktop -->
    <div class="hidden md:flex space-x-8">
      <a href="gestao" :class="currentPage === 'gestao' ? 'highlighted' : ''"
        class="text-white font-semibold relative hover:font-bold transition duration-150 ease-in-out group">
        Gestao
        <span
          class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
          :class="currentPage === 'gestao' ? 'w-full' : ''"></span>
      </a>
      <a href="PorEntregar" :class="currentPage === 'Por Entregar' ? 'highlighted' : ''"
        class="text-white font-semibold relative hover:font-bold transition duration-150 ease-in-out group">
        Por Entregar
        <span
          class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
          :class="currentPage === 'Por Entregar' ? 'w-full' : ''"></span>
      </a>
      <a href="Entregues" :class="currentPage === 'Entregues' ? 'highlighted' : ''"
        class="text-white font-semibold relative hover:font-bold transition duration-150 ease-in-out group">
        Entregues
        <span
          class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
          :class="currentPage === 'Entregues' ? 'w-full' : ''"></span>
      </a>
      <a href="CriarEncomenda" :class="currentPage === 'CriarEncomenda' ? 'highlighted' : ''"
        class="text-white font-semibold relative hover:font-bold transition duration-150 ease-in-out group">
        Criar Encomenda
        <span
          class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
          :class="currentPage === 'CriarEncomenda' ? 'w-full' : ''"></span>
      </a>
      <a href="DashBoard" :class="currentPage === 'DashBoard' ? 'highlighted' : ''"
        class="text-white font-semibold relative hover:font-bold transition duration-150 ease-in-out group">
        DashBoard
        <span
          class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
          :class="currentPage === 'DashBoard' ? 'w-full' : ''"></span>
      </a>
    </div>

    <!-- Botão Logout -->
    <div class="hidden md:flex items-center space-x-4">
      <div class="flex flex-col text-right">
        <span class="text-white font-bold">Amazon JBM</span>
        <span class="text-white text-sm">Sistema de Logística</span>
      </div>
      <button @click="logout"
        class="bg-red-500 text-white font-semibold px-3 py-1 text-sm rounded-full hover:bg-red-700 transition shadow-md">
        <i class="fas fa-sign-out-alt mr-2"></i> Logout
      </button>
    </div>
  </nav>

  <!-- Menu Mobile -->
  <div v-if="showMobileMenu" class="bg-PrimaryColor text-white py-4">
    <div class="flex flex-col space-y-4 px-4">
      <a href="gestao" @click="showMobileMenu = false"
        :class="currentPage === 'gestao' ? 'bg-SecundaryColor p-2 rounded text-black font-semibold' : 'hover:bg-SecundaryColor p-2 rounded transition text-white'">
        Gestao
      </a>
      <a href="PorEntregar" @click="showMobileMenu = false"
        :class="currentPage === 'Por Entregar' ? 'bg-SecundaryColor p-2 rounded text-black font-semibold' : 'hover:bg-SecundaryColor p-2 rounded transition text-white'">
        Por Entregar
      </a>
      <a href="Entregues" @click="showMobileMenu = false"
        :class="currentPage === 'Entregues' ? 'bg-SecundaryColor p-2 rounded text-black font-semibold' : 'hover:bg-SecundaryColor p-2 rounded transition text-white'">
        Entregues
      </a>
      <a href="CriarEncomenda" @click="showMobileMenu = false"
        :class="currentPage === 'CriarEncomenda' ? 'bg-SecundaryColor p-2 rounded text-black font-semibold' : 'hover:bg-SecundaryColor p-2 rounded transition text-white'">
        Criar Encomenda
      </a>
      <a href="DashBoard" @click="showMobileMenu = false"
        :class="currentPage === 'DashBoard' ? 'bg-SecundaryColor p-2 rounded text-black font-semibold' : 'hover:bg-SecundaryColor p-2 rounded transition text-white'">
        DashBoard
      </a>
      <button @click="logout"
        class="bg-red-500 text-white font-semibold px-4 py-2 rounded-full hover:bg-red-700 transition mt-2">
        Logout
      </button>
    </div>
  </div>
</template>


<style scoped>
.fundoNavBar {
  background: linear-gradient(40deg, #202c38 25%, #66c981 120%);
}

nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-width: 320px;
  /* Limite mínimo da largura da página */
}

body {
  min-width: 320px;
}

button {
  cursor: pointer;
}

@media (max-width: 768px) {
  .hidden.md\:flex {
    display: none;
  }

  .fundoNavBar {
    padding: 12px;
    flex-direction: row;
    align-items: center;
  }

  .flex.items-center {
    align-items: center;
  }

  .flex-col {
    flex-direction: column;
  }
}

@media (max-width: 360px) {
  .fundoNavBar {
    padding: 8px;
  }

  .text-white {
    font-size: 12px;
  }
}
</style>