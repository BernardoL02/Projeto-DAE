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
const quantidade = ref(1);
const valMax = ref(null);
const valMin = ref(null);
const sensorId = ref(null);
const successMessage = ref('');
const errorMessages = ref([]);
const embalagemSelecionada = ref(null);
const tiposEmbalagem = ref([]);
const embalagens = ref([]);
const tipoSensorSelecionado = ref(null);

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem('token');

// Função para exibir a mensagem de erro como um alerta estilizado
const showError = (message) => {
  errorMessages.value.push(message);

  setTimeout(() => {
    errorMessages.value.shift();
  }, 5000);
};

const produtoSelecionado = reactive({
  searchTerm: "",
  showSuggestions: false,
  produto: null,
});

const filteredProdutos = (searchTerm) => {
  if (!searchTerm) {
    return produtos.value;
  }
  return produtos.value.filter((produto) =>
    produto.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );
};

const selectProduto = (produto) => {
  produtoSelecionado.produto = produto;
  produtoSelecionado.searchTerm = `(ID: ${produto.id}) - ${produto.nome}`;
  produtoSelecionado.showSuggestions = false;
};

const hideProdutoSuggestions = () => {
  setTimeout(() => {
    produtoSelecionado.showSuggestions = false;
  }, 200);
};


const tipoSelecionado = reactive({
  searchTerm: "",
  showSuggestions: false,
  tipo: null,
});

const filteredTiposEmbalagem = (searchTerm) => {
  if (!searchTerm) {
    return tiposEmbalagem.value;
  }
  return tiposEmbalagem.value.filter((tipo) =>
    tipo.tipo.toLowerCase().includes(searchTerm.toLowerCase())
  );
};

const selectTipo = (tipo) => {
  tipoSelecionado.tipo = tipo;
  tipoSelecionado.searchTerm = `(id: ${tipo.id}) - ${tipo.tipo}`;
  tipoSelecionado.showSuggestions = false;
};

const hideTipoSuggestions = () => {
  setTimeout(() => {
    tipoSelecionado.showSuggestions = false;
  }, 200);
};

// Função para formatar o estado da encomenda
const formateEstado = (estado) => {
  return estado === "EmProcessamento" ? "Em Processamento" : estado === "PorEntregar" ? "Por Entregar" : estado || "Indefinido";
};

// Função para alternar a exibição de detalhes do volume
const toggleVolume = (volume) => {
  volume.mostrarDetalhes = !volume.mostrarDetalhes;
};

// Função para alternar a exibição de sensores da embalagem
const toggleEmbalagem = (embalagem) => {
  embalagem.mostrarSensores = !embalagem.mostrarSensores;
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
    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    tiposSensores.value = await response.json();
  } catch (error) {
    showError(error.message);
  }
};

// Função para abrir o modal de associação de sensor
const handleAssociarSensor = (embalagem) => {
  volumeSelecionado.value = null;
  embalagemSelecionada.value = embalagem;
  tipoSensorSelecionado.value = null;
  valMax.value = null;
  valMin.value = null;
  sensorId.value = null;
  mostrarAssociarSensorModal.value = true;
};

const handleRemoverSensor = async (idEmbalagem, idSensor) => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/embalagem/${idEmbalagem}/sensor/${idSensor}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    await fetchEncomendaDetalhes();

  } catch (error) {
    showError(error.message);
  }
};


// Função para gerar um valor aleatório dentro de um intervalo
const getRandomValueInRange = (min, max) => {
  return Math.floor(Math.random() * (max - min + 1)) + min;
};

// Função para associar um sensor a um volume
const associarSensor = async () => {
  if (sensorId.value === null) {
    showError("Escreva o id do Sensor.");
    return;
  }

  if (!tipoSensorSelecionado.value) {
    showError("Selecione um tipo de sensor.");
    return;
  }

  if (!embalagemSelecionada.value) {
    showError("Nenhuma embalagem selecionada.");
    return;
  }

  try {
    let valor = null;
    if (tipoSensorSelecionado.value.id === 4) {
      // Coordenadas para tipo GPS
      const coordenadas = ["39.73440231964457: -8.821080620077632", "39.74906316836962: -8.81280859823362"];
      valor = coordenadas[Math.floor(Math.random() * coordenadas.length)];
    } else {
      if (valMax.value === null || valMin.value === null) {
        showError("Escreva os valores máximo e mínimo.");
        return;
      }
      valor = getRandomValueInRange(valMin.value, valMax.value);
    }

    let id = sensorId.value;

    const sensorData = {
      id,
      valor,
      tipoId: tipoSensorSelecionado.value.id,
      tipoNome: tipoSensorSelecionado.value.tipo,
      estado: "ativo",
      bateria: 100,
      ...(tipoSensorSelecionado.value.id !== 4 && { valMax: valMax.value, valMin: valMin.value }),
    };

    const token = getToken();
    const response = await fetch(`${api}/embalagem/${embalagemSelecionada.value.id}/sensor`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(sensorData),
    });

    if (!response.ok) {
      // Tenta extrair a mensagem do corpo da resposta
      const errorData = await response.text();
      throw new Error(errorData);
    }

    successMessage.value = "Sensor associado com sucesso!";
    setTimeout(() => (successMessage.value = ""), 3000);

    mostrarAssociarSensorModal.value = false;

    // Atualiza os sensores da embalagem após a associação
    await fetchEncomendaDetalhes();
  } catch (error) {
    showError(error.message || "Erro ao criar a encomenda");
  }
};


// Buscar Tipos de Embalagem
const fetchTiposEmbalagem = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/embalagem/tipos`, {
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
    tiposEmbalagem.value = await response.json();
  } catch (error) {
    showError(error.message);
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
    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    produtos.value = await response.json();
  } catch (error) {
    showError(error.message);
  }
};

const volumeId = ref(null);
const embalagemId = ref("");

// Adicionar Embalagem à Lista
const adicionarEmbalagem = () => {
  // Verificar se o ID da embalagem foi fornecido
  if (!embalagemId.value) {
    showError("Insira um ID para a embalagem.");
    return;
  }

  // Verificar se o ID da embalagem é único
  if (embalagens.value.some((emb) => emb.id === embalagemId.value)) {
    showError("O ID da embalagem já está em uso. Insira um ID único.");
    return;
  }

  if (!produtoSelecionado.produto || !tipoSelecionado.tipo || quantidade.value <= 0) {
    showError("Selecione um produto, um tipo de embalagem e insira uma quantidade válida.");
    return;
  }

  // Adicionar a embalagem à lista
  embalagens.value.push({
    id: embalagemId.value, // ID definido pelo usuário
    produto: { id: produtoSelecionado.produto.id },
    tipo: tipoSelecionado.tipo.id,
    quantidade: quantidade.value,
    produtoNome: produtoSelecionado.produto.nome,
    tipoNome: tipoSelecionado.tipo.tipo,
  });

  // Resetar campos
  embalagemId.value = ""; // Resetar o ID da embalagem
  produtoSelecionado.produto = null;
  produtoSelecionado.searchTerm = "";
  tipoSelecionado.tipo = null;
  tipoSelecionado.searchTerm = "";
  quantidade.value = 1;
};



// Remover Embalagem
const removerEmbalagem = (index) => {
  embalagens.value.splice(index, 1);
};

// Enviar Volume com Embalagens
const adicionarVolume = async () => {
  if (!volumeId.value) {
    showError("Insira um ID para o volume.");
    return;
  }

  if (embalagens.value.length === 0) {
    showError("Adicione ao menos uma embalagem ao volume.");
    return;
  }

  // Estrutura do JSON para envio
  const volumeData = {
    id: volumeId.value,
    embalagens: embalagens.value.map(({ id, produto, tipo, quantidade }) => ({
      id,
      produto,
      tipo,
      quantidade,
    })),
  };

  try {
    const token = getToken();
    const response = await fetch(`${api}/encomendas/${encomendaId}/volume`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(volumeData),
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    successMessage.value = "Volume adicionado com sucesso!";
    setTimeout(() => (successMessage.value = ""), 3000);

    await fetchEncomendaDetalhes();

    mostrarAdicionarProdutoModal.value = false;
    embalagens.value = [];
    volumeId.value = null; // Reseta o ID do volume
  } catch (error) {
    showError(error.message);
  }
};

const VolumeEntregue = async (volume) => {
  try {
    const token = sessionStorage.getItem('token');
    const response = await fetch(`${api}/volume/${volume.id}/entregar`, {
      method: 'PATCH',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData);
    }

    successMessage.value = `Volume ${volume.id} entregue com sucesso!`;

    await fetchEncomendaDetalhes();
  } catch (error) {
    showError(error.message);
  }
};


const showModalRemoverSensor = ref(false);
const sensorToRemove = ref(null);
const embalagemToRemove = ref(null);

const confirmRemoveSensor = (idEmbalagem, idSensor) => {
  embalagemToRemove.value = idEmbalagem;
  sensorToRemove.value = idSensor;
  showModalRemoverSensor.value = true;
};

const closeModal = () => {
  showModalRemoverSensor.value = false;
  embalagemToRemove.value = null;
  sensorToRemove.value = null;
};

const confirmAndRemoveSensor = async () => {
  if (embalagemToRemove.value && sensorToRemove.value) {
    await handleRemoverSensor(embalagemToRemove.value, sensorToRemove.value);
  }
  closeModal();
};



onMounted(() => {
  fetchEncomendaDetalhes();
  fetchTiposSensores();
  fetchProdutos();
  fetchTiposEmbalagem();
});
</script>

<template>
  <Template current-page="" />

  <div v-if="successMessage" class="fixed top-0 left-0 w-full flex justify-center mt-4 z-50">
    <div class="bg-green-500 text-white py-2 px-4 rounded shadow-md">
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
      <button v-if="encomendaData.estado === 'Em Processamento'" @click="mostrarAdicionarProdutoModal = true"
        class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition mb-4">
        Adicionar Volume
      </button>
      <div v-for="volume in volumesData" :key="volume.id"
        class="mb-6 p-4 bg-gray-100 rounded-lg border border-gray-300">
        <div class="flex justify-between items-center">
          <div>
            <h3 class="font-semibold text-lg text-gray-800">
              Volume ID: {{ volume.id }}
            </h3>
            <p v-if="encomendaData.estado === 'Por Entregar'" class="text-sm text-gray-600">
              Estado do Volume: {{ volume.entregue ? 'Entregue' : 'Não Entregue' }}
            </p>
          </div>
          <div class="flex space-x-4">
            <div>
              <!-- Botão para Entregar Volume -->
              <button v-if="!volume.entregue && encomendaData.estado === 'Por Entregar'" @click="VolumeEntregue(volume)"
                class="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-700 transition">
                Entregar Volume
              </button>

              <!-- Botão para Volume já Entregue -->
              <button v-else-if="encomendaData.estado === 'Por Entregar'" disabled
                class="bg-gray-400 text-white px-3 py-1 rounded cursor-not-allowed">
                Volume Entregue
              </button>
            </div>
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
              <div class="flex flex-col space-y-4">
                <button v-if="encomendaData.estado === 'Em Processamento'" @click="handleAssociarSensor(embalagem)"
                  class="text-white px-3 py-1 rounded transition"
                  :class="embalagem.mostrarSensores ? 'bg-[#202c38] hover:bg-[#1b2530]' : 'bg-[#202c38] hover:bg-[#1b2530]'">
                  Associar Sensor
                </button>
                <button @click="toggleEmbalagem(embalagem)" class="text-white px-3 py-1 rounded transition"
                  :class="embalagem.mostrarSensores ? 'bg-[#202c38] hover:bg-[#1b2530]' : 'bg-[#202c38] hover:bg-[#1b2530]'">
                  {{ embalagem.mostrarSensores ? 'Esconder Sensores' : 'Mostrar Sensores' }}
                </button>
              </div>
            </div>


            <div v-if="embalagem.mostrarSensores" class="mt-4">
              <h4 class="font-semibold text-md text-gray-700">Sensores Associados:</h4>
              <ul>
                <li v-for="sensor in embalagem.sensores" :key="sensor.id"
                  class="p-2 bg-gray-50 my-2 rounded shadow relative">
                  <div class="flex flex-row justify-between items-center w-full">
                    <div>
                      <p><strong>ID do Sensor:</strong> {{ sensor.id }}</p>
                      <p><strong>Tipo:</strong> <strong>(id:</strong> {{ sensor.tipoId
                        }}<strong>)</strong> - {{ sensor.tipo }}</p>
                      <p><strong>Valor:</strong> {{ sensor.valor }}</p>
                      <p><strong>Bateria:</strong> {{ sensor.bateria }}%</p>
                      <p><strong>Estado:</strong> {{ sensor.estado }}</p>
                      <p><strong>Última Leitura:</strong> {{ sensor.ultimaLeitura }}</p>
                    </div>

                    <button v-if="encomendaData.estado === 'Em Processamento'"
                      @click="confirmRemoveSensor(embalagem.id, sensor.id)"
                      class="text-white px-3 py-1 rounded transition absolute top-2 right-2"
                      :class="embalagem.mostrarSensores ? 'bg-red-700 hover:bg-[#1b2530]' : 'bg-[#202c38] hover:bg-[#1b2530]'">
                      Remover Sensor
                    </button>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div v-else class="text-center mt-20">
    <p>Carregando detalhes da encomenda...</p>
  </div>

  <!-- Modal de Associar Sensor -->
  <div v-if="mostrarAssociarSensorModal"
    class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white w-1/3 p-6 rounded shadow-lg">
      <!-- Título -->
      <h2 class="text-2xl font-semibold mb-6 text-center">Associar Sensor</h2>

      <!-- Descrição -->
      <p class="text-gray-700 mb-6 text-center">
        Escolha o tipo de sensor para associar à Embalagem ID <strong>{{ embalagemSelecionada?.id }}</strong>:
      </p>

      <!-- Campo de ID do Sensor -->
      <div class="mb-4">
        <label class="block text-gray-700 font-semibold mb-2">ID do Sensor:</label>
        <input v-model="sensorId" type="number"
          class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
          placeholder="Escreva o ID do Sensor" />
      </div>

      <!-- Select de Tipo de Sensor -->
      <div class="mb-4">
        <label class="block text-gray-700 font-semibold mb-2">Tipo de Sensor:</label>
        <select v-model="tipoSensorSelecionado"
          class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400">
          <option v-for="tipo in tiposSensores" :key="tipo.id" :value="tipo">
            {{ tipo.tipo }}
          </option>
        </select>
      </div>

      <!-- Valores Máximo e Mínimo -->
      <div v-if="tipoSensorSelecionado && tipoSensorSelecionado.id !== 4" class="mb-6">
        <div class="mb-4">
          <label class="block text-gray-700 font-semibold mb-2">Valor Máximo:</label>
          <input v-model="valMax" type="number"
            class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Escreva o valor máximo" />
        </div>
        <div>
          <label class="block text-gray-700 font-semibold mb-2">Valor Mínimo:</label>
          <input v-model="valMin" type="number"
            class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Escreva o valor mínimo" />
        </div>
      </div>

      <!-- Botões -->
      <div class="flex justify-end space-x-4 mt-6">
        <button @click="associarSensor"
          class="bg-green-500 text-white px-5 py-2 rounded-lg hover:bg-green-600 transition focus:outline-none focus:ring-2 focus:ring-green-400">
          Associar
        </button>
        <button @click="mostrarAssociarSensorModal = false"
          class="bg-gray-500 text-white px-5 py-2 rounded-lg hover:bg-gray-600 transition focus:outline-none focus:ring-2 focus:ring-gray-400">
          Cancelar
        </button>
      </div>
    </div>
  </div>

  <!-- Modal de Confirmação -->
  <div v-if="showModalRemoverSensor" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg p-6 shadow-lg w-1/3">
      <h3 class="text-lg font-semibold text-gray-800 mb-4">Confirmar Remoção</h3>
      <p class="text-gray-600 mb-6">
        Tem certeza de que deseja remover este sensor? Esta ação não pode ser desfeita.
      </p>
      <div class="flex justify-end space-x-4">
        <button @click="closeModal" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">
          Cancelar
        </button>
        <button @click="confirmAndRemoveSensor" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
          Confirmar
        </button>
      </div>
    </div>
  </div>


  <!-- Modal de Adicionar Volume -->
  <div v-if="mostrarAdicionarProdutoModal"
    class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-[60]">
    <div class="bg-white w-1/3 p-6 rounded shadow-lg z-[70]">
      <h2 class="text-xl font-bold mb-4">Adicionar Volume</h2>

      <!-- Campo para ID do Volume -->
      <div class="mb-4">
        <label class="block mb-2">ID do Volume:</label>
        <input v-model="volumeId" type="number" min="1"
          class="w-full p-2 border rounded focus:ring-blue-500 focus:border-blue-500"
          placeholder="Insira o ID do volume" />
      </div>

      <!-- Lista de Embalagens -->
      <div class="mb-4">
        <h3 class="font-semibold mb-2">Embalagens no Volume</h3>
        <ul class="max-h-48 overflow-y-auto border border-gray-300 rounded">
          <li v-for="(embalagem, index) in embalagens" :key="index"
            class="p-2 bg-gray-100 rounded mb-2 flex justify-between items-center">
            <div>
              <p><strong>Embalagem Id:</strong> {{ embalagem.id }}</p>
              <p><strong>Produto:</strong> (id: {{ embalagem.produto.id }}) - {{ embalagem.produtoNome }}</p>
              <p><strong>Tipo:</strong> (id: {{ embalagem.tipo }}) - {{ embalagem.tipoNome }}</p>
              <p><strong>Quantidade:</strong> {{ embalagem.quantidade }}</p>
            </div>
            <button @click="removerEmbalagem(index)"
              class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600">Remover</button>
          </li>
        </ul>
      </div>

      <!-- Formulário para Adicionar Embalagem -->
      <div class="mb-4">
        <!-- Campo para ID da Embalagem -->
        <label class="block mb-2">ID da Embalagem:</label>
        <input v-model="embalagemId" type="text"
          class="w-full p-2 border rounded focus:ring-blue-500 focus:border-blue-500"
          placeholder="Insira o ID da embalagem" />

        <!-- Campo para Produto -->
        <label class="block mb-2 mt-4">Produto:</label>
        <input type="text" v-model="produtoSelecionado.searchTerm" @focus="produtoSelecionado.showSuggestions = true"
          @blur="hideProdutoSuggestions" placeholder="Pesquisar produto"
          class="w-full p-2 border rounded focus:ring-green-500 focus:border-green-500" />
        <div v-if="produtoSelecionado.showSuggestions && filteredProdutos(produtoSelecionado.searchTerm).length > 0"
          class="relative">
          <ul class="absolute z-10 w-full bg-white border border-gray-300 rounded max-h-48 overflow-y-auto shadow-lg">
            <li v-for="produto in filteredProdutos(produtoSelecionado.searchTerm)" :key="produto.id"
              @mousedown="selectProduto(produto)" class="p-2 hover:bg-gray-100 cursor-pointer">
              (id: {{ produto.id }}) - {{ produto.nome }}
            </li>
          </ul>
        </div>

        <!-- Campo para Tipo de Embalagem -->
        <label class="block mb-2 mt-4">Tipo de Embalagem:</label>
        <input type="text" v-model="tipoSelecionado.searchTerm" @focus="tipoSelecionado.showSuggestions = true"
          @blur="hideTipoSuggestions" placeholder="Pesquisar tipo de embalagem"
          class="w-full p-2 border rounded focus:ring-green-500 focus:border-green-500" />
        <div v-if="tipoSelecionado.showSuggestions && filteredTiposEmbalagem(tipoSelecionado.searchTerm).length > 0"
          class="relative">
          <ul class="absolute z-10 w-full bg-white border border-gray-300 rounded max-h-48 overflow-y-auto shadow-lg">
            <li v-for="tipo in filteredTiposEmbalagem(tipoSelecionado.searchTerm)" :key="tipo.id"
              @mousedown="selectTipo(tipo)" class="p-2 hover:bg-gray-100 cursor-pointer">
              (id: {{ tipo.id }}) - {{ tipo.tipo }}
            </li>
          </ul>
        </div>

        <!-- Campo para Quantidade -->
        <label class="block mb-2">Quantidade:</label>
        <input v-model="quantidade" type="number" min="1" class="w-full p-2 border rounded mb-4" />

        <!-- Botão Adicionar Embalagem -->
        <button @click="adicionarEmbalagem" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700">
          Adicionar Embalagem
        </button>
      </div>

      <div class="flex justify-end">
        <button @click="adicionarVolume" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700">Finalizar
          Volume</button>
        <button @click="mostrarAdicionarProdutoModal = false"
          class="bg-gray-500 text-white px-4 py-2 ml-2 rounded hover:bg-gray-700">Cancelar</button>
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


.max-h-48 {
  max-height: 12rem;
  /* Define altura máxima */
  overflow-y: auto;
  /* Adiciona scroll vertical */
  scrollbar-width: thin;
  /* Personaliza barra no Firefox */
}

.max-h-48::-webkit-scrollbar {
  width: 6px;
  /* Personaliza largura no Chrome/Edge */
}

.max-h-48::-webkit-scrollbar-thumb {
  background-color: #9ca3af;
  /* Cor da barra de rolagem */
  border-radius: 4px;
}

.max-h-48::-webkit-scrollbar-thumb:hover {
  background-color: #6b7280;
  /* Cor ao passar o mouse */
}
</style>
