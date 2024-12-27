package pt.ipleiria.estg.dei.ei.dea.backend.ws;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.*;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Utilizador;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Authenticated;

import java.util.List;
import java.util.stream.Collectors;

@Path("encomendas")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
@PermitAll
public class EncomendaService {

    @Context
    private SecurityContext securityContext;

    @EJB
    private UtilizadorBean utilizadorBean;

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private EncomendaBean encomendaBean;

    @EJB
    private VolumeBean volumeBean;

    @EJB
    private GestorBean gestorBean;

    @EJB
    private AlertaBean alertaBean;

    @GET
    @Path("/")
    @RolesAllowed({"Gestor", "Cliente", "Logista"})
    public Response getAllEncomendas() {
        Utilizador user = utilizadorBean.findOrFail(securityContext.getUserPrincipal().getName());

        List<Encomenda> encomendas = encomendaBean.findAll(user);
        return Response.ok(ResEncomendaEstadoDTO.from(encomendas)).build();
    }

    @POST
    @Path("/")
    @RolesAllowed({"Logista"})
    public Response criarEncomenda(CreateEncomendaDTO encomendasDTO) {
        Encomenda encomenda = encomendaBean.create(
                encomendasDTO.getUsername(),
                encomendasDTO.getEstado(),
                encomendasDTO.getData_expedicao(),
                encomendasDTO.getData_entrega()
        );

        encomendaBean.gerarVolumes(encomenda.getId(), encomendasDTO.getProdutos());

        return Response.ok("Encomenda criada com sucesso com ID: " + encomenda.getId()).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Gestor", "Cliente", "Logista"})
    public Response getEncomendasById(@PathParam("id") int id) {
        Utilizador user = utilizadorBean.findOrFail(securityContext.getUserPrincipal().getName());

        var encomenda = encomendaBean.findEncomendaById(id);

        if (user.isCliente() && !encomenda.getCliente().getUsername().equals(user.getUsername())) {
            return Response.status(Response.Status.FORBIDDEN).entity("Apenas pode ver os detalhes de encomendas que lhe pertencem.").build();
        }

        return Response.ok(ResEncomendaDetalhesDTO.from(encomenda,"SO")).build();
    }

    @PATCH
    @Path("/{id}")
    public Response mudarEstadoEncomenda(@PathParam("id") int id, EncomendasDTO encomendasDTO) {
        encomendaBean.mudarEstadoEncomenda(id,encomendasDTO.getEstado());
        return Response.ok("Estado da encomenda " + id + " alterado com sucesso para " + encomendasDTO.getEstado()).build();
    }

    @GET
    @Path("estado/{estado}")
    @RolesAllowed({"Gestor", "Cliente", "Logista"})
    public Response getEncomendaByEstado(@PathParam("estado") String estado) {
        Utilizador user = utilizadorBean.findOrFail(securityContext.getUserPrincipal().getName());

        List<Encomenda> encomendas = encomendaBean.findEncomendasByEstado(estado, user);
        return Response.ok( ResEncomendaEstadoDTO.from(encomendas)).build();
    }

    @GET
    @Path("/pendentes")
    @RolesAllowed({"Gestor"})
    public Response getEncomendasPendendes() {
        List<Encomenda> encomendas = encomendaBean.findPendentes();
        return Response.ok( ResEncomendaEstadoDTO.from(encomendas)).build();
    }

    @POST
    @Path("/{id}/volume")
    public Response associarVolumeEncomenda(@PathParam("id") int id_encomenda, VolumeDTO volumeDTO){
        volumeBean.create(
                volumeDTO.getId_produto(),
                volumeDTO.getQuantidade(),
                id_encomenda
        );
        return Response.ok("Volume associado com sucesso").build();
    }

    @GET
    @Path("/alertas")
    public Response getEncomendasAlertas() {
        Utilizador user = utilizadorBean.findOrFail(securityContext.getUserPrincipal().getName());

        List<Alerta> alertas = alertaBean.getEncomendasAlertas(user);
        return Response.ok(alertas.stream().map(ResEncomendasAlertasDTO::from).collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}/alertas")
    public Response getAlertasEncomenda(@PathParam("id") int id) {
        List<Alerta> alertas = alertaBean.getAlertasEncomenda(id);
        ResAlertasEncomendaDTO responseDto = ResAlertasEncomendaDTO.from(alertas);
        return Response.ok(responseDto).build();
    }

    @GET
    @Path("/{id}/coordenadas")
    public Response getCoordenadasEncomenda(@PathParam("id") int id) {
        List<Object[]> resultados = encomendaBean.getCoordenadasEncomenda(id);

        List<ResCoordenadasDTO> coordenadasDTOs = resultados.stream()
                .map(result -> new ResCoordenadasDTO((int) result[0], (String) result[1], (String) result[2]))
                .collect(Collectors.toList());

        return Response.ok(coordenadasDTOs).build();
    }

}
