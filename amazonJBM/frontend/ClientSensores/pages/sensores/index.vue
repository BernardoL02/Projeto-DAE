<template>
  <Template />

  <div class="flex justify-center mr-24 mt-20">
    <h1>Sensores</h1>
  </div>

  <!-- Exibir erros, caso ocorram -->
  <div v-if="errorMessages.length">
    <h2>Erros:</h2>
    <ul>
      <li v-for="error in errorMessages" :key="error">{{ error }}</li>
    </ul>
  </div>

  <!-- Exibir a tabela com os dados carregados e passar a função updateSensor para o componente Table -->
  <Table v-else :tableTitles="tableTitles" :tableData="tableData" @update="updateSensor" />
</template>

<style scoped>
  h1 {
    font-size: 1.5rem;
    font-weight: bold;
  }
</style>

<script setup>
import Template from '../template.vue';
import Table from '../table.vue';
import { ref, onMounted, computed } from 'vue';
import { useRuntimeConfig, useFetch } from '#app';

const config = useRuntimeConfig();
const api = config.public.API_URL;

const tableTitles = ['ID', 'Tipo de Sensor', 'Valor', 'Estado', 'Timestamp', 'Bateria', 'ID Encomenda', 'ID Volume'];
const errorMessages = ref([]);

// Função para formatar a data
const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString();
};

// Realiza a chamada à API diretamente
const { data, error } = await useFetch(`${api}/sensor/`);

// Computed para processar os dados da API e permitir edição dos campos "Valor" e "Bateria"
const tableData = computed(() => {
  if (error.value) {
    errorMessages.value.push("Erro ao carregar dados dos sensores.");
    console.error("Erro ao carregar dados dos sensores:", error.value);
    return [];
  }
  
  // Se os dados estão disponíveis, extrair o valor dos refs para evitar [object Object]
  return data.value ? data.value.map(sensor => ({
    id: sensor.id,
    tipoNome: sensor.tipoNome,
    valor: sensor.valor,  // Extrai o valor diretamente para evitar [object Object]
    estado: sensor.estado,
    timeStamp: formatDate(sensor.timeStamp),
    bateria: sensor.bateria,  // Extrai o valor diretamente para evitar [object Object]
    idEncomenda: sensor.idEncomenda,
    idVolume: sensor.idVolume,
  })) : [];
});

const updateSensor = async (sensor) => {
  try {
    const response = await fetch(`${api}/sensor/${sensor.id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        valor: sensor.valor,
        bateria: sensor.bateria,
      }),
    });

    if (!response.ok) throw new Error(`Erro ao atualizar sensor com ID ${sensor.id}`);
    console.log(`Sensor ${sensor.id} atualizado com sucesso`);
  } catch (err) {
    errorMessages.value.push(`Erro ao atualizar o sensor ${sensor.id}: ${err.message}`);
  }
};


</script>
