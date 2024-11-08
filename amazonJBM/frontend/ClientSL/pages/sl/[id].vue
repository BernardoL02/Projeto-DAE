<script setup>
import Template from '../template.vue';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRuntimeConfig } from '#app';

const route = useRoute();
const config = useRuntimeConfig();
const api = config.public.API_URL;

const encomendaId = route.params.id;

const encomendaData = ref(null);
const volumesData = ref([]);
const tiposSensores = ref([]);
const produtos = ref([]);
const mostrarAssociarSensorModal = ref(false);
const mostrarAdicionarProdutoModal = ref(false);
const volumeSelecionado = ref(null);
const produtoSelecionado = ref(null);
const tipoSelecionado = ref(null);
const quantidade = ref(1);
const valMax = ref(null);
const valMin = ref(null);
const successMessage = ref('');
const errorMessage = ref('');

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Função para formatar o estado da encomenda
const formateEstado = (estado) => {
  return estado === "EmProcessamento" ? "Em Processamento" : estado === "PorEntregar" ? "Por Entregar" : estado || "Indefinido";
};

// Função para buscar detalhes da encomenda e volumes associados
const fetchEncomendaDetalhes = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/encomendas/${encomendaId}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    if (!response.ok) throw new Error("Erro ao buscar detalhes da encomenda");

    const data = await response.json();
    encomendaData.value = {
      id: data.id,
      username: data.username,
      data_expedicao: data.data_expedicao,
      data_entrega: data.data_entrega,
      estado: formateEstado(data.estado),
    };

    volumesData.value = data.volumes.map(volume => ({
      id: volume.id,
      nome_produto: volume.nome_produto,
      quantidade: volume.quantidade,
      mostrarSensores: false,
      sensores: volume.sensores.map(sensor => ({
        id: sensor.id,
        tipo: sensor.tipoNome,
        valor: sensor.valor,
        bateria: sensor.bateria,
        estado: sensor.estado,
        ultimaLeitura: new Date(sensor.timeStamp).toLocaleString(),
      }))
    }));
  } catch (error) {
    console.error("Erro ao carregar detalhes da encomenda:", error);
  }
};

// Função para buscar tipos de sensores
const fetchTiposSensores = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/sensor/tipos`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    if (!response.ok) throw new Error("Erro ao buscar tipos de sensores");

    tiposSensores.value = await response.json();
  } catch (error) {
    console.error("Erro ao carregar tipos de sensores:", error);
  }
};

// Função para buscar detalhes do volume
const fetchVolumeDetails = async (volumeId) => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/volume/${volumeId}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    if (!response.ok) throw new Error("Erro ao buscar detalhes do volume");

    const volumeData = await response.json();
    const volumeIndex = volumesData.value.findIndex(volume => volume.id === volumeId);
    if (volumeIndex !== -1) {
      volumesData.value[volumeIndex] = {
        ...volumeData,
        mostrarSensores: true,
        sensores: volumeData.sensores.map(sensor => ({
          id: sensor.id,
          tipo: sensor.tipoNome,
          valor: sensor.valor,
          bateria: sensor.bateria,
          estado: sensor.estado,
          ultimaLeitura: new Date(sensor.timeStamp).toLocaleString(),
        }))
      };
    }
  } catch (error) {
    console.error("Erro ao atualizar detalhes do volume:", error);
  }
};

// Função para alternar a exibição de sensores de um volume específico
const toggleSensores = (volume) => {
  volume.mostrarSensores = !volume.mostrarSensores;
};

// Função para abrir o modal de associação de sensor
const handleAssociarSensor = (volume) => {
  volumeSelecionado.value = volume;
  tipoSelecionado.value = null;
  valMax.value = null;
  valMin.value = null;
  mostrarAssociarSensorModal.value = true;
};

// Função para gerar um valor aleatório dentro de um intervalo
const getRandomValueInRange = (min, max) => {
  return Math.floor(Math.random() * (max - min + 1)) + min;
};

// Função para associar um sensor a um volume
const associarSensor = async () => {
  if (!tipoSelecionado.value) {
    errorMessage.value = "Selecione um tipo de sensor.";
    setTimeout(() => errorMessage.value = '', 3000);
    return;
  }

  try {
    let valor = null;
    if (tipoSelecionado.value.id === 4) {
      // Escolher aleatoriamente entre dois tipos de coordenadas
      const coordenadas = ["39.73440231964457, -8.821080620077632", "39.74906316836962, -8.81280859823362"];
      valor = coordenadas[Math.floor(Math.random() * coordenadas.length)];
    } else {
      if (valMax.value === null || valMin.value === null) {
        errorMessage.value = "Digite os valores máximo e mínimo.";
        setTimeout(() => errorMessage.value = '', 3000);
        return;
      }
      valor = getRandomValueInRange(valMin.value, valMax.value);
    }

    const sensorData = {
      valor,
      tipoId: tipoSelecionado.value.id,
      tipoNome: tipoSelecionado.value.tipo,
      estado: "ativo",
      bateria: 100,
      ...(tipoSelecionado.value.id !== 4 && { valMax: valMax.value, valMin: valMin.value })
    };

    const token = getToken();
    const response = await fetch(`${api}/volume/${volumeSelecionado.value.id}/sensor`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(sensorData)
    });

    if (!response.ok) throw new Error("Erro ao associar sensor");

    successMessage.value = "Sensor associado com sucesso!";
    setTimeout(() => successMessage.value = '', 3000);

    mostrarAssociarSensorModal.value = false;

    // Atualiza os sensores do volume após a associação
    await fetchVolumeDetails(volumeSelecionado.value.id);

  } catch (error) {
    errorMessage.value = "Erro ao associar sensor.";
    setTimeout(() => errorMessage.value = '', 3000);
  }
};

// Função para adicionar um volume à encomenda
const adicionarProduto = async () => {
  if (!produtoSelecionado.value || quantidade.value <= 0) {
    errorMessage.value = "Selecione um produto e insira uma quantidade válida.";
    setTimeout(() => errorMessage.value = '', 3000);
    return;
  }

  try {
    const volumeData = {
      id_produto: produtoSelecionado.value.id,
      quantidade: quantidade.value
    };

    const token = getToken();
    const response = await fetch(`${api}/encomendas/${encomendaId}/volume`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(volumeData)
    });

    if (!response.ok) throw new Error("Erro ao adicionar produto");

    successMessage.value = "Produto adicionado com sucesso!";
    setTimeout(() => successMessage.value = '', 3000);

    mostrarAdicionarProdutoModal.value = false;
    fetchEncomendaDetalhes(); // Atualiza os detalhes da encomenda
  } catch (error) {
    errorMessage.value = "Erro ao adicionar produto.";
    setTimeout(() => errorMessage.value = '', 3000);
  }
};

// Função para buscar produtos disponíveis
const fetchProdutos = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/produtos`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    if (!response.ok) throw new Error("Erro ao buscar produtos");

    produtos.value = await response.json();
  } catch (error) {
    console.error("Erro ao carregar produtos:", error);
  }
};




onMounted(() => {
  fetchEncomendaDetalhes();
  fetchTiposSensores();
  fetchProdutos();
});
</script>

<template>
  <Template />

  <div v-if="successMessage" class="fixed top-0 left-0 w-full flex justify-center mt-4 z-50">
    <div class="bg-green-500 text-white py-2 px-4 rounded shadow-md">
      {{ successMessage }}
    </div>
  </div>
  
  <div v-if="errorMessage" class="fixed top-0 left-0 w-full flex justify-center mt-4 z-50">
    <div class="bg-red-500 text-white py-2 px-4 rounded shadow-md">
      {{ errorMessage }}
    </div>
  </div>

  <div v-if="encomendaData" class="flex flex-col justify-center mx-auto mt-10 p-6 mb-10 bg-white shadow-md rounded-lg border border-gray-300 w-full max-w-5xl">
    <div class="mb-8">
      <h1 class="text-center text-2xl font-semibold mb-4">Detalhes da Encomenda</h1>
      <p class="text-gray-700"><strong>ID:</strong> {{ encomendaData.id }}</p>
      <p class="text-gray-700"><strong>Utilizador:</strong> {{ encomendaData.username }}</p>
      <p class="text-gray-700"><strong>Data de Expedição:</strong> {{ new Date(encomendaData.data_expedicao).toLocaleString() }}</p>
      <p class="text-gray-700"><strong>Data de Entrega:</strong> {{ new Date(encomendaData.data_entrega).toLocaleString() }}</p>
      <p class="text-gray-700"><strong>Estado:</strong> {{ encomendaData.estado }}</p>
    </div>

    <div>
      <h2 class="text-xl font-semibold mb-4">Volumes</h2>
      <button 
        v-if="encomendaData.estado === 'Em Processamento'"
        @click="mostrarAdicionarProdutoModal = true" 
        class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition mb-4"
      >
        Adicionar Produto
      </button>
      <div v-for="volume in volumesData" :key="volume.id" class="mb-6 p-4 bg-gray-100 rounded-lg border border-gray-300">
        <div class="flex justify-between items-center">
          <div>
            <h3 class="font-semibold text-lg text-gray-800">Volume ID: {{ volume.id }} - {{ volume.nome_produto }}</h3>
            <p class="text-gray-600">Quantidade: {{ volume.quantidade }}</p>
          </div>
          <div class="flex space-x-2">
            <button 
              @click="toggleSensores(volume)" 
              class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-700 transition"
            >
              {{ volume.mostrarSensores ? 'Esconder Detalhes' : 'Mostrar Detalhes' }}
            </button>
            <button 
              v-if="encomendaData.estado === 'Em Processamento'"
              @click="handleAssociarSensor(volume)" 
              class="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-700 transition"
            >
              Associar Sensor
            </button>
          </div>
        </div>

        <div v-if="volume.mostrarSensores" class="mt-4">
          <h4 class="font-semibold text-md text-gray-700">Sensores Associados:</h4>
          <ul>
            <li v-for="sensor in volume.sensores" :key="sensor.id" class="p-2 bg-white my-2 rounded shadow">
              <p><strong>ID do Sensor:</strong> {{ sensor.id }}</p>
              <p><strong>Tipo:</strong> {{ sensor.tipo }}</p>
              <p><strong>Valor:</strong> {{ sensor.valor }}</p>
              <p><strong>Bateria:</strong> {{ sensor.bateria }}%</p>
              <p><strong>Estado:</strong> {{ sensor.estado }}</p>
              <p><strong>Última Leitura:</strong> {{ sensor.ultimaLeitura }}</p>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>

  <div v-else class="text-center mt-20">
    <p>Carregando detalhes da encomenda...</p>
  </div>

  <!-- Modal de Associar Sensor -->
  <div v-if="mostrarAssociarSensorModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white w-1/3 p-6 rounded shadow-lg">
      <h2 class="text-xl font-semibold mb-4">Associar Sensor</h2>
      <p>Escolha o tipo de sensor para associar ao Volume ID {{ volumeSelecionado.id }}:</p>
      <select v-model="tipoSelecionado" class="w-full p-2 border border-gray-300 rounded mb-4">
        <option v-for="tipo in tiposSensores" :key="tipo.id" :value="tipo">{{ tipo.tipo }}</option>
      </select>
      <div v-if="tipoSelecionado && tipoSelecionado.id !== 4" class="mb-4">
        <label class="block text-gray-700 font-semibold mb-1">Valor Máximo:</label>
        <input v-model="valMax" type="number" class="w-full p-2 border border-gray-300 rounded mb-2" placeholder="Digite o valor máximo">
        <label class="block text-gray-700 font-semibold mb-1">Valor Mínimo:</label>
        <input v-model="valMin" type="number" class="w-full p-2 border border-gray-300 rounded" placeholder="Digite o valor mínimo">
      </div>
      <button @click="associarSensor" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700">Associar</button>
      <button @click="mostrarAssociarSensorModal = false" class="ml-2 bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-700">Cancelar</button>
    </div>
  </div>

  <!-- Modal de Adicionar Produto -->
  <div v-if="mostrarAdicionarProdutoModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white w-1/3 p-6 rounded shadow-lg">
      <h2 class="text-xl font-semibold mb-4">Adicionar Produto</h2>
      <p>Escolha um produto para adicionar à encomenda:</p>
      <select v-model="produtoSelecionado" class="w-full p-2 border border-gray-300 rounded mb-4 max-h-52 overflow-y-auto mt-2 mb-2">
        <option v-for="produto in produtos" :key="produto.id" :value="produto">{{ produto.nome }}</option>
      </select>
      <label class="block text-gray-700 font-semibold mb-1">Quantidade:</label>
      <input v-model="quantidade" type="number" min="1" class="w-full p-2 border border-gray-300 rounded mb-4" placeholder="Digite a quantidade">
      <button @click="adicionarProduto" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700">Adicionar</button>
      <button @click="mostrarAdicionarProdutoModal = false" class="ml-2 bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-700">Cancelar</button>
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
