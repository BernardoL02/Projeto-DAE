<script setup>
import Template from '../../template.vue';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRuntimeConfig } from '#app';

const route = useRoute();
const config = useRuntimeConfig();
const api = config.public.API_URL;

const encomendaId = route.params.id;

const encomendaData = ref(null);
const volumesData = ref([]);
const alertasData = ref({});
const errorMessages = ref([]);

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Função para exibir a mensagem de erro como um alerta estilizado
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
      data_expedicao: new Date(data.data_expedicao).toLocaleString(),
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
        tipoEmbalagem: embalagem.tipoEmbalagem,
        mostrarSensores: false, // Não exibir detalhes dos sensores inicialmente
        sensores: embalagem.sensores.map((sensor) => ({
          id: sensor.id,
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

// Função para alternar a exibição de sensores de um volume específico
const toggleDetalhes = (embalagem) => {
  embalagem.mostrarDetalhes = !embalagem.mostrarDetalhes;
};

// Função para buscar e exibir alertas de um sensor específico
const fetchAlertas = async (sensor) => {
  try {
    if (alertasData.value[sensor.id]) {
      sensor.mostrarAlertas = !sensor.mostrarAlertas;
    } else {
      const token = getToken();
      const response = await fetch(`${api}/sensor/${sensor.id}/alertas`, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });

      if (!response.ok) throw new Error(`Erro ao buscar alertas do sensor ${sensor.id}`);
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
    console.error(`Erro ao buscar alertas do sensor ${sensor.id}:`, error);
  }
};


onMounted(fetchEncomendaDetalhes);

</script>

<template>
  <Template />

  <!-- Mensagens de erro estilizadas -->
  <div v-if="errorMessages.length" class="fixed bottom-4 right-4 space-y-2 z-[100]">
    <div v-for="(error, index) in errorMessages" :key="index"
      class="bg-red-500 text-white py-4 px-6 rounded shadow-lg w-96">
      <h3 class="font-semibold text-lg mb-2">Erro</h3>
      <p>{{ error }}</p>
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
          <h3 class="font-semibold text-lg text-gray-800">Volume ID: {{ volume.id }}</h3>
          <button @click="toggleVolume(volume)"
            class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-700 transition">
            {{ volume.mostrarDetalhes ? 'Esconder Detalhes do Volume' : 'Mostrar Detalhes do Volume' }}
          </button>
        </div>

        <div v-if="volume.mostrarDetalhes" class="mt-4">
          <div v-for="embalagem in volume.embalagens" :key="embalagem.id" class="mt-4 p-4 bg-white rounded-lg border">
            <div class="flex justify-between items-center">
              <div>
                <p><strong>Embalagem ID:</strong> {{ embalagem.id }}</p>
                <p><strong>Tipo Embalagem:</strong> {{ embalagem.tipoEmbalagem }}</p>
                <p><strong>Produto:</strong> {{ embalagem.produtoName }}</p>
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
                  <p><strong>Tipo:</strong> {{ sensor.tipo }}</p>
                  <p><strong>Valor:</strong> {{ sensor.valor }}</p>
                  <p><strong>Bateria:</strong> {{ sensor.bateria }}%</p>
                  <p><strong>Estado:</strong> {{ sensor.estado }}</p>
                  <p><strong>Última Leitura:</strong> {{ sensor.ultimaLeitura }}</p>
                  <button @click="fetchAlertas(sensor)"
                    class="bg-yellow-500 text-white px-3 py-1 rounded mt-2 hover:bg-yellow-700 transition">
                    {{ sensor.mostrarAlertas ? 'Esconder Alertas' : 'Ver Alertas' }}
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
