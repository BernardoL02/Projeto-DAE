<script setup>
import Template from '../../template.vue';
import Table from '../../table.vue';

import { useRoute } from 'vue-router';
import { ref, computed } from 'vue';


const currentPage = "Sensores";

const route = useRoute()
const config = useRuntimeConfig()
const api = config.public.API_URL
const username = route.params.username;

const tableData = ref([]);
const tableTitles = ['ID', 'Data', 'Valor', 'Bateria', 'estado', 'ID Encomenda', 'ID Volume'];
const tiposSensores = ref([]);
const selectedTipo = ref('Temperatura');

const getToken = () => sessionStorage.getItem('token');

// Função para buscar os tipos de sensores
const fetchTiposSensores = async () => {
  try {
    const token = sessionStorage.getItem('token');
    if (!token) throw new Error("Token não encontrado. Redirecionar para login.");

    const response = await fetch(`${api}/sensor/tipos`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) throw new Error("Erro ao buscar tipos de sensores");

    const data = await response.json();
    tiposSensores.value = data.map(sensor => sensor.tipo);
  } catch (error) {
    console.error("Erro ao carregar tipos de sensores:", error);
  }
};

fetchTiposSensores()

const fetchSensorData = async (tipoSensor) => {
  try {
    const token = getToken();
    const fetchedData = await $fetch(`${api}/sensor/${tipoSensor}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    tableData.value = fetchedData.map(sensor => [
      sensor.id,
      new Date(sensor.timeStamp).toLocaleString(),
      sensor.valor,
      `${sensor.bateria}%`,
      sensor.estado,
      sensor.id_encomenda,
      sensor.id_volume
    ]);

  } catch (error) {
    console.error("Erro ao buscar encomendas! " + error);
  }
};


fetchSensorData(selectedTipo.value)

watch(
  selectedTipo,
  (newValue, oldValue) => {
    if (newValue !== oldValue) {
      fetchSensorData(newValue);
    }
  }
);
</script>

<template>

  <Template :username="username" :currentPage="currentPage"></Template> <!-- Importar o Template -->

  <div class="flex justify-between items-center ml-28 mr-28 mt-20">
    <div>
      <h1 class="text-xl font-semibold text-gray-800">Sensores</h1>
      <p class="mt-1 text-lg text-gray-700">Consulte os últimos valores registados pelos sensores</p>
    </div>

    <!-- Filtro de Encomendas -->
    <div class="w-64">
      <select v-model="selectedTipo" name="Sensores" id="sensores"
        class="block w-full p-2 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-SecundaryColor focus:border-SecundaryColor text-gray-700">
        <option v-for="tipo in tiposSensores" :key="tipo" :value="tipo">
          {{ tipo }}
        </option>
      </select>
    </div>
  </div>

  <Table :tableTitles="tableTitles" :tableData="tableData" :mostrarOperacoes="false" />

</template>

<style scoped>
body {
  background-color: white;
  height: 100vh;
  margin: 0;
}

h1 {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>
