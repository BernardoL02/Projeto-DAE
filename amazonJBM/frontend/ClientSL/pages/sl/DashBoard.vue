<script setup>
import Template from "../template.vue";
import Table from "../table.vue";
import { ref, onMounted } from "vue";
import { useRuntimeConfig } from "#app";

const config = useRuntimeConfig();
const api = config.public.API_URL;

const currentPage = "DashBoard";

const mostrarModalProdutos = ref(false);
const mostrarModalCriar = ref(false);
const produtos = ref([]);
const categorias = ref([]);
const novoProduto = ref({
  id: null,
  nome: "",
  id_categoria: null,
});
const successMessage = ref("");
const errorMessages = ref([]);

// Dados para a pesquisa
const searchCategoria = ref("");
const showCategoriaSuggestions = ref(false);
const filteredCategorias = computed(() => {
  const results = categorias.value.filter((categoria) =>
    categoria.nome.toLowerCase().includes(searchCategoria.value.toLowerCase())
  );
  return results;
});

// Métodos para manipular sugestões
const selectCategoria = (categoria) => {
  novoProduto.value.id_categoria = categoria.id;
  searchCategoria.value = categoria.nome;
  showCategoriaSuggestions.value = false;
};

const hideCategoriaSuggestions = () => {
  setTimeout(() => {
    showCategoriaSuggestions.value = false;
  }, 200);
};

const searchProduto = ref("");
const filteredProdutos = computed(() => {
  if (!searchProduto.value) {
    return produtos.value.map((produto) => ({
      Id: produto.id,
      Nome: produto.nome,
      Categoria: produto.categoria,
    })); // Retorna todos os produtos com ID, Nome e Categoria
  }
  return produtos.value
    .filter(
      (produto) =>
        produto.nome.toLowerCase().includes(searchProduto.value.toLowerCase()) ||
        produto.categoria
          .toLowerCase()
          .includes(searchProduto.value.toLowerCase())
    )
    .map((produto) => ({
      Id: produto.id,
      Nome: produto.nome,
      Categoria: produto.categoria,
    })); // Retorna apenas os produtos filtrados com ID, Nome e Categoria
});




const mostrarModalTipos = ref(false);
const mostrarModalCriarTipo = ref(false);

const tiposSensores = ref([]);
const novoTipo = ref({
  id: "",
  tipo: "",
});

const searchTipo = ref("");
const filteredTipos = computed(() => {
  if (!searchTipo.value) {
    return tiposSensores.value;
  }
  return tiposSensores.value.filter((tipo) =>
    tipo.tipo.toLowerCase().includes(searchTipo.value.toLowerCase())
  );
});

const filteredTiposTableData = computed(() =>
  filteredTipos.value.map((sensor) => ({
    Id: sensor.id, // Inclui o ID do tipo
    Tipo: sensor.tipo, // Nome do tipo
  }))
);

const mostrarModalEmbalagens = ref(false);
const mostrarModalCriarEmbalagem = ref(false);

const embalagens = ref([]);
const novoTipoEmbalagem = ref({
  id: "",
  tipo: "",
  tipos_sensores: [],
});

const selectedSensores = ref([]);
const searchSensor = ref("");

// Filtra tipos de sensores com base na pesquisa
const filteredTiposSensores = computed(() => {
  if (!searchSensor.value) return tiposSensores.value;
  return tiposSensores.value.filter((sensor) =>
    sensor.tipo.toLowerCase().includes(searchSensor.value.toLowerCase())
  );
});

// Variável para controlar as sugestões visíveis
const showSensorSuggestions = ref(false);

// Função para adicionar um sensor à lista de sensores selecionados
const addSensor = (sensor) => {
  if (!sensor.id) {
    console.error("Erro: O objeto sensor não contém um campo id.");
    return;
  }

  // Adiciona o sensor diretamente, permitindo duplicatas
  selectedSensores.value.push(sensor);

  searchSensor.value = ""; // Limpa o campo de pesquisa
  showSensorSuggestions.value = false; // Oculta as sugestões
};

// Função para remover um sensor da lista de sensores selecionados
const removeSensor = (index) => {
  selectedSensores.value.splice(index, 1); // Remove o sensor pelo índice
};

// Função para obter o token do sessionStorage
const getToken = () => sessionStorage.getItem("token");

// Função para exibir mensagens de erro
const showError = (message) => {
  errorMessages.value.push(message);
  setTimeout(() => {
    errorMessages.value.shift();
  }, 5000);
};

// Função para buscar todos os produtos
const fetchProdutos = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/produto`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Erro ao buscar produtos");
    }

    const data = await response.json();
    produtos.value = data.map((produto) => ({
      id: produto.id,
      nome: produto.nome,
      categoria: produto.categoria,
    }));
  } catch (error) {
    console.error("Erro ao buscar produtos:", error);
  }
};

// Função para buscar categorias
const fetchCategorias = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/categoria`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) throw new Error("Erro ao buscar categorias");

    categorias.value = await response.json();
  } catch (error) {
    console.error(error.message);
  }
};

// Função para criar um novo produto
const criarProduto = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/produto`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(novoProduto.value),
    });

    if (!response.ok) {
      // Tenta extrair a mensagem do corpo da resposta
      const errorData = await response.json();
      throw new Error(errorData.message || "Erro ao criar produto");
    }

    successMessage.value = "Produto criado com sucesso!";
    setTimeout(() => (successMessage.value = ""), 3000);

    fetchProdutos();

    novoProduto.value = { id: "", nome: "", id_categoria: "" }; // Reseta o formulário
    mostrarModalCriar.value = false; // Fecha o modal
  } catch (error) {
    // Exibe a mensagem de erro retornada ou uma padrão
    showError(error.message || "Erro ao criar produto");
  }
};

// Função para buscar todos os tipos de sensores
const fetchTiposSensores = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/sensor/tipo`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Erro ao buscar tipos de sensores");
    }

    const data = await response.json();
    tiposSensores.value = data.map((tipo) => ({
      id: tipo.id,
      tipo: tipo.tipo,
    }));
  } catch (error) {
    console.error("Erro ao buscar tipos de sensores:", error);
    showError("Erro ao buscar tipos de sensores");
  }
};

// Função para criar um novo tipo de sensor
const criarTipo = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/sensor/tipo`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(novoTipo.value),
    });

    if (!response.ok) {
      // Tenta extrair a mensagem do corpo da resposta
      const errorData = await response.json();
      throw new Error(errorData.message || "Erro ao criar tipo de sensor");
    }

    successMessage.value = "Tipo de sensor criado com sucesso!";
    setTimeout(() => (successMessage.value = ""), 3000);

    fetchTiposSensores();

    novoTipo.value = { id: "", tipo: "" }; // Reseta o formulário
    mostrarModalCriarTipo.value = false; // Fecha o modal
  } catch (error) {
    // Exibe a mensagem de erro retornada ou uma padrão
    showError(error.message || "Erro ao criar tipo de sensor");
  }
};

// Função para buscar embalagens
const fetchEmbalagens = async () => {
  try {
    const token = getToken();
    const response = await fetch(`${api}/embalagem/tipo`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Erro ao buscar tipos de embalagem");
    }

    // Mapeia apenas os campos relevantes para a lista de tipos de embalagem
    const data = await response.json();
    embalagens.value = data.map((embalagem) => ({
      id: embalagem.id,
      tipo: embalagem.tipo,
      sensores: embalagem.tipoSensorDTO.map((sensor) => sensor.tipo),
    }));
  } catch (error) {
    showError("Erro ao buscar tipos de embalagem");
  }
};

// Função para criar nova embalagem
const criarEmbalagem = async () => {
  try {
    console.log("Sensores Selecionados:", selectedSensores.value);
    console.log("Novo Tipo de Embalagem:", novoTipoEmbalagem.value);

    // Formatar o corpo da requisição
    const requestBody = {
      id: novoTipoEmbalagem.value.id,
      tipo: novoTipoEmbalagem.value.tipo.trim(),
      tipos_sensores: selectedSensores.value.map((sensor) => ({
        id: parseInt(sensor.id, 10),
      })),
    };

    console.log(
      "Body da requisição formatado:",
      JSON.stringify(requestBody, null, 2)
    );

    // Envio da requisição
    const token = getToken();
    const response = await fetch(`${api}/embalagem/tipo`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(requestBody),
    });

    if (!response.ok) {
      // Tenta extrair a mensagem do corpo da resposta
      const errorData = await response.json();
      throw new Error(errorData.message || "Erro ao criar tipo de embalagem");
    }

    successMessage.value = "Tipo de embalagem criado com sucesso!";
    setTimeout(() => (successMessage.value = ""), 3000);

    // Atualiza a lista de embalagens e reseta os valores
    fetchEmbalagens();
    novoTipoEmbalagem.value = { id: "", tipo: "", tipos_sensores: [] };
    selectedSensores.value = [];
    mostrarModalCriarEmbalagem.value = false;
  } catch (error) {
    showError(error.message || "Erro ao criar tipo de embalagem");
  }
};

onMounted(() => {
  fetchProdutos();
  fetchCategorias();
  fetchTiposSensores();
  fetchEmbalagens();
});
</script>

<template>
  <Template :currentPage="currentPage"></Template>

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

  <div class="flex justify-center items-center mt-20">
    <h1>Sistema de Logistica - Dashboard</h1>
  </div>

  <div class="flex flex-col space-x-28 justify-center items-center">
    <div class="flex flex-col justify-center w-[405px]">
      <!-- Listagem de Produtos -->
      <div
        class="mb-10 flex flex-col justify-center mx-auto mt-10 p-6 bg-white shadow-md rounded-lg border border-gray-300 w-full max-w-5xl items-center">
        <div class="flex flex-row space-x-4 relative">
          <div
            class="left-[-225px] top-[-40px] absolute inset-0 flex justify-center items-center w-12 h-12 border border-gray-300 bg-white rounded-full">
            <img src="../../public/Images/produtos.png" alt="Ícone de Embalagens" class="w-7 h-7" />
          </div>
        </div>

        <h2 class="text-xl font-semibold mb-4">Produtos</h2>
        <div class="flex flex-row space-x-6">
          <button @click="mostrarModalProdutos = true"
            class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700 transition">
            Mostrar Produtos
          </button>

          <button @click="mostrarModalCriar = true"
            class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition">
            Criar Produto
          </button>
        </div>

        <!-- Modal para mostrar produtos -->
        <div v-if="mostrarModalProdutos"
          class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
          <div class="bg-white w-3/4 p-6 rounded shadow-lg relative max-h-[90vh] overflow-y-auto">
            <button @click="mostrarModalProdutos = false"
              class="absolute top-2 right-2 bg-gray-200 text-gray-600 hover:text-gray-900 hover:bg-white rounded-full w-8 h-8 flex items-center justify-center">
              <i class="fas fa-times"></i>
            </button>
            <h2 class="text-xl font-semibold mb-4">Produtos</h2>

            <!-- Barra de Pesquisa -->
            <div class="mb-4">
              <input type="text" v-model="searchProduto" placeholder="Pesquisar produtos..."
                class="w-full border border-gray-300 p-2 rounded focus:ring-blue-500 focus:border-blue-500" />
            </div>

            <!-- Tabela de Produtos -->
            <Table :tableTitles="['Id', 'Nome', 'Categoria']" :tableData="filteredProdutos" :mostrarAcoes="false" />

          </div>
        </div>

        <!-- Modal para criar produto -->
        <div v-if="mostrarModalCriar"
          class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
          <div class="bg-white w-1/2 p-6 rounded shadow-lg relative">
            <button @click="mostrarModalCriar = false"
              class="absolute top-2 right-2 bg-gray-200 text-gray-600 hover:text-gray-900 hover:bg-white rounded-full w-8 h-8 flex items-center justify-center">
              <i class="fas fa-times"></i>
            </button>
            <h2 class="text-xl font-semibold mb-4">Criar Novo Produto</h2>
            <form @submit.prevent="criarProduto" class="space-y-4">
              <div>
                <label for="id" class="block font-medium">Id do Produto</label>
                <input id="id" v-model="novoProduto.id" type="text" class="w-full border border-gray-300 p-2 rounded"
                  placeholder="Escreva o id do produto" />
              </div>

              <div>
                <label for="nome" class="block font-medium">Nome do Produto</label>
                <input id="nome" v-model="novoProduto.nome" type="text"
                  class="w-full border border-gray-300 p-2 rounded" placeholder="Escreva o nome do produto" />
              </div>

              <div class="mb-4">
                <label class="block text-gray-700 font-semibold mb-1">Categoria:</label>
                <input type="text" v-model="searchCategoria" @focus="showCategoriaSuggestions = true"
                  @blur="hideCategoriaSuggestions" placeholder="Pesquisar categoria"
                  class="w-full p-2 border border-gray-300 rounded mb-2 focus:ring-green-500 focus:border-green-500" />
                <div v-if="
                  showCategoriaSuggestions && filteredCategorias.length > 0
                " class="relative">
                  <ul
                    class="absolute z-10 w-full bg-white border border-gray-300 rounded max-h-48 overflow-y-auto shadow-lg">
                    <li v-for="categoria in filteredCategorias" :key="categoria.id"
                      @mousedown="selectCategoria(categoria)" class="p-2 hover:bg-gray-100 cursor-pointer">
                      {{ categoria.nome }}
                    </li>
                  </ul>
                </div>
              </div>

              <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition">
                Criar Produto
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>

    <div class="flex flex-col pr-28 w-[520px]">
      <!-- Listagem de Tipos -->
      <div
        class="mb-10 flex flex-col justify-center mx-auto p-6 bg-white shadow-md rounded-lg border border-gray-300 w-full max-w-5xl items-center">
        <div class="flex flex-row space-x-4 relative">
          <div
            class="left-[-225px] top-[-40px] absolute inset-0 flex justify-center items-center w-12 h-12 border border-gray-300 bg-white rounded-full">
            <img src="../../public/Images/sensor.png" alt="Ícone de Embalagens" class="w-8 h-8" />
          </div>
        </div>

        <h2 class="text-xl font-semibold mb-4">Tipos de Sensores</h2>
        <div class="flex flex-row space-x-6">
          <button @click="mostrarModalTipos = true"
            class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700 transition">
            Mostrar Sensores
          </button>
          <button @click="mostrarModalCriarTipo = true"
            class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition">
            Criar Sensor
          </button>
        </div>

        <!-- Modal para mostrar tipos -->
        <div v-if="mostrarModalTipos"
          class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
          <div class="bg-white w-3/4 p-6 rounded shadow-lg relative max-h-[90vh] overflow-y-auto">
            <button @click="mostrarModalTipos = false"
              class="absolute top-2 right-2 bg-gray-200 text-gray-600 hover:text-gray-900 hover:bg-white rounded-full w-8 h-8 flex items-center justify-center">
              <i class="fas fa-times"></i>
            </button>
            <h2 class="text-xl font-semibold mb-4">Tipos de Sensores</h2>
            <!-- Barra de Pesquisa -->
            <div class="mb-4">
              <input type="text" v-model="searchTipo" placeholder="Pesquisar tipos..."
                class="w-full border border-gray-300 p-2 rounded focus:ring-blue-500 focus:border-blue-500" />
            </div>

            <!-- Tabela de Tipos -->
            <Table :tableTitles="['Id', 'Tipo']" :tableData="filteredTiposTableData" :mostrarAcoes="false" />
          </div>
        </div>

        <!-- Modal para criar tipo -->
        <div v-if="mostrarModalCriarTipo"
          class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
          <div class="bg-white w-1/2 p-6 rounded shadow-lg relative">
            <button @click="mostrarModalCriarTipo = false"
              class="absolute top-2 right-2 bg-gray-200 text-gray-600 hover:text-gray-900 hover:bg-white rounded-full w-8 h-8 flex items-center justify-center">
              <i class="fas fa-times"></i>
            </button>
            <h2 class="text-xl font-semibold mb-4">Criar Novo Tipo</h2>
            <form @submit.prevent="criarTipo" class="space-y-4">
              <div>
                <label for="id" class="block font-medium">Id do Tipo Sensor</label>
                <input id="id" v-model="novoTipo.id" type="text" class="w-full border border-gray-300 p-2 rounded"
                  placeholder="Escreva o id do tipo de Sensor" />
              </div>
              <div>
                <label for="tipo" class="block font-medium">Tipo do Sensor</label>
                <input id="tipo" v-model="novoTipo.tipo" type="text" class="w-full border border-gray-300 p-2 rounded"
                  placeholder="Escreva o tipo do sensor" />
              </div>
              <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition">
                Criar Tipo
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>

    <div class="flex flex-col justify-center pr-28 w-[520px]">
      <!-- Listagem de Embalagens -->
      <div
        class="mb-10 flex flex-col justify-center mx-auto p-6 bg-white shadow-md rounded-lg border border-gray-300 w-full max-w-5xl items-center">
        <div class="flex flex-row space-x-4 relative">
          <div
            class="left-[-225px] top-[-40px] absolute inset-0 flex justify-center items-center w-12 h-12 border border-gray-300 bg-white rounded-full">
            <img src="../../public/Images/embalagem.png" alt="Ícone de Embalagens" class="w-8 h-8" />
          </div>
        </div>

        <h2 class="text-xl font-semibold mb-4">Tipos de Embalagens</h2>

        <div class="flex flex-row space-x-6">
          <button @click="mostrarModalEmbalagens = true"
            class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700 transition">
            Mostrar Embalagens
          </button>
          <button @click="mostrarModalCriarEmbalagem = true"
            class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition">
            Criar Embalagem
          </button>
        </div>

        <div v-if="mostrarModalEmbalagens"
          class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
          <div
            class="bg-white w-11/12 sm:w-3/4 md:w-2/3 lg:w-1/2 p-6 rounded-lg shadow-lg relative max-h-[90vh] overflow-y-auto max-w-2xl">
            <button @click="mostrarModalEmbalagens = false"
              class="absolute top-7 right-6 bg-gray-200 text-gray-600 hover:text-gray-900 hover:bg-white rounded-full w-8 h-8 flex items-center justify-center focus:outline-none">
              <i class="fas fa-times text-sm"></i>
            </button>
            <div class="mb-4">
              <h2 class="text-lg font-semibold">Tipos de Embalagens</h2>
              <p class="text-base text-gray-700">
                Estes são os sensores mínimos necessários para cada embalagem.
              </p>
            </div>

            <!-- Grid de cartões -->
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3 max-w-2xl">
              <div v-for="embalagem in embalagens" :key="embalagem.id"
                class="p-4 bg-gray-100 rounded-lg shadow hover:shadow-lg transition-transform transform hover:scale-105 ]">
                <h3 class="text-base font-semibold text-gray-800 mb-2">{{ embalagem.tipo }} (id: {{ embalagem.id }})
                </h3>
                <ul class="list-disc list-inside text-gray-600 text-sm">
                  <li v-if="embalagem.sensores.length == 0">
                    Não existem sensores obrigatórios
                  </li>
                  <li v-else v-for="sensor in embalagem.sensores" :key="sensor">
                    {{ sensor }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>

        <!-- Modal para criar embalagem -->
        <div v-if="mostrarModalCriarEmbalagem"
          class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
          <div class="bg-white w-1/2 p-6 rounded shadow-lg relative">
            <button @click="mostrarModalCriarEmbalagem = false"
              class="absolute top-2 right-2 bg-gray-200 text-gray-600 hover:text-gray-900 hover:bg-white rounded-full w-8 h-8 flex items-center justify-center">
              <i class="fas fa-times"></i>
            </button>
            <h2 class="text-xl font-semibold mb-4">Criar Nova Embalagem</h2>
            <form @submit.prevent="criarEmbalagem" class="space-y-4">
              <!-- Campo para id Tipo da Embalagem -->
              <div>
                <label for="tipo" class="block font-medium">Id do Tipo da Embalagem</label>
                <input id="tipo" v-model="novoTipoEmbalagem.id" type="text"
                  class="w-full border border-gray-300 p-2 rounded" placeholder="Escreva id do tipo da embalagem" />
              </div>
              <!-- Campo para o Tipo da Embalagem -->
              <div>
                <label for="tipo" class="block font-medium">Tipo da Embalagem</label>
                <input id="tipo" v-model="novoTipoEmbalagem.tipo" type="text"
                  class="w-full border border-gray-300 p-2 rounded" placeholder="Escreva o tipo da embalagem" />
              </div>

              <!-- Campo para Adicionar Tipos de Sensores -->
              <div class="mb-4">
                <label class="block text-gray-700 font-semibold mb-1">Tipos de Sensores:</label>
                <input type="text" v-model="searchSensor" @focus="showSensorSuggestions = true"
                  placeholder="Pesquisar sensores"
                  class="w-full p-2 border border-gray-300 rounded mb-2 focus:ring-blue-500 focus:border-blue-500" />
                <div class="relative">
                  <ul v-if="showSensorSuggestions"
                    class="absolute z-10 w-full bg-white border border-gray-300 rounded max-h-48 overflow-y-auto shadow-lg">
                    <li v-for="sensor in filteredTiposSensores" :key="sensor.id" @click="addSensor(sensor)"
                      class="p-2 hover:bg-gray-100 cursor-pointer">
                      {{ sensor.tipo }}
                    </li>
                  </ul>
                </div>
              </div>

              <!-- Lista de Sensores Selecionados -->
              <div v-if="selectedSensores.length > 0" class="mb-4">
                <h3 class="font-medium text-gray-700 mb-2">
                  Sensores Selecionados:
                </h3>
                <ul class="space-y-2">
                  <li v-for="(sensor, index) in selectedSensores" :key="index"
                    class="flex items-center justify-between bg-gray-100 p-2 rounded border">
                    <span>{{ sensor.tipo }}</span>
                    <button @click="removeSensor(index)" class="text-red-500 hover:text-red-700">
                      Remover
                    </button>
                  </li>
                </ul>
              </div>

              <!-- Botão para Criar Embalagem -->
              <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700 transition">
                Criar Embalagem
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
h1 {
  font-size: 1.5rem;
  font-weight: bold;
}

.fixed div {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

input {
  outline: none;
  transition: border-color 0.3s;
}

input:focus {
  border-color: #2563eb;
  /* Azul */
  box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.3);
  /* Foco com sombra */
}
</style>
