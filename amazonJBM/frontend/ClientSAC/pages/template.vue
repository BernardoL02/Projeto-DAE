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
  username: {
    type: String,
    required: false,
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

    <!-- Texto do sistema (visível apenas em dispositivos móveis) -->
    <div class="flex-grow flex flex-col items-center text-white md:hidden">
      <span class="text-base font-bold">Amazon JBM</span>
      <span class="text-sm">Sistema de Apoio ao Cliente</span>
    </div>

    <!-- Botão Hambúrguer (aparece no mobile) -->
    <button @click="showMobileMenu = !showMobileMenu"
      class="text-white focus:outline-none focus:ring-2 focus:ring-white md:hidden ml-auto">
      <i class="fas fa-bars text-2xl"></i>
    </button>

    <!-- Navegação Desktop -->
    <div class="hidden md:flex space-x-8">
      <nuxt-link :to="`/sac/${username}/encomendas`" :class="currentPage === 'Encomendas' ? 'highlighted' : ''"
        class="text-white font-semibold relative hover:font-bold transition duration-150 ease-in-out group">
        Encomendas
        <span
          class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
          :class="currentPage === 'Encomendas' ? 'w-full' : ''"></span>
      </nuxt-link>
      <p href="encomenda" :class="currentPage === 'Encomenda' ? 'highlighted' : ''"
        class="text-white font-semibold relative hover:font-bold transition duration-150 ease-in-out group">
        Encomenda
        <span
          class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
          :class="currentPage === 'Encomenda' ? 'w-full' : ''"></span>
      </p>
      <nuxt-link :to="`/sac/${username}/sensores`" :class="currentPage === 'Sensores' ? 'highlighted' : ''"
        class="text-white font-semibold relative hover:font-bold transition duration-150 ease-in-out group">
        Sensores
        <span
          class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
          :class="currentPage === 'Sensores' ? 'w-full' : ''"></span>
      </nuxt-link>
    </div>

    <!-- Botão Logout -->
    <div class="hidden md:flex items-center space-x-4">
      <div class="flex flex-col text-right">
        <span class="text-white font-bold">Amazon JBM</span>
        <span class="text-white text-sm">Sistema de Apoio ao Cliente</span>
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
      <nuxt-link :to="`/sac/${username}/encomendas`" @click="showMobileMenu = false"
        :class="currentPage === 'Encomendas' ? 'bg-SecundaryColor' : ''"
        class="hover:bg-SecundaryColor p-2 rounded transition text-white">Encomendas</nuxt-link>
      <p @click="showMobileMenu = false" :class="currentPage === 'Encomenda' ? 'bg-SecundaryColor' : ''"
        class="hover:bg-SecundaryColor p-2 rounded transition text-white">Encomenda</p>
      <nuxt-link :to="`/sac/${username}/sensores`" @click="showMobileMenu = false"
        :class="currentPage === 'Sensores' ? 'bg-SecundaryColor' : ''"
        class="hover:bg-SecundaryColor p-2 rounded transition text-white">Sensores</nuxt-link>
      <button @click="logout"
        class="bg-red-500 text-white font-semibold px-4 py-2 rounded-full hover:bg-red-700 transition mt-2">
        Logout
      </button>
    </div>
  </div>
</template>

<style scoped>
body {
  background-color: white;
  margin: 0;
}

.fundoNavBar {
  background: linear-gradient(40deg, #202c38 25%, #66c981 120%);
}

nav {
  min-width: 320px;
}

@media (max-width: 768px) {
  .hidden.md\:flex {
    display: none;
  }
}
</style>
