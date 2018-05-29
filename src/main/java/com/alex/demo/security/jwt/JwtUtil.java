package com.alex.demo.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {

  // JWT security config
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String AUTH_HEADER = "Authorization";

  private static final String SECRET = "gbvvZeIigTktBc1QarwY5CQuxnveeT88FksvyvU14CXXItERdIRgD+012+ifnJZDl6wu/A/4ITNldut1UBNJdA==";
  private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS512;
  private static final Key KEY = new SecretKeySpec(Base64.getDecoder().decode(SECRET),
      ALGORITHM.getJcaName());

  public static String create(JwtUser auth) {
    return TOKEN_PREFIX + Jwts.builder().setSubject(auth.getId())
        .claim("pn", auth.getPhone())
        .claim("pf", auth.getPlatform())
        .claim("rs", auth.getRights())
        .setExpiration(auth.getExpire()).signWith(ALGORITHM, KEY).compact();
  }

  public static JwtUser parse(String token) {
    if (token.startsWith(TOKEN_PREFIX)) {
      token = token.substring(TOKEN_PREFIX.length());
    } else {
      return null;
    }

    try {
      Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
      String id = claims.getSubject();
      String phone = (String) claims.get("pn");
      String platform = (String) claims.get("pf");
      List<String> rights = (List<String>) claims.get("rs");
      Date expire = claims.getExpiration();
      return new JwtUser(id, phone, platform, rights, expire);
    } catch (JwtException ex) {
      ex.printStackTrace();
      return null;
    }
  }

}
