<script setup>
import Template from '../template.vue';
import Table from '../table.vue';
import { ref, onMounted } from 'vue';
import { useRuntimeConfig, useRoute } from '#app';

const route = useRoute();
const config = useRuntimeConfig();

const encomendasTableTitles = ['ID Encomenda', 'Utilizador', 'Data de Expedição', 'Data de Entrega', 'Estado'];
const encomendasTableData = ref([]);
const currentPage = 'Entregues';
const api = config.public.API_URL;

const mostrarAlertasModal = ref(false); // Controla a exibição do modal de alertas
const alertasData = ref([]); // Dados dos alertas para o modal
const errorMessages = ref([]); // Mensagens de erro

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Função para formatar o estado
const formatEstado = (estado) => {
  switch (estado) {
    case 'EmProcessamento':
      return 'Em Processamento';
    case 'PorEntregar':
      return 'Por Entregar';
    case 'Entregue':
      return 'Entregue';
    default:
      return estado;
  }
};

const formatDate = (dateString) => {
  return dateString.replace('T', ' ');
};

// Função para buscar encomendas entregues
const fetchEncomendasEntregues = async () => {
  try {
    const token = getToken(); // Função para obter o token
    const response = await fetch(`${api}/encomendas/estado/Entregue`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) throw new Error("Erro ao buscar encomendas entregues");

    const data = await response.json();
    encomendasTableData.value = data.map(encomenda => ({
      id: encomenda.id,
      username: encomenda.username,
      dataExpedicao: new Date(encomenda.data_expedicao).toLocaleString(),
      dataEntrega: new Date(encomenda.data_entrega).toLocaleString(),
      estado: formatEstado(encomenda.estado)
    }));
  } catch (error) {
    console.error("Erro ao carregar encomendas entregues:", error);
  }
};

// Função para buscar alertas de uma encomenda específica
const verAlertasEncomenda = async (id) => {
  try {
    const response = await fetch(`${api}/encomendas/${id}/alertas`);
    if (!response.ok) throw new Error("Erro ao buscar alertas da encomenda");

    const data = await response.json();

    // Mapear a estrutura de "sensores" e "alertas" conforme a resposta fornecida
    if (data.sensores && Array.isArray(data.sensores)) {
      alertasData.value = data.sensores.map(sensor => ({
        id: sensor.id,
        tipo: sensor.tipo,
        alertas: sensor.alertas.map(alerta => ({
          id: alerta.id,
          mensagem: alerta.mensagem,
          timeStamp: alerta.timeStamp,
          valor: alerta.valor
        }))
      }));
    } else {
      throw new Error("Formato inesperado dos dados de alerta");
    }

    mostrarAlertasModal.value = true;
  } catch (error) {
    errorMessages.value.push(`Erro ao buscar alertas da encomenda ${id}: ${error.message}`);
  }
};

// Chama a função ao montar o componente
onMounted(fetchEncomendasEntregues);
</script>

<template>
  <Template :currentPage="currentPage" />

  <div class="flex justify-center mr-24 mt-20">
    <h1>Sistema de Gestão - Encomendas Entregues</h1>
  </div>

  <div v-if="errorMessages.length" class="text-red-500 text-center mt-4">
    <p v-for="(error, index) in errorMessages" :key="index">{{ error }}</p>
  </div>

  <!-- Tabela para Encomendas Entregues com botão de ver alertas -->
  <Table :tableTitles="encomendasTableTitles" :tableData="encomendasTableData" @verAlertas="verAlertasEncomenda" />

  <!-- Modal de Alertas -->
  <div v-if="mostrarAlertasModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white w-1/2 p-6 rounded shadow-lg relative">
      <button @click="mostrarAlertasModal = false" class="absolute top-2 right-2 text-gray-600 hover:text-gray-900">
        <i class="fas fa-times"></i> <!-- Botão de fechar -->
      </button>
      <h2 class="text-xl font-semibold mb-4">Alertas da Encomenda</h2>
      <div v-if="alertasData.length === 0"
        class="flex flex-col items-center text-gray-600 p-6 border border-gray-300 bg-gray-50 rounded-lg">
        <i class="fas fa-info-circle text-3xl text-blue-500 mb-2"></i>
        <p class="text-lg font-medium">Encomenda sem alertas</p>
      </div>
      <div v-for="sensor in alertasData" :key="sensor.id" class="mb-4 p-4 bg-gray-100 rounded-lg border">
        <p class="font-semibold">Sensor ID: {{ sensor.id }} - Tipo: {{ sensor.tipo }}</p>
        <ul class="mt-2 space-y-2">
          <li v-for="alerta in sensor.alertas" :key="alerta.id" class="p-3 bg-yellow-100 rounded-lg border">
            <p><strong>ID do Alerta:</strong> {{ alerta.id }}</p>
            <p><strong>Data:</strong> {{ formatDate(alerta.timeStamp) }}</p>
            <p><strong>Mensagem:</strong> {{ alerta.mensagem }}</p>
            <p><strong>Valor:</strong> {{ alerta.valor }}</p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
h1 {
  font-size: 1.5rem;
  font-weight: bold;
}

.ml-10 {
  margin-left: 2.5rem;
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
