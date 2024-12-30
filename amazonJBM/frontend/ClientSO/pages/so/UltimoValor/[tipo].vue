<script setup>
import Template from '../../template.vue';
import Table from '../../table.vue';
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRuntimeConfig, useRoute } from '#app';

const config = useRuntimeConfig();
const api = config.public.API_URL;
const route = useRoute();

const sensorTableTitles = ['ID Sensor', 'Data', 'Valor', 'Bateria', 'Estado', 'ID Encomenda', 'ID Volume'];
const sensorTableData = ref([]);
const tiposSensores = ref([]);
const selectedTipo = ref(route.params.tipo || ''); // Tipo do sensor selecionado
const currentPage = 'Ultimos Valores';

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');


const errorMessages = ref([]);
const showError = (message) => {
  errorMessages.value.push(message);

  setTimeout(() => {
    errorMessages.value.shift();
  }, 5000);
};

// Função para buscar tipos de sensores para o dropdown
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

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const data = await response.json();
    tiposSensores.value = data.map(sensor => sensor.tipo);
  } catch (error) {
    showError(error.message);
  }
};

// Função para buscar o último valor do tipo de sensor selecionado
const fetchUltimoValor = async () => {
  if (!selectedTipo.value) return;

  try {
    const token = getToken(); // Função para obter o token
    const response = await fetch(`${api}/sensor/${selectedTipo.value}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}` // Adiciona o token no cabeçalho
      }
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const data = await response.json();
    sensorTableData.value = data.map(sensor => ({
      idSensor: sensor.id,
      data: new Date(sensor.timeStamp).toLocaleString(),
      valor: sensor.valor,
      bateria: sensor.bateria,
      estado: sensor.estado,
      idEncomenda: sensor.id_encomenda,
      idVolume: sensor.id_volume
    }));

  } catch (error) {
    showError(error.message);
  }
};


// Atualiza automaticamente os dados a cada 3 segundos
let updateInterval;
onMounted(() => {
  fetchTiposSensores();
  fetchUltimoValor();
  updateInterval = setInterval(fetchUltimoValor, 3000);
});

onUnmounted(() => {
  clearInterval(updateInterval);
});

// Atualiza os dados ao trocar o tipo no dropdown
watch(selectedTipo, fetchUltimoValor);

</script>

<template>
  <Template :currentPage="currentPage" />

  <!-- Título da Página -->
  <div class="flex justify-center mr-24 mt-20">
    <h1 v-if="selectedTipo" class="text-xl font-semibold text-gray-700">
      Último Valor dos Sensores - {{ selectedTipo }}
    </h1>
  </div>

  <!-- Mensagens de erro estilizadas -->
  <div v-if="errorMessages.length" class="fixed bottom-4 right-4 space-y-2 z-[100]">
    <div v-for="(error, index) in errorMessages" :key="index"
      class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96">
      <h3 class="font-semibold text-lg mb-2">Erro</h3>
      <p>{{ error }}</p>
    </div>
  </div>

  <!-- Tabela com os valores dos sensores -->
  <Table v-if="sensorTableData.length" :tableTitles="sensorTableTitles" :tableData="sensorTableData"
    :mostrarAcoes="false" />
</template>


<style scoped>
h1 {
  font-size: 1.5rem;
  font-weight: bold;
}

select {
  min-width: 220px;
  border: none;
}
</style>
