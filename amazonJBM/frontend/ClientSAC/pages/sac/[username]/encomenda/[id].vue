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
    const token = getToken(); // Função para obter o token
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
      embalagens: volume.embalagems.map(embalagem => ({
        id: embalagem.id,
        produto: {
          id: embalagem.produto.id,
          quantidade: embalagem.produto.quantidade_de_produtos_comprados
        },
        quantidade: embalagem.quantidade,
        sensores: embalagem.sensores.map(sensor => ({
          id: sensor.id,
          tipo: sensor.tipoNome,
          valor: sensor.valor,
          bateria: sensor.bateria,
          estado: sensor.estado,
          ultimaLeitura: new Date(sensor.timeStamp).toLocaleString(),
          mostrarAlertas: false
        }))
      })),
      mostrarSensores: false
    }));
  } catch (error) {
    console.error("Erro ao carregar detalhes da encomenda:", error);
  }
};

// Função para alternar a exibição de sensores de um volume específico
const toggleDetalhes = (embalagem) => {
  embalagem.mostrarDetalhes = !embalagem.mostrarDetalhes;
};

// Função para buscar e exibir alertas de um sensor específico
const fetchAlertas = async (sensor) => {
  try {
    const token = getToken();
    if (alertasData.value[sensor.id]) {
      sensor.mostrarAlertas = !sensor.mostrarAlertas;
    } else {
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

  <div v-if="encomendaData"
    class="flex flex-col justify-center mx-auto mt-10 p-6 mb-10 bg-white shadow-md rounded-lg border border-gray-300 w-full max-w-5xl">
    <div class="mb-8">
      <h1 class="text-center text-2xl font-semibold mb-4">Detalhes da Encomenda</h1>
      <p class="text-gray-700"><strong>ID:</strong> {{ encomendaData.id }}</p>
      <p class="text-gray-700"><strong>Utilizador:</strong> {{ encomendaData.username }}</p>
      <p class="text-gray-700"><strong>Data de Expedição:</strong> {{ new
        Date(encomendaData.data_expedicao).toLocaleString() }}</p>
      <p class="text-gray-700"><strong>Data de Entrega:</strong> {{ encomendaData.data_entrega ? new
        Date(encomendaData.data_entrega).toLocaleString() : 'Não entregue' }}</p>
      <p class="text-gray-700"><strong>Estado:</strong> {{ encomendaData.estado }}</p>
    </div>

    <div>
      <h2 class="text-xl font-semibold mb-4">Volumes</h2>
      <div v-for="volume in volumesData" :key="volume.id"
        class="mb-6 p-4 bg-gray-100 rounded-lg border border-gray-300">
        <h3 class="font-semibold text-lg text-gray-800">Volume ID: {{ volume.id }}</h3>

        <h4 class="font-semibold text-md text-gray-700 mt-4">Embalagens:</h4>
        <div v-for="embalagem in volume.embalagens" :key="embalagem.id" class="p-4 bg-white my-4 rounded shadow">
          <div class="flex justify-between items-center">
            <div>
              <p><strong>ID da Embalagem:</strong> {{ embalagem.id }}</p>
              <p><strong>Produto ID:</strong> {{ embalagem.produto.id }}</p>
              <p><strong>Quantidade:</strong> {{ embalagem.quantidade }}</p>
            </div>
            <button @click="toggleDetalhes(embalagem)"
              class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-700 transition">
              {{ embalagem.mostrarDetalhes ? 'Esconder Detalhes' : 'Mostrar Detalhes' }}
            </button>
          </div>

          <div v-if="embalagem.mostrarDetalhes" class="mt-4">
            <p><strong>Sensores:</strong></p>

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
