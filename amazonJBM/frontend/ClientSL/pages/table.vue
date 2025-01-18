<script setup>
import { defineProps, defineEmits } from 'vue';
import '@fortawesome/fontawesome-free/css/all.css';

const props = defineProps({
  tableTitles: { type: Array, required: true },
  tableData: { type: Array, required: true },
  mostrarAcoes: { type: Boolean, default: true }
});

const emit = defineEmits(['tracking', 'confirmEntrega', 'expedirEncomenda']);
</script>

<template>
  <div class="table-container p-4">
    <div class="responsive-table shadow-lg rounded-lg relative overflow-x-auto">
      <table class="min-w-full bg-white rounded-lg border border-gray-300">
        <thead class="bg-gradient-to-r from-purple-600 to-indigo-600 sticky top-0 z-10">
          <tr>
            <th v-for="(title, index) in tableTitles" :key="index"
              class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">
              {{ title }}
            </th>
            <th v-if="mostrarAcoes" class="py-4 px-6 text-white font-bold text-center uppercase border border-gray-300">
              Ações
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, rowIndex) in tableData" :key="rowIndex"
            class="odd:bg-gray-100 even:bg-white hover:bg-indigo-50 transition duration-300">
            <td v-for="(cell, cellIndex) in Object.values(row)" :key="cellIndex"
              class="py-4 px-6 text-center border border-gray-300">
              {{ cell }}
            </td>
            <td v-if="mostrarAcoes"
              class="py-4 px-6 text-center border border-gray-300 flex flex-wrap justify-center space-x-2">
              <nuxt-link :to="`/sl/${row.id}`">
                <button class="bg-blue-500 text-white py-1 px-2 rounded hover:bg-blue-700 transition">
                  <i class="fas fa-eye"></i>
                </button>
              </nuxt-link>
              <button v-if="row.estado === 'Por Entregar'" @click="$emit('confirmEntrega', row.id)"
                class="bg-green-500 text-white py-1 px-2 rounded hover:bg-green-700 transition">
                <i class="fas fa-truck"></i>
              </button>
              <button v-if="row.estado === 'Em Processamento'" @click="$emit('expedirEncomenda', row.id)"
                class="bg-yellow-500 text-white py-1 px-2 rounded hover:bg-yellow-700 transition">
                <i class="fas fa-truck"></i>
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
  max-width: 100%;
  margin: 0 auto;
}

.responsive-table {
  overflow-x: auto;
}

th,
td {
  padding: 16px;
  white-space: nowrap;
}

.bg-gradient-to-r {
  background: #202c38;
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

@media (max-width: 768px) {

  th,
  td {
    padding: 8px;
    font-size: 0.875rem;
  }

  .flex-wrap {
    flex-wrap: wrap;
  }

  .space-x-2> :not([hidden])~ :not([hidden]) {
    margin-left: 0.25rem;
    margin-top: 0.25rem;
  }
}
</style>
