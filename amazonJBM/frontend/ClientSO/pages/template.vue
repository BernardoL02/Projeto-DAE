<script setup>
import { ref, onMounted } from 'vue';
import { useRuntimeConfig } from '#app';

const icon = '/Images/IconJBM.png';

const props = defineProps({
  currentPage: {
    type: String,
    required: true
  }
});

const config = useRuntimeConfig();
const api = config.public.API_URL;
const tiposSensores = ref([]); 
const selectedTipo = ref('');

// Função para buscar os tipos de sensores
const fetchTiposSensores = async () => {
  try {
    const response = await fetch(`${api}/sl/tipoSensores`);
    if (!response.ok) throw new Error("Erro ao buscar tipos de sensores");

    const data = await response.json();
    tiposSensores.value = data.map(sensor => sensor.tipo);
  } catch (error) {
    console.error("Erro ao carregar tipos de sensores:", error);
  }
};

// Redirecionar para página de valores ao selecionar
const handleTipoChange = () => {
  if (selectedTipo.value) {
    window.location.href = `/so/UltimoValor/${selectedTipo.value}`;
  }
};

onMounted(fetchTiposSensores);
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
          <a href="/so/Pendentes" 
            :class="currentPage === 'Pendentes' ? 'highlighted' : ''" 
            class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
            Pendentes
              <span class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full" 
                    :class="currentPage === 'Pendentes' ? 'w-full' : ''"></span>
          </a>
          <a href="/so/PorEntregar" 
            :class="currentPage === 'Por Entregar' ? 'highlighted' : ''" 
            class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
            Por Entregar
              <span class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full" 
                    :class="currentPage === 'Por Entregar' ? 'w-full' : ''"></span>
          </a>
          <a href="/so/Entregues" 
            :class="currentPage === 'Entregues' ? 'highlighted' : ''" 
            class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
            Entregues
              <span class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full" 
                    :class="currentPage === 'Entregues' ? 'w-full' : ''"></span>
          </a>
          <a href="/so/liveAlertas" 
            :class="currentPage === 'liveAlertas' ? 'highlighted' : ''" 
            class="text-white font-semibold text-base relative hover:font-bold transition duration-150 ease-in-out group">
            Live Alertas
              <span class="absolute bottom-[-8px] left-0 w-0 h-1 bg-SecundaryColor transition-all duration-300 ease-in-out group-hover:w-full" 
                    :class="currentPage === 'liveAlertas' ? 'w-full' : ''"></span>
          </a>
        </div>

        <!-- Dropdown para Seleção de Sensor -->
        <div class="relative">
          <select 
            v-model="selectedTipo" 
            @change="handleTipoChange" 
            class="bg-white text-black p-2 rounded shadow-md focus:outline-none"
          >
            <option disabled value="">Ultimos Valores</option>
            <option v-for="tipo in tiposSensores" :key="tipo" :value="tipo">
              {{ tipo }}
            </option>
          </select>
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

select {
  min-width: 200px;
  border: none;
}

.highlighted {
  font-weight: bold;
}

</style>
