<script setup>
import Template from '../template.vue';
import Table from '../table.vue';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRuntimeConfig } from '#app';

const config = useRuntimeConfig();
const api = config.public.API_URL;

const alertasTableTitles = ['ID Alerta', 'ID Sensor', 'Valor', 'Mensagem', 'Data', 'Utilizador', 'ID Encomenda'];
const alertasTableData = ref([]);
const currentPage = 'liveAlertas';

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

const errorMessages = ref([]);
const showError = (message) => {
  errorMessages.value.push(message);

  setTimeout(() => {
    errorMessages.value.shift();
  }, 5000);
};

// Função para buscar alertas de encomendas
const fetchAlertasEncomendas = async () => {
  try {
    const token = getToken(); // Função para obter o token do sessionStorage
    const response = await fetch(`${api}/encomenda/alerta`, {
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
    alertasTableData.value = data.map(alerta => ({
      id: alerta.id,
      idSensor: alerta.id_sensor,
      valor: alerta.valor,
      mensagem: alerta.mensagem,
      data: new Date(alerta.time_stamp).toLocaleString(),
      username: alerta.username,
      idEncomenda: alerta.id_encomenda
    }));
  } catch (error) {
    showError(error.message);
  }
};


// Intervalo para verificar novos alertas a cada 3 segundos
let alertInterval;
onMounted(() => {
  fetchAlertasEncomendas();
  alertInterval = setInterval(fetchAlertasEncomendas, 3000);
});

onUnmounted(() => {
  clearInterval(alertInterval);
});
</script>

<template>
  <Template :currentPage="currentPage" />

  <div class="flex justify-center mr-24 mt-20">
    <h1>Alertas de Encomendas Por Entregar</h1>
  </div>

  <!-- Mensagens de erro estilizadas -->
  <div v-if="errorMessages.length" class="fixed bottom-4 right-4 space-y-2 z-[100]">
    <div v-for="(error, index) in errorMessages" :key="index"
      class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96">
      <h3 class="font-semibold text-lg mb-2">Erro</h3>
      <p>{{ error }}</p>
    </div>
  </div>

  <!-- Tabela para exibir os alertas das encomendas sem a coluna de ações -->
  <Table :tableTitles="alertasTableTitles" :tableData="alertasTableData" :mostrarAcoes="false" />
</template>

<style scoped>
h1 {
  font-size: 1.5rem;
  font-weight: bold;
}

.ml-10 {
  margin-left: 2.5rem;
}
</style>
