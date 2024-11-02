<script setup>
import Template from '../../template.vue';
import Table from '../../table.vue';
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRuntimeConfig, useRoute } from '#app';

const config = useRuntimeConfig();
const api = config.public.API_URL;
const route = useRoute();

const sensorTableTitles = ['ID Sensor', 'Data' , 'Valor' , 'Bateria', 'Estado', 'ID Encomenda', 'ID Volume'];
const sensorTableData = ref([]);
const errorMessages = ref([]);
const tiposSensores = ref([]);
const selectedTipo = ref(route.params.tipo || ''); // Tipo do sensor selecionado
const currentPage = 'Ultimos Valores';

// Função para formatar a data
const formatDate = (dateString) => {
  return dateString ? dateString.replace('T', ' ') : "Data não disponível";
};


// Função para buscar tipos de sensores para o dropdown
const fetchTiposSensores = async () => {
  try {
    const response = await fetch(`${api}/sl/tipoSensores`);
    if (!response.ok) throw new Error("Erro ao buscar tipos de sensores");

    const data = await response.json();
    tiposSensores.value = data.map(sensor => sensor.tipo);
  } catch (error) {
    errorMessages.value.push("Erro ao carregar tipos de sensores.");
  }
};

// Função para buscar o último valor do tipo de sensor selecionado
const fetchUltimoValor = async () => {
  if (!selectedTipo.value) return;

  try {
    const response = await fetch(`${api}/so/sensor/${selectedTipo.value}`);
    if (!response.ok) throw new Error("Erro ao buscar último valor do sensor");

    const data = await response.json();
    sensorTableData.value = data.map(sensor => ({
      idSensor: sensor.id,
      data: formatDate(sensor.timeStamp), 
      valor: sensor.valor,
      bateria: sensor.bateria,
      estado: sensor.estado,                                      
      idEncomenda: sensor.id_encomenda,   
      idVolume: sensor.id_volume                   
    }));

  } catch (error) {
    errorMessages.value.push(`Erro ao carregar dados do sensor ${selectedTipo.value}`);
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
    <div class="flex justify-center mr-24 mt-10">
      <h1 v-if="selectedTipo" class="text-xl font-semibold text-gray-700">
        Último Valor dos Sensores - {{ selectedTipo }}
      </h1>
    </div>
  
    <!-- Mensagem de erro, caso ocorra -->
    <div v-if="errorMessages.length" class="text-red-500 text-center mt-4">
      <p v-for="(error, index) in errorMessages" :key="index">{{ error }}</p>
    </div>
  
    <!-- Tabela com os valores dos sensores -->
    <Table v-if="sensorTableData.length" :tableTitles="sensorTableTitles" :tableData="sensorTableData" :mostrarAcoes="false" />
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
