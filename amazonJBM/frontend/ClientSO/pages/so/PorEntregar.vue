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


let map = null;
let markers = [];

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Mensagens de erro
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
    const token = getToken(); // Função para obter o token
    const response = await fetch(`${api}/encomenda/estado/PorEntregar`, {
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
      dataExpedicao: encomenda.data_expedicao
        ? new Date(encomenda.data_expedicao).toLocaleString("pt-PT", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit",
        })
        : "Não definido",
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


const verAlertasEncomenda = async (id) => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/encomenda/${id}/alerta`, {
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

    if (data.sensores && Array.isArray(data.sensores)) {
      alertasData.value = data.sensores.map(sensor => ({
        id: sensor.id,
        tipo: sensor.tipo,

        alertas: sensor.alertas.map(alerta => ({
          id: alerta.id,
          mensagem: alerta.mensagem,
          timeStamp: alerta.timeStamp,
          valor: alerta.valor,
          id_volume: alerta.id_volume,
          id_encomenda: alerta.id_encomenda,
          id_embalagem: alerta.id_embalagem
        }))
      }));
    } else {
      throw new Error("Formato inesperado dos dados de alerta");
    }

    mostrarAlertasModal.value = true;
  } catch (error) {
    showError(error.message);
  }
};


const verTracking = async (id) => {
  try {
    const token = getToken(); // Função para obter o token
    const response = await fetch(`${api}/encomenda/${id}/coordenada`, {
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
      if (map) {
        map.remove(); // Remove o mapa anterior
      }
      const firstCoord = trackingData.value[0].coordenadas.split(':');
      map = L.map('map').setView([firstCoord[0], firstCoord[1]], 13);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      }).addTo(map);

      markers = trackingData.value.map(coord => {
        const [lat, lng] = coord.coordenadas.split(':').map(Number);
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

onMounted(async () => {
  loadLeafletCSS();
  await loadLeafletJS();
  fetchEncomendasPendentes();
});
</script>

<template>
  <Template :currentPage="currentPage" />

  <div class="flex justify-center mt-20 px-4">
    <h1 class="text-center text-lg md:text-xl lg:text-2xl font-bold break-words">
      Sistema de Gestão - Encomendas Por Entregar
    </h1>
  </div>

  <!-- Mensagens de erro estilizadas -->
  <div v-if="errorMessages.length" class="fixed bottom-4 right-4 space-y-2 z-[100]">
    <div v-for="(error, index) in errorMessages" :key="index"
      class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96">
      <h3 class="font-semibold text-lg mb-2">Erro</h3>
      <p>{{ error }}</p>
    </div>
  </div>

  <!-- Tabela para Encomendas "Por Entregar" com botões de ver alertas e tracking -->
  <Table :tableTitles="encomendasTableTitles" :tableData="encomendasTableData" @verAlertas="verAlertasEncomenda"
    @tracking="verTracking" />

  <!-- Modal de Alertas -->
  <div v-if="mostrarAlertasModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <!-- Conteúdo do Modal -->
    <div
      class="bg-white w-full max-w-[90%] sm:max-w-[80%] md:max-w-[70%] lg:max-w-[60%] xl:max-w-[50%] p-0 rounded shadow-lg relative max-h-[90vh] overflow-y-auto">
      <!-- Cabeçalho fixo preenchido -->
      <div class="sticky top-0 bg-white z-10 p-4 border-b border-gray-300 flex justify-between items-center">
        <h2 class="text-lg md:text-xl font-semibold">Alertas da Encomenda</h2>
        <!-- Botão de fechar dentro do cabeçalho -->
        <button @click="mostrarAlertasModal = false"
          class="w-8 h-8 bg-gray-200 text-gray-600 hover:text-gray-900 hover:bg-white rounded-full flex items-center justify-center shadow">
          <i class="fas fa-times"></i>
        </button>
      </div>

      <!-- Conteúdo rolável -->
      <div class="p-4 md:p-6">
        <div v-if="alertasData.length === 0"
          class="flex flex-col items-center text-gray-600 p-6 border border-gray-300 bg-gray-50 rounded-lg">
          <i class="fas fa-info-circle text-3xl text-blue-500 mb-2"></i>
          <p class="text-base md:text-lg font-medium">Encomenda sem alertas</p>
        </div>
        <div v-else>
          <div v-for="sensor in alertasData" :key="sensor.id" class="mb-4 p-4 bg-gray-100 rounded-lg border">
            <p class="font-semibold text-sm md:text-base">Sensor ID: {{ sensor.id }} - Tipo: {{ sensor.tipo }}</p>
            <ul class="mt-2 space-y-2">
              <li v-for="alerta in sensor.alertas" :key="alerta.id"
                class="p-3 bg-yellow-100 rounded-lg border text-sm md:text-base">
                <p><strong>ID do Alerta:</strong> {{ alerta.id }}</p>
                <p>
                  <strong>Volume ID:</strong> {{ alerta.id_volume }},
                  <strong>Encomenda ID:</strong> {{ alerta.id_encomenda }},
                  <strong>Embalagem ID:</strong> {{ alerta.id_embalagem }}
                </p>
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

      <div v-if="trackingData.length > 0">
        <!-- Renderiza botões e mapa se houver dados -->
        <div class="mb-4 flex items-center space-x-2">
          <h3 class="text-lg font-semibold">Volumes e Produtos:</h3>
          <div class="flex space-x-2">
            <button v-for="(coord, index) in trackingData" :key="index"
              @click="goToLocation(...coord.coordenadas.split(':').map(Number))"
              class="bg-blue-500 text-white px-3 py-2 rounded hover:bg-blue-700 transition">
              {{ coord.produtoNome }}
            </button>
          </div>
        </div>
        <div id="map" class="w-full h-96"></div>
      </div>

      <div v-else class="text-center">
        <!-- Exibe mensagem quando não há dados -->
        <p class="text-gray-600 font-semibold text-lg">
          Não existe nenhum sensor de GPS associado a esta encomenda.
        </p>
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
</style>
