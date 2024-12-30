<script setup>
import Template from '../template.vue';
import Table from '../table.vue';
import { ref, onMounted } from 'vue';
import { useRuntimeConfig, useRoute } from '#app';

const route = useRoute();
const config = useRuntimeConfig();
const api = config.public.API_URL;

const encomendasTableTitles = ['ID Encomenda', 'Utilizador', 'Data de Expedição', 'Data de Entrega', 'Estado'];
const encomendasTableData = ref([]);
const currentPage = 'Pendentes';
const successMessage = ref('');
const mostrarAlertasModal = ref(false);
const alertasData = ref([]);
const mostrarTrackingModal = ref(false);
const trackingData = ref([]);
let map = null;
let markers = [];

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Função para exibir a mensagem de erro como um alerta estilizado
const errorMessages = ref([]);
const showError = (message) => {
  errorMessages.value.push(message);

  setTimeout(() => {
    errorMessages.value.shift();
  }, 5000);
};

const loadLeafletCSS = () => {
  const link = document.createElement('link');
  link.rel = 'stylesheet';
  link.href = 'https://unpkg.com/leaflet/dist/leaflet.css';
  document.head.appendChild(link);
};

const loadLeafletJS = () => {
  return new Promise((resolve) => {
    const script = document.createElement('script');
    script.src = 'https://unpkg.com/leaflet/dist/leaflet.js';
    script.onload = resolve;
    document.body.appendChild(script);
  });
};

const formatEstado = (estado) => {
  switch (estado) {
    case 'EmProcessamento':
      return 'Em Processamento';
    case 'PorEntregar':
      return 'Por Entregar';
    default:
      return estado;
  }
};

const fetchEncomendasPendentes = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/encomendas/estado/EmProcessamento`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const data = await response.json();
    encomendasTableData.value = data.map(encomenda => ({
      id: encomenda.id,
      username: encomenda.username,
      dataExpedicao: new Date(encomenda.data_expedicao).toLocaleString(),
      dataEntrega: encomenda.data_entrega
        ? new Date(encomenda.data_entrega).toLocaleString("pt-PT", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit",
        })
        : "Não definido",
      estado: formatEstado(encomenda.estado)
    }));
  } catch (error) {
    showError(error.message);
  }
};

const cancelarEncomenda = async (id) => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/encomendas/${id}`, {
      method: 'PATCH',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ estado: "Cancelada" })
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    successMessage.value = `Encomenda ${id} cancelada com sucesso!`;
    setTimeout(() => { successMessage.value = ''; }, 3000);

    await fetchEncomendasPendentes();
  } catch (error) {
    showError(error.message);
  }
};

const verAlertasEncomenda = async (id) => {
  try {
    const response = await fetch(`${api}/encomendas/${id}/alertas`);
    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const data = await response.json();
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

    mostrarAlertasModal.value = true;
  } catch (error) {
    showError(error.message);
  }
};

const verTracking = async (id) => {
  try {
    const response = await fetch(`${api}/encomendas/${id}/coordenadas`);
    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const data = await response.json();
    trackingData.value = data;

    mostrarTrackingModal.value = true;

    setTimeout(() => {
      // Reinicializa o mapa e os marcadores ao abrir o modal
      if (map) {
        map.remove(); // Remove o mapa anterior
      }
      const firstCoord = trackingData.value[0].coordenadas.split(',');
      map = L.map('map').setView([firstCoord[0], firstCoord[1]], 13);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      }).addTo(map);

      markers = trackingData.value.map(coord => {
        const [lat, lng] = coord.coordenadas.split(',').map(Number);
        const marker = L.marker([lat, lng]).addTo(map).bindPopup(`Volume ID: ${coord.volumeId}, Produto: ${coord.produtoNome}`);
        return marker;
      });
    }, 0);
  } catch (error) {
    showError(error.message);
  }
};

const goToLocation = (lat, lng) => {
  if (map) {
    map.setView([lat, lng], 15);
    markers.forEach(marker => {
      if (marker.getLatLng().lat === lat && marker.getLatLng().lng === lng) {
        marker.openPopup();
      }
    });
  }
};

const showCancelConfirmModal = ref(false);
const selectedEncomendaId = ref(null);

const handleCancelClick = (id) => {
  selectedEncomendaId.value = id;
  showCancelConfirmModal.value = true;
};

const confirmCancel = async () => {
  await cancelarEncomenda(selectedEncomendaId.value);
  showCancelConfirmModal.value = false;
};


onMounted(async () => {
  loadLeafletCSS();
  await loadLeafletJS();
  fetchEncomendasPendentes();
});
</script>

<template>
  <div>
    <Template :currentPage="currentPage" />

    <div class="flex justify-center mr-24 mt-20">
      <h1>Sistema de Gestão - Encomendas Pendentes</h1>
    </div>

    <!-- Mensagem de Sucesso -->
    <div v-if="successMessage"
      class="fixed top-0 left-0 w-full flex justify-center mt-4 z-50 transition-transform transform-gpu"
      :class="{ 'animate-slide-down': successMessage, 'animate-slide-up': !successMessage }">
      <div class="bg-green-500 text-white py-2 px-4 mr-28 rounded shadow-md">
        {{ successMessage }}
      </div>
    </div>

    <!-- Mensagens de erro estilizadas -->
    <div v-if="errorMessages.length" class="fixed bottom-4 right-4 space-y-2 z-[100]">
      <div v-for="(error, index) in errorMessages" :key="index"
        class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96">
        <h3 class="font-semibold text-lg mb-2">Erro</h3>
        <p>{{ error }}</p>
      </div>
    </div>

    <!-- Tabela para Encomendas Pendentes com botão de ver alertas e tracking -->
    <Table :tableTitles="encomendasTableTitles" :tableData="encomendasTableData" @cancelar="handleCancelClick"
      @verAlertas="verAlertasEncomenda" @tracking="verTracking" />

    <!-- Modal de Confirmação de Cancelamento -->
    <div v-if="showCancelConfirmModal"
      class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white w-1/3 p-6 rounded shadow-lg">
        <h2 class="text-xl font-semibold mb-4">Confirmar Cancelamento</h2>
        <p>Tem certeza que deseja cancelar a encomenda ID {{ selectedEncomendaId }}?</p>
        <div class="mt-4 flex justify-end space-x-2">
          <button @click="showCancelConfirmModal = false"
            class="bg-gray-500 text-white py-1 px-4 rounded hover:bg-gray-700">Cancelar</button>
          <button @click="confirmCancel"
            class="bg-red-500 text-white py-1 px-4 rounded hover:bg-red-700">Confirmar</button>
        </div>
      </div>
    </div>


    <!-- Modal de Alertas -->
    <div v-if="mostrarAlertasModal"
      class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
      <!-- Botão de fechar fora do modal -->
      <button @click="mostrarAlertasModal = false"
        class="absolute top-3 right-[calc(50%-25%)] w-8 h-8 bg-gray-200 text-gray-600 hover:text-gray-900 hover:bg-white rounded-full flex items-center justify-center z-50 shadow">
        <i class="fas fa-times"></i>
      </button>

      <!-- Conteúdo do Modal -->
      <div class="bg-white w-1/2 p-0 rounded shadow-lg relative max-h-[90vh] overflow-y-auto">
        <!-- Cabeçalho fixo preenchido -->
        <div class="sticky top-0 bg-white z-10 p-4 border-b border-gray-300">
          <h2 class="text-xl font-semibold">Alertas da Encomenda</h2>
        </div>

        <!-- Conteúdo rolável -->
        <div class="p-6">
          <div v-if="alertasData.length === 0"
            class="flex flex-col items-center text-gray-600 p-6 border border-gray-300 bg-gray-50 rounded-lg">
            <i class="fas fa-info-circle text-3xl text-blue-500 mb-2"></i>
            <p class="text-lg font-medium">Encomenda sem alertas</p>
          </div>
          <div v-else>
            <div v-for="sensor in alertasData" :key="sensor.id" class="mb-4 p-4 bg-gray-100 rounded-lg border">
              <p class="font-semibold">Sensor ID: {{ sensor.id }} - Tipo: {{ sensor.tipo }}</p>
              <ul class="mt-2 space-y-2">
                <li v-for="alerta in sensor.alertas" :key="alerta.id" class="p-3 bg-yellow-100 rounded-lg border">
                  <p><strong>ID do Alerta:</strong> {{ alerta.id }}</p>
                  <p><strong>Data:</strong> {{ new Date(alerta.timeStamp).toLocaleString() }}</p>
                  <p><strong>Mensagem:</strong> {{ alerta.mensagem }}</p>
                  <p><strong>Valor:</strong> {{ alerta.valor }}</p>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de Tracking -->
    <div v-if="mostrarTrackingModal"
      class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white w-3/4 p-6 rounded shadow-lg relative">
        <button @click="mostrarTrackingModal = false" class="absolute top-2 right-2 text-gray-600 hover:text-gray-900">
          <i class="fas fa-times"></i>
        </button>
        <h2 class="text-xl font-semibold mb-4">Tracking da Encomenda</h2>
        <div class="mb-4 flex items-center space-x-2">
          <h3 class="text-lg font-semibold">Volumes e Produtos:</h3>
          <div class="flex space-x-2">
            <button v-for="(coord, index) in trackingData" :key="index"
              @click="goToLocation(...coord.coordenadas.split(',').map(Number))"
              class="bg-blue-500 text-white px-3 py-2 rounded hover:bg-blue-700 transition">
              {{ coord.produtoNome }}
            </button>
          </div>
        </div>
        <div id="map" class="w-full h-96"></div>
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
