package math.projeto3.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "users")
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(unique = true)
    @NotBlank(message = "Login deve ser informado")
    private String username;

    @NotBlank(message = "Uma senha deve ser informada")
    private String password;

    @OneToOne(mappedBy = "idUser")
    private ShoppingModel shopping;

    // Getters e Setters

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public @NotBlank(message = "Login deve ser informado") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Login deve ser informado") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Uma senha deve ser informada") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Uma senha deve ser informada") String password) {
        this.password = password;
    }

    public ShoppingModel getShopping() {
        return shopping;
    }

    public void setShopping(ShoppingModel shopping) {
        this.shopping = shopping;
    }
}
