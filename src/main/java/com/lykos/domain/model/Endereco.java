package com.lykos.domain.model;

import com.lykos.domain.model.enums.AddressType;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEndereco;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "id_cidade", nullable = false)
    private Cidade cidade;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType tipoEndereco = AddressType.RESIDENCIAL;
    
    @Column(nullable = false, length = 10)
    private String cep;
    
    @Column(nullable = false, length = 100)
    private String logradouro;
    
    @Column(nullable = false, length = 10)
    private String numero;
    
    @Column(length = 50)
    private String complemento;
    
    @Column(nullable = false, length = 50)
    private String bairro;
}