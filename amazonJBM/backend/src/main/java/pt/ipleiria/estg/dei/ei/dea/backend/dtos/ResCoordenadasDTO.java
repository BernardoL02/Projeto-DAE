package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

public class ResCoordenadasDTO {
    private int volumeId;
    private String produtoNome;
    private String coordenadas;

    public ResCoordenadasDTO(int volumeId, String produtoNome, String coordenadas) {
        this.volumeId = volumeId;
        this.produtoNome = produtoNome;
        this.coordenadas = coordenadas;
    }

    // Getters e setters
    public int getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(int volumeId) {
        this.volumeId = volumeId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }


}
