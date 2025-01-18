<script setup>
import Template from '../../../template.vue';
import Table from '../../../table.vue';

import { useRoute } from 'vue-router';

const currentPage = "Encomenda";

const route = useRoute()
const config = useRuntimeConfig()
const username = route.params.username
const encomendaId = route.params.id

const api = config.public.API_URL

const encomendaData = ref(null);
const volumesData = ref([]);
const alertasData = ref({});
const historicoData = ref([]);
const showHistoricoModal = ref(false);

const errorMessages = ref([]);
const showError = (message) => {
  errorMessages.value.push(message);

  setTimeout(() => {
    errorMessages.value.shift();
  }, 5000);
};

// Função para alternar a exibição de detalhes do volume
const toggleVolume = (volume) => {
  volume.mostrarDetalhes = !volume.mostrarDetalhes;
};

// Função para alternar a exibição de sensores da embalagem
const toggleEmbalagem = (embalagem) => {
  embalagem.mostrarSensores = !embalagem.mostrarSensores;
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

const getToken = () => sessionStorage.getItem('token');


// Função para buscar detalhes da encomenda e volumes associados
const fetchEncomendaDetalhes = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/encomenda/${encomendaId}`, {
      method: 'GET',
      headers: {
        Accept: 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    const data = await response.json();
    encomendaData.value = {
      id: data.id,
      username: data.username,
      data_expedicao: data.data_expedicao
        ? new Date(data.data_expedicao).toLocaleString('pt-PT')
        : 'Não definido',
      data_entrega: data.data_entrega
        ? new Date(data.data_entrega).toLocaleString('pt-PT')
        : 'Não definido',
      estado: formateEstado(data.estado),
    };

    volumesData.value = data.volumes.map((volume) => ({
      id: volume.id,
      entregue: volume.entregue,
      mostrarDetalhes: true, // Exibir detalhes do volume automaticamente
      embalagens: volume.embalagems.map((embalagem) => ({
        id: embalagem.id,
        produtoId: embalagem.produto.id,
        produtoName: embalagem.produto.nome,
        quantidade: embalagem.quantidade,
        idTipoEmbalagem: embalagem.idTipoEmbalagem,
        tipoEmbalagem: embalagem.tipoEmbalagem,
        mostrarSensores: false, // Não exibir detalhes dos sensores inicialmente
        sensores: embalagem.sensores.map((sensor) => ({
          id: sensor.id,
          tipoId: sensor.tipoId,
          tipo: sensor.tipoNome,
          valor: sensor.valor,
          bateria: sensor.bateria,
          estado: sensor.estado,
          ultimaLeitura: new Date(sensor.timeStamp).toLocaleString('pt-PT'),
        })),
      })),
    }));
  } catch (error) {
    showError(error.message);
  }
};

// Função para buscar e exibir alertas de um sensor específico
const fetchAlertas = async (sensor) => {
  try {
    const token = getToken();
    if (alertasData.value[sensor.id]) {
      sensor.mostrarAlertas = !sensor.mostrarAlertas;
    } else {
      const response = await fetch(`${api}/sensor/${sensor.id}/alerta`, {
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

      const alertas = await response.json();
      alertasData.value[sensor.id] = alertas.map(alerta => ({
        id: alerta.id,
        data: new Date(alerta.data).toLocaleString(),
        mensagem: alerta.mensagem,
        valor: alerta.valor
      }));
      sensor.mostrarAlertas = true;

      console.log(alertas);
    }
  } catch (error) {
    showError(error.message);
  }
};

// Função para buscar histórico de um sensor
const fetchHistoricoSensor = async (sensorId) => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/sensor/${sensorId}/leitura`, {
      method: 'GET',
      headers: {
        Accept: 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    historicoData.value = await response.json();
    showHistoricoModal.value = true; // Exibir o modal com o histórico
  } catch (error) {
    showError(error.message);
  }
};

onMounted(fetchEncomendaDetalhes);
</script>

<template>
  <Template :username="username" :currentPage="currentPage" />

  <!-- Mensagens de erro estilizadas -->
  <div v-if="errorMessages.length" class="fixed bottom-4 right-4 space-y-2 z-[100]">
    <div v-for="(error, index) in errorMessages" :key="index"
      class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96">
      <h3 class="font-semibold text-lg mb-2">Erro</h3>
      <p>{{ error }}</p>
    </div>
  </div>

  <!-- Modal para Histórico -->
  <div v-if="showHistoricoModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
    <div class="bg-white p-6 rounded-lg shadow-lg w-3/4 max-w-4xl relative">
      <!-- Título e botão de fechar -->
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-lg font-bold">Histórico de Leituras do Sensor</h2>
        <button @click="showHistoricoModal = false" class="absolute top-2 right-2 bg-gray-200 text-gray-600 hover:text-gray-900 hover:bg-white rounded-full w-8
          h-8 flex items-center justify-center">
          <i class="fas fa-times"></i>
        </button>
      </div>

      <!-- Conteúdo com rolagem -->
      <div class="overflow-y-auto max-h-96">
        <ul>
          <li v-for="leitura in historicoData" :key="leitura.timeStamp" class="p-4 border-b">
            <p><strong>Valor:</strong> {{ leitura.valor }}</p>
            <p><strong>Bateria:</strong> {{ leitura.bateria }}%</p>
            <p><strong>Timestamp:</strong> {{ new Date(leitura.timeStamp).toLocaleString('pt-PT') }}</p>
          </li>
        </ul>
      </div>
    </div>
  </div>

  <div v-if="encomendaData"
    class="flex flex-col justify-center mx-auto mt-10 p-6 mb-10 bg-white shadow-md rounded-lg border border-gray-300 w-full max-w-5xl">
    <div class="mb-8">
      <h1 class="text-center text-2xl font-semibold mb-4">Detalhes da Encomenda</h1>
      <p class="text-gray-700"><strong>ID:</strong> {{ encomendaData.id }}</p>
      <p class="text-gray-700"><strong>Utilizador:</strong> {{ encomendaData.username }}</p>
      <p class="text-gray-700"><strong>Data de Expedição:</strong> {{ encomendaData.data_expedicao }}</p>
      <p class="text-gray-700"><strong>Data de Entrega:</strong> {{ encomendaData.data_entrega }}</p>
      <p class="text-gray-700"><strong>Estado:</strong> {{ encomendaData.estado }}</p>
    </div>

    <div>
      <h2 class="text-xl font-semibold mb-4">Volumes</h2>
      <div v-for="volume in volumesData" :key="volume.id"
        class="mb-6 p-4 bg-gray-100 rounded-lg border border-gray-300">
        <div class="flex justify-between items-center">
          <div>
            <h3 class="font-semibold text-lg text-gray-800">
              Volume ID: {{ volume.id }}
            </h3>
            <p v-if="encomendaData.estado !== 'Em Processamento'" class="text-sm text-gray-600">
              Estado do Volume: {{ volume.entregue ? 'Entregue' : 'Não Entregue' }}
            </p>
          </div>
          <div class="flex space-x-4">
            <button @click="toggleVolume(volume)"
              class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-700 transition">
              {{ volume.mostrarDetalhes ? 'Esconder Detalhes do Volume' : 'Mostrar Detalhes do Volume' }}
            </button>
          </div>
        </div>

        <div v-if="volume.mostrarDetalhes" class="mt-4">
          <div v-for="embalagem in volume.embalagens" :key="embalagem.id" class="mt-4 p-4 bg-white rounded-lg border">
            <div class="flex justify-between items-center">
              <div>
                <p><strong>Embalagem ID:</strong> {{ embalagem.id }}</p>
                <p><strong>Tipo Embalagem:</strong> <strong>(id:</strong> {{ embalagem.idTipoEmbalagem
                  }}<strong>)</strong> - {{ embalagem.tipoEmbalagem }} </p>
                <p><strong>Produto:</strong> <strong>(id:</strong> {{ embalagem.produtoId }}<strong>)</strong> - {{
                  embalagem.produtoName }} </p>
                <p><strong>Quantidade:</strong> {{ embalagem.quantidade }}</p>
              </div>
              <button @click="toggleEmbalagem(embalagem)"
                class="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-700 transition">
                {{ embalagem.mostrarSensores ? 'Esconder Sensores' : 'Mostrar Sensores' }}
              </button>
            </div>

            <div v-if="embalagem.mostrarSensores" class="mt-4">
              <h4 class="font-semibold text-md text-gray-700">Sensores Associados:</h4>
              <ul v-if="embalagem.sensores && embalagem.sensores.length > 0">
                <li v-for="sensor in embalagem.sensores" :key="sensor.id" class="p-2 bg-gray-50 my-2 rounded shadow">
                  <p><strong>ID do Sensor:</strong> {{ sensor.id }}</p>
                  <p><strong>Tipo:</strong> <strong>(id:</strong> {{ sensor.tipoId
                    }}<strong>)</strong> - {{ sensor.tipo }}</p>
                  <p><strong>Valor:</strong> {{ sensor.valor }}</p>
                  <p><strong>Bateria:</strong> {{ sensor.bateria }}%</p>
                  <p><strong>Estado:</strong> {{ sensor.estado }}</p>
                  <p><strong>Última Leitura:</strong> {{ sensor.ultimaLeitura }}</p>
                  <button v-if="encomendaData.estado !== 'Em Processamento'" @click="fetchAlertas(sensor)"
                    class="bg-yellow-500 text-white px-3 py-1 rounded mt-2 hover:bg-yellow-700 transition mr-2">
                    {{ sensor.mostrarAlertas ? 'Esconder Alertas' : 'Ver Alertas' }}
                  </button>
                  <button @click="fetchHistoricoSensor(sensor.id)"
                    class="bg-yellow-500 text-white px-3 py-1 rounded mt-2 hover:bg-yellow-700 transition">
                    Ver Histórico
                  </button>

                  <div v-if="sensor.mostrarAlertas" class="mt-2 p-2 bg-yellow-100 rounded shadow">
                    <h5 class="font-semibold text-sm text-yellow-800 mb-2">Alertas:</h5>

                    <ul v-if="alertasData[sensor.id] && alertasData[sensor.id].length > 0">
                      <li v-for="alerta in alertasData[sensor.id]" :key="alerta.id"
                        class="mb-2 p-2 border-b border-yellow-300 last:border-none">
                        <p><strong>ID:</strong> {{ alerta.id }}</p>
                        <p><strong>Data:</strong> {{ alerta.data }}</p>
                        <p><strong>Mensagem:</strong> {{ alerta.mensagem }}</p>
                        <p><strong>Valor:</strong> {{ alerta.valor }}</p>
                      </li>
                    </ul>

                    <p v-else class="text-gray-600 italic">Neste momento não há alertas para este sensor.</p>
                  </div>
                </li>
              </ul>

              <p v-else class="text-gray-600 italic">Nenhum sensor associado a esta embalagem.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div v-else class="text-center mt-20">
    <p>Carregando detalhes da encomenda...</p>
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
