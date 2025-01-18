package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EmbalagemCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ProdutoCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.VolumeCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class EncomendaBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private VolumeBean volumeBean;

    @EJB
    private TipoEmbalagemBean tipoEmbalagemBean;

    public Response create(int id, String client_username, List<VolumeCreateEncomendaDTO> volumes) {

        // Verifica se já existe uma encomenda com o ID fornecido
        if (em.find(Encomenda.class, id) != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Já existe uma encomenda com o ID fornecido!\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Verifica se o cliente existe
        Cliente cliente = clienteBean.find(client_username);
        if (cliente == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Cliente não encontrado!\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Pré-validação: IDs duplicados em volumes e embalagens e existência no banco de dados
        Set<Integer> volumeIds = new HashSet<>();
        Set<Integer> embalagemIds = new HashSet<>();

        for (VolumeCreateEncomendaDTO volume : volumes) {
            // Verifica duplicação de ID no JSON para volumes
            if (!volumeIds.add(volume.getId())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"IDs de volumes duplicados encontrados: " + volume.getId() + "!\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Verifica se o ID do volume já existe no banco de dados
            if (em.find(Volume.class, volume.getId()) != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Já existe um volume com o ID fornecido: " + volume.getId() + "!\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            for (EmbalagemCreateEncomendaDTO embalagem : volume.getEmbalagens()) {
                // Verifica duplicação de ID no JSON para embalagens
                if (!embalagemIds.add(embalagem.getId())) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"message\": \"IDs de embalagens duplicados encontrados no volume " + volume.getId() + ": " + embalagem.getId() + "!\"}")
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }

                // Verifica se o ID da embalagem já existe no banco de dados
                if (em.find(Embalagem.class, embalagem.getId()) != null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"message\": \"Já existe uma embalagem com o ID fornecido: " + embalagem.getId() + "!\"}")
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }

                // Verifica se o tipo de embalagem existe
                if (tipoEmbalagemBean.find(embalagem.getTipo()) == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"message\": \"Tipo de embalagem não encontrado: " + embalagem.getTipo() + "!\"}")
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }

                // Verifica se o produto existe
                if (em.find(Produto.class, embalagem.getProduto().getId()) == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"message\": \"Produto não encontrado: " + embalagem.getProduto().getId() + "!\"}")
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }
            }
        }

        // Criação da encomenda após validações
        Encomenda encomenda = new Encomenda(id, cliente);
        em.persist(encomenda);

        for (VolumeCreateEncomendaDTO volume : volumes) {
            Volume volume1 = new Volume(volume.getId(), encomenda);
            em.persist(volume1);

            for (EmbalagemCreateEncomendaDTO embalagem : volume.getEmbalagens()) {
                Produto produto1 = em.find(Produto.class, embalagem.getProduto().getId());
                Tipo_Embalagem tipoEmbalagem = em.find(Tipo_Embalagem.class, embalagem.getTipo());

                Embalagem embalagem1 = new Embalagem(
                        embalagem.getId(),
                        produto1,
                        volume1,
                        embalagem.getQuantidade(),
                        tipoEmbalagem
                );
                em.persist(embalagem1);

                volume1.addEmbalagem(embalagem1);
            }

            encomenda.addVolume(volume1);
        }

        return Response.ok("{\"message\": \"Encomenda criada com sucesso.\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }


    public List<Encomenda> findAll(Utilizador user) {

        List<Encomenda> encomendas = new ArrayList<>();

        if (user.getRole().equals("Cliente")) {
            Cliente cliente = em.find(Cliente.class, user.getUsername());
            Hibernate.initialize(cliente.getEncomendas());
            encomendas = cliente.getEncomendas();
        }
        else{
            encomendas = em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();
        }

        return encomendas;
    }

    public Encomenda find(int id) {
        Encomenda encomenda = em.find(Encomenda.class, id);
        Hibernate.initialize(encomenda);

        return encomenda;
    }

    public Encomenda findEncomendaById(int id) {

        var encomenda = em.find(Encomenda.class, id);

        return encomenda;
    }

    public List<Encomenda> findEncomendasByEstado(String estado, Utilizador user) {
        List<Encomenda> encomendasFiltradas = new ArrayList<>();
        List<Encomenda> todasEncomendas = em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();

        if(user.isCliente()){
            for (Encomenda encomenda : todasEncomendas) {
                if (encomenda.getEstado().equals(estado) && encomenda.getCliente().getUsername().equals(user.getUsername())){
                    encomendasFiltradas.add(encomenda);
                }
            }
        }
        else{
            for (Encomenda encomenda : todasEncomendas) {
                if (encomenda.getEstado().equals(estado)){
                    encomendasFiltradas.add(encomenda);
                }
            }
        }

        return encomendasFiltradas;
    }

    public Response mudarEstadoEncomenda(int id, String estado, Utilizador user) {

        Set<String> estadosPermitidos = Set.of("EmProcessamento", "PorEntregar", "Entregue", "Cancelada");

        if (!estadosPermitidos.contains(estado)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Estado inválido! Estados permitidos são: EmProcessamento, PorEntregar, Entregue, Cancelada.").build();
        }

        Encomenda encomenda = this.find(id);

        if (encomenda == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Encomenda não encontrada!").build();
        }

        if(user.isCliente()){
            if(!encomenda.getCliente().getUsername().equals(user.getUsername())) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Apenas pode cancelar as suas encomendas.").build();
            }

            if(!estado.equals("Cancelada")){
                return Response.status(Response.Status.BAD_REQUEST).entity("Apenas pode mudar estado de encomendas para cancelada.").build();
            }

            if(!encomenda.getEstado().equals("EmProcessamento")){
                return Response.status(Response.Status.BAD_REQUEST).entity("Apenas pode cancelar encomendas que estajam em estado 'Em Processamento'.").build();
            }
        }
        else if(user.getRole().equals("Gestor")){

            if(!estado.equals("Cancelada")){
                return Response.status(Response.Status.BAD_REQUEST).entity("Gestores apenas podem mudar estado de encomendas para cancelada.").build();
            }

            if(encomenda.getEstado().equals("Entregue")){
                return Response.status(Response.Status.BAD_REQUEST).entity("Os Gestores não pode cancelar encomendas já entregues.").build();
            }
        }
        else if(user.getRole().equals("Logista")){

            if(estado.equals("Cancelada")){
                return Response.status(Response.Status.BAD_REQUEST).entity("O Logista não pode cancelar uma ecomenda!").build();
            }

            if(estado.equals("PorEntregar") && !encomenda.getEstado().equals("EmProcessamento")){
                return Response.status(Response.Status.BAD_REQUEST).entity("Não pode expedir uma encomenda que não esteja 'Em Processamento'.").build();
            }

            if(estado.equalsIgnoreCase("Entregue")){

                for(Volume volume : encomenda.getVolumes()){
                    if(!volume.getEntregue()){
                        return Response.status(Response.Status.BAD_REQUEST).entity("Os volumes desta encomenda ainda não foram todos entregues.").build();
                    }
                }

                encomenda.setData_entrega(LocalDateTime.now());

                for(Volume volume : encomenda.getVolumes()){
                    for(Embalagem embalagem : volume.getEmbalagens()){
                        for(Sensor sensor : embalagem.getSensores()){
                            sensor.setEstado("inativo");
                        }
                    }
                }
            }

            if(estado.equals("PorEntregar")){
                for(Volume volume : encomenda.getVolumes()) {
                    for (Embalagem embalagem : volume.getEmbalagens()) {
                         List<Tipo_Sensores> tipoSensores = new ArrayList<>(embalagem.getTipo().getSensores());

                        for(Sensor sensor : embalagem.getSensores()) {
                            for(Tipo_Sensores tipo : tipoSensores){
                                if(sensor.getTipo().equals(tipo) ){
                                    tipoSensores.remove(tipo);
                                    break;
                                }
                            }
                        }

                        if(tipoSensores.size() != 0){
                            return Response.status(Response.Status.BAD_REQUEST).entity("A embalagem não contém os sensores suficientes para seguir viagem.").build();
                        }
                    }
                }

                encomenda.setData_expedicao(LocalDateTime.now());
            }
        }

        encomenda.setEstado(estado);
        em.merge(encomenda);

        return Response.ok("Estado da encomenda " + encomenda.getId() + " alterado com sucesso para " + estado).build();
    }

    public List<Object[]> getCoordenadasEncomenda(int id) {
        Encomenda encomenda = em.find(Encomenda.class, id);

        List<Object[]> coordenadasList = new ArrayList<>();

        for (Volume volume : encomenda.getVolumes()) {
            for(Embalagem embalagem : volume.getEmbalagens()) {
                for (Sensor sensor : embalagem.getSensores()) {
                    if ("GPS".equals(sensor.getTipo().getTipo())) {
                        coordenadasList.add(new Object[]{volume.getId(), embalagem.getProduto().getNome(), sensor.getValor()});
                    }
                }
            }
        }
        return coordenadasList;
    }
}
