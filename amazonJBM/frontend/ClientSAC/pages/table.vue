<script setup>
import '@fortawesome/fontawesome-free/css/all.css'; 
import { defineProps } from 'vue';
import { useRoute } from 'vue-router';

const emit = defineEmits(['verAlertas']);

const route = useRoute()
const config = useRuntimeConfig()
const api = config.public.API_URL
const username = route.params.username;


// Props to receive titles and data from parent component
const props = defineProps({
  tableTitles: {
    type: Array,
    required: true
  },
  tableData: {
    type: Array,
    required: true
  }, 
  mostrarOperacoes: {
    type: Boolean,
    required: true
  }
});

const formateEstado = (estado) => {

  if (estado === "EmProcessamento") {
    return estado.replace("EmProcessamento", "Em Processamento"); 
  }
  else if (estado === "PorEntregar") {
    return estado.replace("PorEntregar", "Por Entregar"); 
  }

  return estado;
};


let successMessage = ref("")

async function refresh() {
  try {
    const response = await $fetch(`${api}/encomendas`);
    // Atualiza a tabela com os dados recebidos
    props.tableData.splice(0, props.tableData.length, ...response.map(order => [
      order.id,                            
      new Date(order.data_expedicao).toLocaleString(),    
      new Date(order.data_entrega).toLocaleString(),     
      formateEstado(order.estado)            
    ]));

    successMessage.value = "Encomenda cancelada com sucesso!"

    setTimeout(() => (successMessage.value = ''), 3000);

  } catch (error) {
    
    successMessage.value = "Encomenda cancelada com sucesso!"
    setTimeout(() => (successMessage.value = ''), 3000);
  }
}

async function cancelar(id) {
  
  const requestOptions = {
    method: 'PATCH',
    headers: {
      "Content-Type": "application/json",
      "Accept": "application/json"
    },
    body: JSON.stringify({ estado: "Cancelada" }) 
  };

  try {
    const response = await $fetch(`${api}/encomendas/${id}`, requestOptions);

    refresh();

  } catch (error) {
    console.error("Erro ao cancelar a encomenda!");
  }
}

</script>

<template>

    <div  v-bind="$attrs" v-if="successMessage" class="fixed -top-2 ml-4 left-0 w-full flex justify-center mt-4 z-50 transition-transform transform-gpu"
    :class="{ 'animate-slide-down': successMessage, 'animate-slide-up': !successMessage }">
      <div class="bg-green-500 text-white py-2 px-4 mr-28 rounded shadow-md">
        {{ successMessage }}
      </div>
    </div> 
    
    <div class="table-container p-8">
      <div :class="{'overflow-y-auto max-h-96': tableData.length > 7}" class="shadow-lg rounded-lg relative">
        <table class="min-w-full bg-white rounded-lg border border-gray-300">
          
          <!-- Table Headings -->
          <thead class="bg-gradient-to-r from-purple-600 to-indigo-600 sticky top-0 z-10">
            <tr>
              <th v-for="(title, index) in tableTitles" :key="index" class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">
                {{ title }}
              </th>
              <th v-if="mostrarOperacoes == true" class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">Operações</th>
            </tr>
          </thead>

          <!-- Table Data -->
          <tbody>
            <tr v-for="(row, rowIndex) in tableData" :key="rowIndex" class="odd:bg-gray-100 even:bg-white hover:bg-indigo-50 transition duration-300">
              <td v-for="(cell, cellIndex) in row" :key="cellIndex" class="py-4 px-6 text-center border border-gray-300">
                {{ cell }}
              </td>
              <td v-if="mostrarOperacoes == true" class="py-4 px-6 text-center border border-gray-300">
                <div v-if="row.includes('Em Processamento')">
                  
                  <nuxt-link :to="`/sac/${username}/encomenda/${row[0]}`">
                    <button class="bg-blue-500 text-white py-1 px-[10px] rounded hover:bg-blue-700 transition">
                      <i class="fas fa-eye"></i> <!-- Ícone de olho para "Ver Detalhes" -->
                    </button>
                  </nuxt-link>
                  
                  <button @click="cancelar(row[0])" class="ml-2 bg-red-500 text-white py-1 px-3 rounded hover:bg-red-700 transition">
                    <i class="fas fa-times"></i>
                  </button>
                </div>

                <div v-else-if="row.includes('Por Entregar')">
                  
                  <nuxt-link :to="`/sac/${username}/encomenda/${row[0]}`">
                    <button class="bg-blue-500 text-white py-1 px-[10px] rounded hover:bg-blue-700 transition">
                      <i class="fas fa-eye"></i> <!-- Ícone de olho para "Ver Detalhes" -->
                    </button>
                  </nuxt-link>
                  
                  <button  class="ml-2 bg-green-500 text-white py-1 px-3 rounded hover:bg-green-700 transition">
                    <i class="fas fa-map-marker-alt"></i> <!-- Ícone de localização para "Tracking" -->
                  </button>

                  <button @click="emit('verAlertas', row[0])" class="ml-2 bg-yellow-500 text-white py-1 px-3 rounded hover:bg-yellow-600 transition">
                    <i class="fas fa-bell"></i> <!-- Ícone de campainha para "Alertas" -->
                  </button>

                </div>

                <div v-else>
                  <nuxt-link :to="`/sac/${username}/encomenda/${row[0]}`">
                    <button class="bg-blue-500 text-white py-1 px-[10px] rounded hover:bg-blue-700 transition">
                      <i class="fas fa-eye"></i> <!-- Ícone de olho para "Ver Detalhes" -->
                    </button>
                  </nuxt-link>

                  <button @click="emit('verAlertas', row[0])" class="ml-2 bg-yellow-500 text-white py-1 px-3 rounded hover:bg-yellow-600 transition">
                    <i class="fas fa-bell"></i> <!-- Ícone de campainha para "Alertas" -->
                  </button>
                  
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

</template>

<style scoped>
.table-container {
  max-width: 90%;
  margin: 0 auto;
}
th, td {
  padding: 16px;
}
.bg-gradient-to-r {
  background: #202c38;
}
.text-white {
  color: #ffffff;
}
.odd\:bg-gray-100 {
  background-color: #f7fafc;
}
.even\:bg-white {
  background-color: #ffffff;
}
.hover\:bg-indigo-50:hover {
  background-color: #ebf4ff;
}
button {
  cursor: pointer;
}
.overflow-y-auto {
  overflow-y: auto;
}
.max-h-96 {
  max-height: 24rem;
}
.sticky {
  position: sticky;
}
.top-0 {
  top: 0;
}
.z-10 {
  z-index: 10;
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
