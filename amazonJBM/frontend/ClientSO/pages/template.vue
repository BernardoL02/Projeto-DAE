<script setup>
import { ref, onMounted } from "vue";
import { useRuntimeConfig } from "#app";
import { useRouter } from "vue-router";

const icon = "/Images/IconJBM.png";

const props = defineProps({
  currentPage: {
    type: String,
    required: false,
  },
});

const config = useRuntimeConfig();
const api = config.public.API_URL;

const showMobileMenu = ref(false); // Controle do menu mobile
const dropdownStates = ref({
  sensores: false, // Estado do dropdown de sensores
  outroDropdown: false, // Estado do outro dropdown
});

const tiposSensores = ref([]);
const selectedTipo = ref("");

const router = useRouter();

// Função de logout
const logout = () => {
  sessionStorage.removeItem("token");
  router.push("/");
};

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem("token");

// Função para buscar os tipos de sensores
const fetchTiposSensores = async () => {
  try {
    const token = getToken();
    if (!token) throw new Error("Token não encontrado");

    const response = await fetch(`${api}/sensor/tipo`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) throw new Error("Erro ao buscar tipos de sensores");

    const data = await response.json();
    tiposSensores.value = data.map((sensor) => sensor.tipo);
  } catch (error) {
    console.error("Erro ao carregar tipos de sensores:", error);
  }
};

// Função para alternar o estado de um dropdown
const toggleDropdown = (dropdownKey) => {
  dropdownStates.value[dropdownKey] = !dropdownStates.value[dropdownKey];
};

// Função para fechar todos os dropdowns e o menu mobile
const closeAllDropdowns = () => {
  Object.keys(dropdownStates.value).forEach((key) => {
    dropdownStates.value[key] = false;
  });
  showMobileMenu.value = false;
};

// Redireciona ao selecionar um tipo
const selectTipo = (tipo) => {
  selectedTipo.value = tipo;
  dropdownStates.value.sensores = false; // Fecha o dropdown de sensores
  window.location.href = `/so/UltimoValor/${tipo}`;
};

onMounted(fetchTiposSensores);
</script>



<template>
  <nav class="fundoNavBar flex justify-between items-center p-4 shadow-xl xl:px-16">
    <!-- Logo -->
    <div class="flex items-center">
      <div class="relative w-[100px] h-[100px] rounded-full bg-white mr-4">
        <img :src="icon" class="rounded-full w-full h-full object-cover" />
      </div>
    </div>


    <!-- Texto do sistema (visível apenas em dispositivos móveis) -->
    <div class="flex-grow flex flex-col items-center text-white xl:hidden">
      <span class="text-base font-bold">Amazon JBM</span>
      <span class="text-sm">Sistema Operacional</span>
    </div>


    <!-- Botão Hambúrguer -->
    <button @click="showMobileMenu = !showMobileMenu" class="text-white focus:outline-none  xl:hidden">
      <i class="fas fa-bars text-2xl"></i>
    </button>

    <!-- Navegação Desktop -->
    <div class="hidden xl:flex space-x-8 items-center">
      <!-- Links de Navegação -->
      <div class="space-x-10 flex items-center">
        <a href="/so/EmProcessamento" :class="currentPage === 'EmProcessamento' ? 'highlighted' : ''"
          class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
          Em Processamento
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'EmProcessamento' ? 'w-full' : ''"></span>
        </a>
        <a href="/so/PorEntregar" :class="currentPage === 'Por Entregar' ? 'highlighted' : ''"
          class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
          Por Entregar
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'Por Entregar' ? 'w-full' : ''"></span>
        </a>
        <a href="/so/Entregues" :class="currentPage === 'Entregues' ? 'highlighted' : ''"
          class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
          Entregues
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'Entregues' ? 'w-full' : ''"></span>
        </a>
        <a href="/so/Cancelada" :class="currentPage === 'Cancelada' ? 'highlighted' : ''"
          class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
          Canceladas
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'Cancelada' ? 'w-full' : ''"></span>
        </a>
        <a href="/so/liveAlertas" :class="currentPage === 'liveAlertas' ? 'highlighted' : ''"
          class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
          Live Alertas
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'liveAlertas' ? 'w-full' : ''"></span>
        </a>
      </div>
      <!-- Dropdown para Desktop -->
      <div class="relative dropdown-container flex items-center">
        <button @click.stop="toggleDropdown('sensores')"
          class="bg-transparent text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group focus:outline-none">
          {{ selectedTipo || "Últimos Valores" }}
          <span
            class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
            :class="currentPage === 'Ultimos Valores' ? 'w-full' : ''"></span>
        </button>
        <div v-show="dropdownStates.sensores"
          class="absolute top-full mt-2 w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-50">
          <div class="py-1">
            <a v-for="tipo in tiposSensores" :key="tipo" @click.prevent="selectTipo(tipo)"
              class="block px-4 py-2 text-gray-700 hover:bg-green-100 hover:text-green-700 cursor-pointer">
              {{ tipo }}
            </a>
          </div>
        </div>
      </div>
    </div>

    <!-- Botão Logout -->
    <div class="hidden lg:flex items-center space-x-4">
      <div class="flex flex-col text-right">
        <span class="text-white font-bold">Amazon JBM</span>
        <span class="text-white text-sm">Sistema Operacional</span>
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
      <a href="/so/EmProcessamento" @click="showMobileMenu = false"
        :class="currentPage === 'EmProcessamento' ? 'bg-SecundaryColor p-2 rounded text-black font-semibold' : 'hover:bg-SecundaryColor p-2 rounded transition text-white'">
        Em Processamento
      </a>
      <a href="/so/PorEntregar" @click="showMobileMenu = false"
        :class="currentPage === 'Por Entregar' ? 'bg-SecundaryColor p-2 rounded text-black font-semibold' : 'hover:bg-SecundaryColor p-2 rounded transition text-white'">
        Por Entregar
      </a>
      <a href="/so/Entregues" @click="showMobileMenu = false"
        :class="currentPage === 'Entregues' ? 'bg-SecundaryColor p-2 rounded text-black font-semibold' : 'hover:bg-SecundaryColor p-2 rounded transition text-white'">
        Entregues
      </a>
      <a href="/so/Cancelada" @click="showMobileMenu = false"
        :class="currentPage === 'Cancelada' ? 'bg-SecundaryColor p-2 rounded text-black font-semibold' : 'hover:bg-SecundaryColor p-2 rounded transition text-white'">
        Canceladas
      </a>
      <a href="/so/liveAlertas" @click="showMobileMenu = false"
        :class="currentPage === 'liveAlertas' ? 'bg-SecundaryColor p-2 rounded text-black font-semibold' : 'hover:bg-SecundaryColor p-2 rounded transition text-white'">
        Live Alertas
      </a>
      <!-- Dropdown para Mobile -->
      <div class="relative dropdown-container">
        <button @click="toggleDropdown('sensores')"
          class="bg-transparent text-white font-semibold p-2 focus:outline-none">
          {{ selectedTipo || "Últimos Valores" }}
        </button>
        <div v-show="dropdownStates.sensores" class="absolute bg-white rounded-md shadow-lg text-black">
          <a v-for="tipo in tiposSensores" :key="tipo" @click.prevent="selectTipo(tipo)"
            :class="currentPage === tipo ? 'bg-green-100 text-green-700 p-2 rounded' : 'block px-4 py-2 text-gray-700 hover:bg-green-100 hover:text-green-700 cursor-pointer'">
            {{ tipo }}
          </a>
        </div>
      </div>
      <button @click="logout" class="bg-red-500 text-white px-4 py-2 rounded">
        Logout
      </button>
    </div>
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

.highlighted {
  font-weight: bold;
}
</style>
