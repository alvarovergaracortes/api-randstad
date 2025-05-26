package cl.randstad.user.infraestructura.rest.dto;

public record PhoneResponse(
		String number,
		String citycode,
		String contrycode
)
{}
