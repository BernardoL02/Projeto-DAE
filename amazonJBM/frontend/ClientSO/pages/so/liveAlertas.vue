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
const errorMessages = ref([]);

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Função para buscar alertas de encomendas
const fetchAlertasEncomendas = async () => {
  try {
    const token = getToken(); // Função para obter o token do sessionStorage
    const response = await fetch(`${api}/encomendas/alertas`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}` // Adiciona o token no cabeçalho
      }
    });

    if (!response.ok) throw new Error("Erro ao buscar alertas das encomendas");

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
    errorMessages.value.push("Erro ao buscar alertas das encomendas.");
    console.error("Erro ao carregar alertas:", error);
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

  <div v-if="errorMessages.length" class="text-red-500 text-center mt-4">
    <p v-for="(error, index) in errorMessages" :key="index">{{ error }}</p>
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
