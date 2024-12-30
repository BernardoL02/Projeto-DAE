<script setup>
import { defineProps, defineEmits } from 'vue';
import '@fortawesome/fontawesome-free/css/all.css'; // Importa a biblioteca de ícones Font Awesome

// Definindo props para os dados e controle da exibição da coluna de ações
const props = defineProps({
  tableTitles: {
    type: Array,
    required: true
  },
  tableData: {
    type: Array,
    required: true
  },
  mostrarAcoes: { // Nova prop para controlar a exibição da coluna de ações
    type: Boolean,
    default: true
  }
});

// Definindo os eventos que podem ser emitidos
const emit = defineEmits(['cancelar', 'verAlertas', 'tracking']);
</script>

<template>
  <div class="table-container p-8">
    <div :class="{ 'overflow-y-auto max-h-96': tableData.length > 7 }" class="shadow-lg rounded-lg relative">
      <table class="min-w-full bg-white rounded-lg border border-gray-300">
        <thead class="bg-gradient-to-r from-purple-600 to-indigo-600 sticky top-0 z-10">
          <tr>
            <th v-for="(title, index) in tableTitles" :key="index"
              class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">
              {{ title }}
            </th>
            <!-- Renderizar a coluna de ações apenas se mostrarAcoes for true -->
            <th v-if="mostrarAcoes" class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">
              Ações</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="(row, rowIndex) in tableData" :key="rowIndex"
            class="odd:bg-gray-100 even:bg-white hover:bg-indigo-50 transition duration-300">
            <td v-for="(cell, cellIndex) in Object.values(row)" :key="cellIndex"
              class="py-4 px-6 text-center border border-gray-300">
              {{ cell }}
            </td>
            <!-- Renderizar as ações apenas se mostrarAcoes for true -->
            <td v-if="mostrarAcoes" class="py-4 px-6 text-center border border-gray-300 flex justify-center space-x-2">
              <nuxt-link :to="`/so/encomenda/${row.id}`">
                <button class="bg-blue-500 text-white py-1 px-2 rounded hover:bg-blue-700 transition">
                  <i class="fas fa-eye"></i> <!-- Ícone de olho para "Ver Detalhes" -->
                </button>
              </nuxt-link>
              <button v-if="row.estado === 'Em Processamento'" @click="emit('cancelar', row.id)"
                class="bg-red-500 text-white py-1 px-2 rounded hover:bg-red-700 transition">
                <i class="fas fa-times"></i> <!-- Ícone de X para "Cancelar" -->
              </button>
              <button v-if="row.estado === 'Por Entregar'" @click="emit('tracking', row.id)"
                class="bg-green-500 text-white py-1 px-2 rounded hover:bg-green-700 transition">
                <i class="fas fa-map-marker-alt"></i> <!-- Ícone de localização para "Tracking" -->
              </button>
              <button v-if="row.estado === 'Por Entregar'" @click="emit('verAlertas', row.id)"
                class="bg-yellow-500 text-white py-1 px-2 rounded hover:bg-yellow-600 transition">
                <i class="fas fa-bell"></i> <!-- Ícone de campainha para "Alertas" -->
              </button>
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

th,
td {
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
</style>
