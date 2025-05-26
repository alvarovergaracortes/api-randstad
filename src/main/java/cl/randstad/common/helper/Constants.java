package cl.randstad.common.helper;

public final class Constants {
	private Constants() {}
	
	public static final String BEARER_PREFIX = "Bearer ";
	public static final String AUTHORIZATION = "Authorization";

	public static final String SECRET_KEY = "mi-clave-secreta-muy-extensa-para-HS256-con-al-menos-32-caracteres";

	public static final Long ACCESS_TOKEN_VALIDITY_SECONDS = 2_592_000L;
}
