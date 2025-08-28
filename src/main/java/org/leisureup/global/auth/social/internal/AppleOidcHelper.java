package org.leisureup.global.auth.social.internal;

import io.jsonwebtoken.*;
import java.math.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.auth.social.*;
import org.leisureup.global.exception.*;
import org.springframework.stereotype.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleOidcHelper {

    private static final String APPLE_ISS = "https://appleid.apple.com";

    private static String getUnsignedToken(String idToken) {
        String[] splitToken = idToken.split("\\.");
        if (splitToken.length != 3) {
            throw new InvalidIdentityTokenException(401, "Invalid id token encountered.");
        }
        return splitToken[0] + "." + splitToken[1] + ".";

    }

    private static PublicKey getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");

        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);

        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        return rsaKeyFactory.generatePublic(
                new RSAPublicKeySpec(n, e)
        );
    }

    public String getKidClaimUnsignedFrom(String idToken) {
        Jwt<Header, Claims> claims;

        try {
            claims = Jwts.parser()
                    .requireIssuer(APPLE_ISS)
                    .build()
                    .parseUnsecuredClaims(getUnsignedToken(idToken));
        } catch (ExpiredJwtException e) {
            throw new InvalidIdentityTokenException(401, "Token has been expired.");
        } catch (IncorrectClaimException | MissingClaimException e) {
            throw new InvalidIdentityTokenException(401, "Invalid token issuer detected.");
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected exception occurred", e);
            throw new RuntimeException(e);
        }

        return claims.getPayload().get("kid", String.class);
    }

    public OAuthResponse getVerifiedInfoFrom(
            String idToken, String modulus, String exponent
    ) {
        Claims jwsClaims = getOidcJwsClaimsWith(idToken, modulus, exponent)
                .getPayload();

        String sub = jwsClaims.get("sub", String.class);
        String email = jwsClaims.get("email", String.class);
        boolean emailVerified = jwsClaims.get("email_verified", Boolean.class);
        boolean isPrivateEmail = jwsClaims.get("is_private_email", Boolean.class);

        if (sub == null || sub.isEmpty()) {
            log.error("Invalid sub id detected.");
            throw new IllegalStateException("Invalid sub id detected.");
        }

        email = email != null && emailVerified && !isPrivateEmail ? email : null;

        return OAuthResponse.of(sub, "User", email);
    }

    private Jws<Claims> getOidcJwsClaimsWith(
            String idToken, String modulus, String exponent
    ) {
        try {
            return Jwts.parser()
                    .verifyWith(getRSAPublicKey(modulus, exponent))
                    .requireIssuer(APPLE_ISS)
                    .build()
                    .parseSignedClaims(idToken);
        } catch (ExpiredJwtException e) {
            throw new InvalidIdentityTokenException(401, "Token has been expired.");
        } catch (IncorrectClaimException | MissingClaimException e) {
            throw new InvalidIdentityTokenException(401, "Invalid token issuer detected.");
        } catch (Exception e) {
            log.error("Unexpected exception occurred", e);
            throw new RuntimeException(e);
        }
    }
}
