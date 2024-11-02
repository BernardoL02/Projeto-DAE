package pt.ipleiria.estg.dei.ei.dea.backend.ws;


import jakarta.ejb.EJB;
import jakarta.ws.rs.*;

import java.util.List;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.*;
import pt.ipleiria.estg.dei.ei.dea.backend.ejbs.*;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;

@Path("sl")
 @Produces({MediaType.APPLICATION_JSON})
 @Consumes({MediaType.APPLICATION_JSON})

 public class LogistaService {

     @EJB
     private EncomendaBean encomendaBean;

     @EJB
     private ClienteBean clienteBean;

     @EJB
     private ProdutoBean produtoBean;

     @EJB
     private VolumeBean volumeBean;

     @EJB
     private SensorBean sensorBean;
     @EJB
     private TipoSensoresBean tipoSensoresBean;

     @GET
     @Path("encomendas/estado/{estado}")
     public Response getEncomendaByEstado(@PathParam("estado") String estado) {

         List<Encomenda> encomendas = encomendaBean.findEncomendasByEstado(estado);
         return Response.ok(ResEncomendaEstadoDTO.from(encomendas)).build();
     }

    @GET
    @Path("clientes")
    public Response getClientes() {
        var clientes = clienteBean.findAll();
        return Response.ok(ResClienteDTO.from(clientes)).build();
    }

    @GET
    @Path("produtos")
    public Response getProdutos() {
         var produtos = produtoBean.findAll();
         return Response.ok(ProdutoDTO.from(produtos)).build();
    }

    @GET
    @Path("encomendas/{id}")
    public Response getEncomendasById(@PathParam("id") int id) {
        var encomenda = encomendaBean.find(id);
        return Response.ok(ResEncomendaDetalhesDTO.from(encomenda)).build();
    }

    @PATCH
    @Path("encomendas/{id}/Entregue")
    public Response entregarEncomenda(@PathParam("id") int id) {
        encomendaBean.entregarEncomenda(id);
        return Response.ok("Encomenda " + id + " concluida com sucesso.").build();
    }

    @GET
    @Path("volume/{id}")
    public Response getDetalhesVolume(@PathParam("id") int id){
        var volume = volumeBean.find(id);
        return Response.ok(ResVolumeDetalhesDTO.from(volume)).build();
    }

    @POST
    @Path("volume/{id}/sensor")
    public Response associarSensorAVolume(@PathParam("id") int id,SensorDTO sensorDTO) {

         if("GPS".equals(sensorDTO.getTipoNome())){
             sensorBean.create(
                     sensorDTO.getValor(),
                     sensorDTO.getTipoId(),
                     sensorDTO.getEstado(),
                     sensorDTO.getBateria(),
                     id
             );
         }else{
             sensorBean.create(
                     sensorDTO.getValor(),
                     sensorDTO.getTipoId(),
                     sensorDTO.getEstado(),
                     sensorDTO.getBateria(),
                     sensorDTO.getValMax(),
                     sensorDTO.getValMin(),
                     id
             );
         }

        return Response.ok("Sensor associado com sucesso").build();
    }

    @POST
    @Path("encomendas")
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
    @Path("tipoSensores")
    public Response getTipoSensores() {
        var tipoSensores = tipoSensoresBean.findAll();
        return Response.ok(ResTipoSensoresDTO.from(tipoSensores)).build();
    }

    @POST
    @Path("encomendas/{id}/volume")
    public Response associarVolumeEncomenda(@PathParam("id") int id_encomenda, VolumeDTO volumeDTO){
        volumeBean.create(
                volumeDTO.getId_produto(),
                volumeDTO.getQuantidade(),
                id_encomenda
        );
        return Response.ok("Volume associado com sucesso").build();
    }

 }
