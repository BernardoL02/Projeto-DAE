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
const currentPage = 'Por Entregar';

const mostrarAlertasModal = ref(false); // Controla a exibição do modal de alertas
const alertasData = ref([]); // Dados dos alertas para o modal
const mostrarTrackingModal = ref(false); // Controla a exibição do modal de tracking
const trackingData = ref([]); // Dados de coordenadas para o mapa
const errorMessages = ref([]); // Mensagens de erro

let map = null;
let markers = [];

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

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
    const token = getToken(); // Função para obter o token
    const response = await fetch(`${api}/encomendas/estado/PorEntregar`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) throw new Error("Erro ao buscar encomendas 'Por Entregar'");

    const data = await response.json();
    encomendasTableData.value = data.map(encomenda => ({
      id: encomenda.id,
      username: encomenda.username,
      dataExpedicao: new Date(encomenda.data_expedicao).toLocaleString(),
      dataEntrega: new Date(encomenda.data_entrega).toLocaleString(),
      estado: formatEstado(encomenda.estado)
    }));
  } catch (error) {
    errorMessages.value.push(`Erro ao carregar encomendas 'Por Entregar': ${error.message}`);
    console.error("Erro ao carregar encomendas 'Por Entregar':", error);
  }
};


const verAlertasEncomenda = async (id) => {
  try {
    const token = getToken(); // Função para obter o token
    const response = await fetch(`${api}/encomendas/${id}/alertas`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) throw new Error("Erro ao buscar alertas da encomenda");

    const data = await response.json();
    console.log("Dados de alerta recebidos:", data);

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
    console.error(`Erro ao buscar alertas da encomenda ${id}:`, error);
  }
};


const verTracking = async (id) => {
  try {
    const token = getToken(); // Função para obter o token
    const response = await fetch(`${api}/encomendas/${id}/coordenadas`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) throw new Error("Erro ao buscar coordenadas da encomenda");

    const data = await response.json();
    trackingData.value = data;

    mostrarTrackingModal.value = true;

    setTimeout(() => {
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
    errorMessages.value.push(`Erro ao buscar coordenadas da encomenda ${id}: ${error.message}`);
    console.error(`Erro ao buscar coordenadas da encomenda ${id}:`, error);
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

onMounted(async () => {
  loadLeafletCSS();
  await loadLeafletJS();
  fetchEncomendasPendentes();
});
</script>

<template>
  <Template :currentPage="currentPage" />

  <div class="flex justify-center mr-24 mt-20">
    <h1>Sistema de Gestão - Encomendas Por Entregar</h1>
  </div>

  <div v-if="errorMessages.length" class="text-red-500 text-center mt-4">
    <p v-for="(error, index) in errorMessages" :key="index">{{ error }}</p>
  </div>

  <!-- Tabela para Encomendas "Por Entregar" com botões de ver alertas e tracking -->
  <Table :tableTitles="encomendasTableTitles" :tableData="encomendasTableData" @verAlertas="verAlertasEncomenda"
    @tracking="verTracking" />

  <!-- Modal de Alertas -->
  <div v-if="mostrarAlertasModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white w-1/2 p-6 rounded shadow-lg relative">
      <button @click="mostrarAlertasModal = false" class="absolute top-2 right-2 text-gray-600 hover:text-gray-900">
        <i class="fas fa-times"></i>
      </button>
      <h2 class="text-xl font-semibold mb-4">Alertas da Encomenda</h2>
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
