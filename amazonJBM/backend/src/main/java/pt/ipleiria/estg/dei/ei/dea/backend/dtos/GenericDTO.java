package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import java.util.HashMap;
import java.util.Map;

public class GenericDTO {
    private Map<String, Object> fields; // Campos dinâmicos do DTO
    private String fieldName; // Nome personalizado para o campo principal

    // Construtor padrão
    public GenericDTO() {
        this.fields = new HashMap<>();
        this.fieldName = "fields"; // Nome padrão
    }

    // Construtor para definir um nome de campo personalizado
    public GenericDTO(String fieldName) {
        this.fields = new HashMap<>();
        this.fieldName = fieldName;
    }

    // Método para adicionar campos dinâmicos
    public GenericDTO addField(String field, Object value) {
        this.fields.put(field, value);
        return this;
    }

    // Método para retornar a estrutura JSON encapsulada
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put(fieldName, fields); // Usa o nome personalizado como chave principal
        return result;
    }
}
