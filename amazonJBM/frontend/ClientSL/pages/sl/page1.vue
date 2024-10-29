<script setup>
import Template from '../template.vue';
import Table from '../table.vue';

// Definindo os títulos da tabela e os dados para as encomendas do utilizador
const tableTitles = ['ID Encomenda', 'Utilizador', 'Data de Expedição', 'Data de Entrega', 'Estado'];
const tableData = [
  ['1', 'user1', '2024-10-01', '2024-10-05', 'Por entregar'],
  ['2', 'user2', '2024-10-03', '2024-10-08', 'Por entregar'],
  ['3', 'user3', '2024-10-10', '2024-10-15', 'Em Processamento'],
  ['4', 'user4', '2024-10-12', '2024-10-18', 'Em Processamento'],
  ['5', 'user1', '2024-10-15', '2024-10-20', 'Em Processamento'],
];

const currentPage = 'Home';
</script>

<template>
  <Template :currentPage="currentPage"></Template> <!-- Importar o Template -->

  <div class="flex justify-center mr-24 mt-20">
    <h1>Logistica</h1>
  </div>
  
  <Table :tableTitles="tableTitles" :tableData="tableData" />
</template>

<style scoped>
  h1 {
    font-size: 1.5rem;
    font-weight: bold;
  }
</style>
