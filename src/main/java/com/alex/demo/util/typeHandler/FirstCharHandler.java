package com.alex.demo.util.typeHandler;


import com.alex.demo.constant.DatabaseEnum.FirstChar;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * mybatis {@link FirstChar} type handler
 */
public class FirstCharHandler<E extends FirstChar> extends BaseTypeHandler<E> {

  private final Class<E> type;
  private final E[] enums;

  public FirstCharHandler(Class<E> type) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
    this.enums = type.getEnumConstants();
    if (this.enums == null) {
      throw new IllegalArgumentException(
          type.getSimpleName() + " does not represent an enum type.");
    }
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setString(i, String.valueOf(parameter.v()));
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String ch = rs.getString(columnName);
    return rs.wasNull() ? null : valueOf(ch);
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String ch = rs.getString(columnIndex);
    return rs.wasNull() ? null : valueOf(ch);
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String ch = cs.getString(columnIndex);
    return cs.wasNull() ? null : valueOf(ch);
  }

  private E valueOf(String ch) {
    if (ch.isEmpty()) {
//      throw TException.of(ResponseEnum.DB_JUNK_DATA);
    }

    char c = ch.charAt(0);
    for (E en : enums) {
      if (c == en.v()) {
        return en;
      }
    }
    return null;
//    throw TException.of(ResponseEnum.DB_EXCEPTION);
  }

}
