<template>
  <Template />

  <div class="flex justify-center items-center mr-24 mt-20 space-x-4">
    <h1>Sensores</h1>
    <!-- Botão para controlar o intervalo de atualização dos valores -->
    <button
      @click="toggleValueUpdate"
      class="bg-blue-500 text-white py-1 px-4 rounded hover:bg-blue-700 transition"
    >
      {{ isValueUpdateRunning ? "Stop" : "Start" }}
    </button>
  </div>

  <div v-if="errorMessages.length">
    <h2>Erros:</h2>
    <ul>
      <li v-for="error in errorMessages" :key="error">{{ error }}</li>
    </ul>
  </div>

  <div
    v-if="successMessage"
    class="fixed top-0 left-0 w-full flex justify-center mt-4 z-50 transition-transform transform-gpu"
    :class="{ 'animate-slide-down': successMessage, 'animate-slide-up': !successMessage }"
  >
    <div class="bg-green-500 text-white py-2 px-4 mr-28 rounded shadow-md">
      {{ successMessage }}
    </div>
  </div>

  <Table :tableTitles="tableTitles" :tableData="tableData" @update="confirmUpdateSensor" @cancel="cancelSensor" />
</template>

<style scoped>
  h1 {
    font-size: 1.5rem;
    font-weight: bold;
  }

  @keyframes slideDown {
    0% {
      transform: translateY(-100%);
      opacity: 0;
    }
    50% {
      transform: translateY(0);
      opacity: 1;
    }
    100% {
      transform: translateY(0);
      opacity: 1;
    }
  }
  @keyframes slideUp {
    0% {
      transform: translateY(0);
      opacity: 1;
    }
    100% {
      transform: translateY(-100%);
      opacity: 0;
    }
  }

  .animate-slide-down {
    animation: slideDown 0.5s forwards;
  }
  .animate-slide-up {
    animation: slideUp 0.5s forwards;
  }
</style>

<script setup>
import Template from '../template.vue';
import Table from '../table.vue';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRuntimeConfig } from '#app';

const config = useRuntimeConfig();
const api = config.public.API_URL;

const tableTitles = ['ID', 'Tipo de Sensor', 'Valor', 'Estado', 'Timestamp', 'Bateria', 'ID Encomenda', 'ID Volume'];
const errorMessages = ref([]);
const successMessage = ref('');
const isValueUpdateRunning = ref(false); // Estado do intervalo de atualização

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString();
};

const fetchSensors = async () => {
  try {
    const response = await fetch(`${api}/sensor/`);
    if (!response.ok) throw new Error("Erro ao carregar dados dos sensores");

    const data = await response.json();
    return data.map(sensor => ({
      id: sensor.id,
      tipoNome: sensor.tipoNome,
      valor: sensor.valor,
      valMax: sensor.valMax,
      valMin: sensor.valMin,
      estado: sensor.estado,
      timeStamp: formatDate(sensor.timeStamp),
      bateria: sensor.bateria,
      idEncomenda: sensor.idEncomenda,
      idVolume: sensor.idVolume,
    }));
  } catch (error) {
    errorMessages.value.push("Erro ao carregar dados dos sensores.");
    console.error("Erro ao carregar dados dos sensores:", error);
    return [];
  }
};

const tableData = ref([]);

// Função para gerar valores aleatórios para cada sensor e atualizar no backend
const updateSensorValues = async () => {
  for (const sensor of tableData.value) {
    if (sensor.estado === 'inativo') continue;

    if (sensor.tipoNome === 'GPS') {
      const lat = (40 + Math.random()).toFixed(5);
      const long = (-74 - Math.random()).toFixed(5);
      sensor.valor = `${lat}, ${long}`;
    } else {
      let randomValue = Math.random() * (sensor.valMax - sensor.valMin) + sensor.valMin;
      if (Math.random() < 0.2) {
        randomValue = Math.random() < 0.5
          ? sensor.valMax + Math.random() * 5
          : sensor.valMin - Math.random() * 5;
      }
      sensor.valor = parseFloat(randomValue.toFixed(1));
    }

    sensor.bateria = Math.max(sensor.bateria - 2, 0);

    try {
      await fetch(`${api}/sensor/${sensor.id}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ valor: sensor.valor, bateria: sensor.bateria }),
      });
    } catch (error) {
      console.error(`Erro ao atualizar valor do sensor ${sensor.id}:`, error);
      errorMessages.value.push(`Erro ao atualizar o sensor ${sensor.id}: ${error.message}`);
    }
  }
  tableData.value = await fetchSensors();
};

// Função para verificar e criar alertas
const checkAlert = async (sensor) => {
  if (sensor.tipoNome !== 'GPS' && (sensor.valor > sensor.valMax || sensor.valor < sensor.valMin)) {
    const mensagem = sensor.valor > sensor.valMax
      ? `Valor acima do limite máximo (${sensor.valMax})`
      : `Valor abaixo do limite mínimo (${sensor.valMin})`;

    try {
      const response = await fetch(`${api}/sensor/`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          mensagem: `${mensagem} para o sensor ${sensor.tipoNome}`,
          id_sensor: sensor.id,
          valor: sensor.valor,
          bateria: sensor.bateria,
          id_volume: sensor.idVolume,
        }),
      });

      if (response.ok) {
        successMessage.value = `Alerta criado para sensor ${sensor.id}: ${mensagem}!`;
        setTimeout(() => (successMessage.value = ''), 3000);
      }
    } catch (err) {
      errorMessages.value.push(`Erro ao criar alerta para o sensor ${sensor.id}: ${err.message}`);
    }
  }
};

const checkAlertsPeriodically = () => {
  tableData.value.forEach(sensor => checkAlert(sensor));
};

// Intervalos de atualização
let valueUpdateInterval;
let alertCheckInterval;

// Função para alternar o estado de atualização de valores
const toggleValueUpdate = () => {
  if (isValueUpdateRunning.value) {
    clearInterval(valueUpdateInterval);
    isValueUpdateRunning.value = false;
  } else {
    valueUpdateInterval = setInterval(updateSensorValues, 5000);
    isValueUpdateRunning.value = true;
  }
};

// Inicia a verificação de alertas, mas mantém a atualização de valores desligada inicialmente
onMounted(async () => {
  tableData.value = await fetchSensors();
  alertCheckInterval = setInterval(checkAlertsPeriodically, 10000);
});

onUnmounted(() => {
  clearInterval(valueUpdateInterval);
  clearInterval(alertCheckInterval);
});

// Função para atualizar manualmente o valor de um sensor específico
const confirmUpdateSensor = async (sensor) => {
  sensor.bateria = Math.max(sensor.bateria - 2, 0);
  try {
    const response = await fetch(`${api}/sensor/${sensor.id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ valor: sensor.valor, bateria: sensor.bateria }),
    });

    if (!response.ok) throw new Error(`Erro ao atualizar sensor com ID ${sensor.id}`);

    successMessage.value = `Sensor ${sensor.id} confirmado com sucesso!`;
    setTimeout(() => (successMessage.value = ''), 3000);
  } catch (error) {
    errorMessages.value.push(`Erro ao confirmar atualização do sensor ${sensor.id}: ${error.message}`);
  }
};

// Função para cancelar o sensor
const cancelSensor = async (sensor) => {
  try {
    const response = await fetch(`${api}/sensor/${sensor.id}/desativar`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ estado: "inativo" }),
    });

    if (!response.ok) throw new Error(`Erro ao cancelar sensor com ID ${sensor.id}`);

    sensor.estado = "inativo";

    successMessage.value = `Sensor com id:${sensor.id} foi desativado com sucesso!`;

    setTimeout(async () => {
      successMessage.value = '';
      tableData.value = await fetchSensors();
    }, 3000);
  } catch (err) {
    errorMessages.value.push(`Erro ao cancelar o sensor ${sensor.id}: ${err.message}`);
  }
};
</script>
