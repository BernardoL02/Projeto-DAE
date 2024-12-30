<script setup>
import Template from '../../template.vue';
import Table from '../../table.vue';

import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';

const currentPage = "Encomendas";

const route = useRoute()
const config = useRuntimeConfig()
const api = config.public.API_URL
const username = route.params.username || sessionStorage.getItem('username');

const tableData = ref([]);
const tableTitles = ['ID Encomenda', 'Data de Expedição', 'Data de Entrega', 'Estado'];
const mostrarAlertasModal = ref(false);
const alertasData = ref([]);
const estado = ref("Todas");

const mostrarTrackingModal = ref(false);
const trackingData = ref([]);
let map = null;
let markers = [];

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

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

const formateEstado = (estado) => {

  if (estado === "EmProcessamento") {
    return estado.replace("EmProcessamento", "Em Processamento");
  }
  else if (estado === "PorEntregar") {
    return estado.replace("PorEntregar", "Por Entregar");
  }

  return estado;
};


// Função para buscar encomendas
const fetchEncomendas = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/encomendas`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const data = await response.json();
    tableData.value = data.map(order => [
      order.id,
      new Date(order.data_expedicao).toLocaleString(),
      order.data_entrega ? new Date(order.data_entrega).toLocaleString() : "Não Entregue",
      formateEstado(order.estado),
    ]);
  } catch (error) {
    showError(error.message);
  }
};

// Função para buscar encomendas com filtros
const fetchEncomendasByEstado = async () => {
  const token = getToken();
  try {
    const url = estado.value === "Todas"
      ? `${api}/encomendas`
      : `${api}/encomendas/estado/${estado.value}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const data = await response.json();
    tableData.value = data.map(order => [
      order.id,
      new Date(order.data_expedicao).toLocaleString(),
      order.data_entrega ? new Date(order.data_entrega).toLocaleString() : "Não Entregue",
      formateEstado(order.estado),
    ]);
  } catch (error) {
    showError(error.message);
  }
};


const verAlertasEncomenda = async (id) => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/encomendas/${id}/alertas`, {
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
    const token = sessionStorage.getItem('token');
    const response = await fetch(`${api}/encomendas/${id}/coordenadas`, {
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
    trackingData.value = data;

    mostrarTrackingModal.value = true;

    setTimeout(() => {
      // Reinicializa o mapa e os marcadores ao abrir o modal
      if (map) {
        map.remove();
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


watch(estado, fetchEncomendasByEstado);
onMounted(async () => {
  loadLeafletCSS();
  await loadLeafletJS();
  fetchEncomendas();
});
</script>

<template>

  <Template :username="username" :currentPage="currentPage"></Template>

  <!-- Mensagens de erro estilizadas -->
  <div v-if="errorMessages.length" class="fixed bottom-4 right-4 space-y-2 z-[100]">
    <div v-for="(error, index) in errorMessages" :key="index"
      class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96">
      <h3 class="font-semibold text-lg mb-2">Erro</h3>
      <p>{{ error }}</p>
    </div>
  </div>

  <div class="flex justify-between items-center ml-28 mr-28 mt-20">
    <div>
      <h1 class="text-xl font-semibold text-gray-800">Bem-vindo, {{ username }}</h1>
      <p class="mt-1 text-lg text-gray-700">Encomendas</p>
    </div>

    <!-- Filtro de Encomendas -->
    <div class="w-64">
      <select v-model="estado" name="Encomendas" id="encomendas"
        class="block w-full p-2 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-SecundaryColor focus:border-SecundaryColor text-gray-700">
        <option value="Todas" class="text-gray-700">Todas</option>
        <option value="Entregue" class="text-gray-700">Entregue</option>
        <option value="EmProcessamento" class="text-gray-700">Em Processamento</option>
        <option value="PorEntregar" class="text-gray-700">Por Entregar</option>
        <option value="Cancelada" class="text-gray-700">Cancelada</option>
      </select>
    </div>
  </div>

  <Table @verAlertas="verAlertasEncomenda" @tracking="verTracking" :tableTitles="tableTitles" :tableData="tableData"
    :mostrarOperacoes="true" />

  <!-- Modal de Alertas -->
  <div v-if="mostrarAlertasModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
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
      <div v-if="trackingData.length > 0" class="mb-4 flex flex-col space-y-4">
        <h3 class="text-lg font-semibold">Volumes e Produtos:</h3>
        <div class="flex space-x-2">
          <button v-for="(coord, index) in trackingData" :key="index"
            @click="goToLocation(...coord.coordenadas.split(',').map(Number))"
            class="bg-blue-500 text-white px-3 py-2 rounded hover:bg-blue-700 transition">
            {{ coord.produtoNome }}
          </button>
        </div>
        <div id="map" class="w-full h-96"></div>
      </div>
      <div v-else class="text-center">
        <p class="text-gray-600 font-semibold text-lg">Não existe nenhum sensor de GPS associado a esta encomenda.</p>
      </div>
    </div>
  </div>


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
