package com.alex.demo.util.typeHandler;


import com.alex.demo.pojo.po.JsonType;
import com.alex.demo.util.CommonUtil;
import com.alex.demo.util.JsonUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * mybatis json type handler
 */
public class JsonTypeHandler<E extends JsonType> extends BaseTypeHandler<E> {

  private final Class<E> type;

  public JsonTypeHandler(Class<E> type) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setString(i, JsonUtil.write(parameter));
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String ch = rs.getString(columnName);
    return rs.wasNull() ? CommonUtil.newInstance(type) : JsonUtil.read(ch, type);
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String ch = rs.getString(columnIndex);
    return rs.wasNull() ? CommonUtil.newInstance(type) : JsonUtil.read(ch, type);
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String ch = cs.getString(columnIndex);
    return cs.wasNull() ? CommonUtil.newInstance(type) : JsonUtil.read(ch, type);
  }

}
