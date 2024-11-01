<script setup>
import { defineProps } from 'vue';
import { useRoute } from 'vue-router';

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


</script>

<template>
  
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
                  <button class="bg-blue-500 text-white py-1 px-3 rounded mr-2 hover:bg-blue-700 transition">Ver Detalhes</button>
                </nuxt-link>

                <button class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-700 transition">Cancelar</button>
              </div>

              <div v-else-if="row.includes('Por Entregar')">
                
                <nuxt-link :to="`/sac/${username}/encomenda/${row[0]}`">
                  <button class="bg-blue-500 text-white py-1 px-3 rounded mr-2 hover:bg-blue-700 transition">Ver Detalhes</button>
                </nuxt-link>
                
                <button class="bg-green-500 text-white py-1 px-3 rounded hover:bg-green-700 transition">Tracking</button>
              </div>

              <div v-else>
                <nuxt-link :to="`/sac/${username}/encomenda/${row[0]}`">
                  <button class="bg-blue-500 text-white py-1 px-3 rounded hover:bg-blue-700 transition">Ver Detalhes</button>
                </nuxt-link>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<!-- http://localhost:3001/backend/api/sac/encomendas/2/Bernardo  -->
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
</style>
