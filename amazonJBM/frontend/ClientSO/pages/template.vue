<script setup>
import { ref, onMounted } from 'vue';
import { useRuntimeConfig } from '#app';

const icon = '/Images/IconJBM.png';

const props = defineProps({
  currentPage: {
    type: String,
    required: false
  }
});

const config = useRuntimeConfig();
const api = config.public.API_URL;
const tiposSensores = ref([]);
const selectedTipo = ref('');
const isDropdownOpen = ref(false); // Controle do estado de abertura do dropdown

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Função para buscar os tipos de sensores
const fetchTiposSensores = async () => {
  try {
    const token = getToken(); // Função para obter o token do sessionStorage
    const response = await fetch(`${api}/sensor/tipos`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}` // Adiciona o token no cabeçalho
      }
    });

    if (!response.ok) throw new Error("Erro ao buscar tipos de sensores");

    const data = await response.json();
    tiposSensores.value = data.map(sensor => sensor.tipo);
  } catch (error) {
    console.error("Erro ao carregar tipos de sensores:", error);
  }
};


onMounted(fetchTiposSensores);

// Fecha o dropdown ao clicar fora dele
const handleClickOutside = (event) => {
  const dropdown = document.querySelector('.dropdown-container');
  if (dropdown && !dropdown.contains(event.target)) {
    isDropdownOpen.value = false;
  }
};

// Adicione o método selectTipo para redirecionar ao selecionar um tipo
const selectTipo = (tipo) => {
  selectedTipo.value = tipo;
  isDropdownOpen.value = false; // Fecha o dropdown após a seleção
  // Redireciona para a rota dinâmica
  window.location.href = `/so/UltimoValor/${tipo}`;
};


// Adiciona e remove o event listener no momento certo
onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});
</script>

<template>
  <!-- Barra de Navegação com Dropdown -->
  <nav class="fundoNavBar flex justify-center items-center p-4 shadow-xl">
    <div class="container mx-auto flex justify-between items-center md:px-16">

      <!-- Logotipo -->
      <div class="flex items-center justify-center">
        <div class="relative w-[110px] h-[110px] rounded-full bg-white p-[-2] pl-[1px]">
          <img :src="icon" class="rounded-full w-full h-full object-cover">
        </div>
      </div>

      <!-- Páginas de navegação e dropdown de sensores -->
      <div class="flex items-center space-x-8">
        <!-- Links de Navegação -->
        <div class="space-x-10 hidden md:flex">
          <a href="/so/Pendentes" :class="currentPage === 'Pendentes' ? 'highlighted' : ''"
            class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
            Pendentes
            <span
              class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
              :class="currentPage === 'Pendentes' ? 'w-full' : ''"></span>
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
          <a href="/so/liveAlertas" :class="currentPage === 'liveAlertas' ? 'highlighted' : ''"
            class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
            Live Alertas
            <span
              class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full"
              :class="currentPage === 'liveAlertas' ? 'w-full' : ''"></span>
          </a>
        </div>

        <!-- Dropdown customizado para seleção de sensores -->
        <div class="relative dropdown-container" @mouseenter="isDropdownOpen = true"
          @mouseleave="isDropdownOpen = false">
          <button class="bg-transparent text-white font-semibold p-2 focus:outline-none">
            {{ selectedTipo || 'Últimos Valores' }}
          </button>
          <!-- Lista de tipos de sensores, sempre no DOM mas controlado por v-show -->
          <div v-show="isDropdownOpen"
            class="absolute left-0 -mt-1 w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-50">
            <div class="py-1">
              <a v-for="tipo in tiposSensores" :key="tipo" @click.prevent="selectTipo(tipo)"
                class="block px-4 py-2 text-gray-700 hover:bg-green-100 hover:text-green-700 cursor-pointer">
                {{ tipo }}
              </a>
            </div>
          </div>
        </div>
      </div>

      <!-- Nome do sistema -->
      <div class="text-right">
        <div class="text-white font-bold">Amazon JBM</div>
        <div class="text-white">Sistema Operacional</div>
      </div>
    </div>
  </nav>
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
