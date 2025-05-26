package cl.randstad.user.infraestructura.rest.dto;

import java.util.List;

import cl.randstad.common.annotation.PasswordPattern;
import cl.randstad.common.annotation.ValidEmail;
import jakarta.validation.constraints.NotEmpty;

public class UserRequest {
	@NotEmpty(message = "El nombre no puede estar vacío")
    private String name;

    @ValidEmail
    private String email;

    @PasswordPattern
    private String password;
    
    private boolean isActive;

    private List<PhoneRequest> phones;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<PhoneRequest> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneRequest> phones) {
		this.phones = phones;
	}
}
